package com.example.verboversa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button // Import statement for Button

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize your views and set up listeners
        val signUpButton: Button = findViewById(R.id.signUpButton)
        signUpButton.setOnClickListener {
            // Handle the sign-up logic here
        }
    }
}

class SpeechActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speech)
    }
}