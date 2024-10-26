package com.example.kirajob3

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmyfriende.Viewmodel.AuthenticationViewModel_06
import com.example.findmyfriende.Viewmodel.FirestoreViewModel_06
import com.example.findmyfriende.Viewmodel.LocationViewModel_06
import com.example.kirajob3.Adapter_06.UserAdapter_06
import com.example.kirajob3.databinding.FragmentFriends06Binding
import com.example.kirajob3.view_06.MapsActivity_06
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class FriendsFragment_06 : Fragment() {
    private lateinit var binding: FragmentFriends06Binding
    private lateinit var firestoreViewModel: FirestoreViewModel_06
    private lateinit var authenticationViewModel: AuthenticationViewModel_06
    private lateinit var userAdapter: UserAdapter_06
    private lateinit var locationViewModel: LocationViewModel_06
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getLocation()
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentFriends06Binding.inflate(inflater,container, false)

        firestoreViewModel = ViewModelProvider(this)[FirestoreViewModel_06::class.java]
        locationViewModel = ViewModelProvider(this)[LocationViewModel_06::class.java]
        authenticationViewModel = ViewModelProvider(this)[AuthenticationViewModel_06::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationViewModel.initializeFusedLocationClient(fusedLocationClient)

        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // Permission is already granted
            getLocation()
        }
        userAdapter = UserAdapter_06(emptyList())
        binding.userRV.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        fetchUsers()

        binding.locationBtn.setOnClickListener {
            startActivity(Intent(requireContext(), MapsActivity_06::class.java))
        }


        return binding.root
    }

    private fun fetchUsers() {
        firestoreViewModel.getAllUsers(requireContext()){
            userAdapter.updateData(it)
        }
    }

    private fun getLocation() {
        locationViewModel.getLastLocation {
            // Save location to Firestore for the current user
            authenticationViewModel.getCurrentUserId().let { userId ->
                firestoreViewModel.updateUserLocation(requireContext(),userId, it)
            }
        }
    }

}