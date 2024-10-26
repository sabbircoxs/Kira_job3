package com.example.kirajob3.view_06

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.findmyfriende.Viewmodel.AuthenticationViewModel_06
import com.example.findmyfriende.Viewmodel.FirestoreViewModel_06
import com.example.kirajob3.databinding.ActivityRegister06Binding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class RegisterActivity_06 : AppCompatActivity() {
    private lateinit var binding: ActivityRegister06Binding
    private lateinit var authenticationViewModel: AuthenticationViewModel_06
    private lateinit var firestoreViewModel: FirestoreViewModel_06
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegister06Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        authenticationViewModel = ViewModelProvider(this).get(AuthenticationViewModel_06::class.java)
        firestoreViewModel = ViewModelProvider(this).get(FirestoreViewModel_06::class.java)

        binding.registerBtn.setOnClickListener {
            val name = binding.displayNameEt.text.toString()
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()
            val confirmPassword = binding.conPasswordEt.text.toString()
            val location = "Don't found any location yet"
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT)
                    .show()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
            } else {
                authenticationViewModel.register(email, password, {
                    firestoreViewModel.saveUser(this, authenticationViewModel.getCurrentUserId(), name, email, location)
                    startActivity(Intent(this, MainActivity_06::class.java))
                    finish()
                }, {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                })
            }
        }
        binding.loginTxt.setOnClickListener {
            startActivity(Intent(this, LoginActivity_06::class.java))
        }

    }

    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser != null) {
            startActivity(Intent(this@RegisterActivity_06, MainActivity_06::class.java))
            finish()
        }
    }
}