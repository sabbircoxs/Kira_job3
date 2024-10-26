package com.example.kirajob3

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.findmyfriende.Viewmodel.AuthenticationViewModel_06
import com.example.findmyfriende.Viewmodel.FirestoreViewModel_06
import com.example.findmyfriende.Viewmodel.LocationViewModel_06
import com.example.kirajob3.databinding.FragmentProfile06Binding
import com.example.kirajob3.view_06.LoginActivity_06
import com.example.kirajob3.view_06.MainActivity_06
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment_06 : Fragment() {

    private lateinit var binding: FragmentProfile06Binding
    private lateinit var authViewModel: AuthenticationViewModel_06
    private lateinit var firestoreViewModel: FirestoreViewModel_06
    private lateinit var locationViewModel: LocationViewModel_06
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfile06Binding.inflate(inflater, container, false)

        authViewModel = ViewModelProvider(this).get(AuthenticationViewModel_06::class.java)
        firestoreViewModel = ViewModelProvider(this).get(FirestoreViewModel_06::class.java)
        locationViewModel = ViewModelProvider(this).get(LocationViewModel_06::class.java)


        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(requireContext(), LoginActivity_06::class.java))

        }

        binding.homeBtn.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity_06::class.java))
        }

        loadUserInfo()
        binding.updateBtn.setOnClickListener {
            val newName = binding.NameEt.text.toString()
            val newLocation = binding.Loaction.text.toString()

            updateBtn(newName, newLocation)
        }

        return binding.root
    }


    private fun updateBtn(newName: String, newLocation: String) {
        val currentUser = authViewModel.getCurrentUser()
        if (currentUser != null) {
            val userId = currentUser.uid
            firestoreViewModel.updateUser(requireContext(), userId, newName, newLocation)
            Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), MainActivity_06::class.java))
        } else {
            Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadUserInfo() {
        val currentUser = authViewModel.getCurrentUser()
        if(currentUser != null) {
            binding.emailEt.setText(currentUser.email)
            firestoreViewModel.getUser(requireContext(), currentUser.uid){ user ->
                if (currentUser.displayName != null) {
                    binding.NameEt.setText(currentUser.displayName)
                }
            }
        }else {
            Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
        }

    }
}