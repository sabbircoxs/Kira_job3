package com.example.kirajob3.view_06

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.findmyfriende.Viewmodel.AuthenticationViewModel_06
import com.example.kirajob3.databinding.ActivityLogin06Binding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class LoginActivity_06 : AppCompatActivity() {
    private lateinit var binding: ActivityLogin06Binding
    private lateinit var authenticationViewModel: AuthenticationViewModel_06
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLogin06Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        authenticationViewModel = ViewModelProvider(this).get(AuthenticationViewModel_06::class.java)

        binding.loginBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(this, "Please enter valid password", Toast.LENGTH_SHORT).show()
            } else {
                authenticationViewModel.login(email, password, {
                    startActivity(Intent(this, MainActivity_06::class.java))
                    finish()
                }, {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                })
            }
        }

        binding.registerTxt.setOnClickListener {
            startActivity(Intent(this, RegisterActivity_06::class.java))
        }

        binding.forgetPass.setOnClickListener {
            Toast.makeText(this, "Forgot password", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser != null) {
            startActivity(Intent(this@LoginActivity_06, MainActivity_06::class.java))
            finish()
        }
    }
}