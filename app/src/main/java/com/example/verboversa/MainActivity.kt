package com.example.verboversa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent // Import statement for Intent
import com.example.verboversa.databinding.ActivityMainBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import android.app.Application
import com.facebook.FacebookSdk
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.verboversa.ui.theme.VerboVersaTheme
import android.widget.Button




class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var callbackManager: CallbackManager
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize CallbackManager
        callbackManager = CallbackManager.Factory.create()

        // Set up the Facebook Login button
        binding.facebookLoginButton.setOnClickListener {
            // Initiate Facebook Login
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        // Handle login success
                        val intent = Intent(this@MainActivity, LoggedInActivity::class.java)
                        startActivity(intent)
                        finish() // Optionally finish the current activity if you don't want to return to it
                    }

                    override fun onCancel() {
                        // Handle login cancellation
                    }

                    override fun onError(error: FacebookException) {
                        // Handle login error
                    }
                })
        }


    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)



        binding.signUpButtonMain.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.googleLoginButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN) // RC_SIGN_IN is an integer constant for the request code
        }


    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val intent = Intent(this@MainActivity, LoggedInActivity::class.java)
                startActivity(intent)
                finish() // Optionally finish the current activity if you don't want to return to it
            } catch (e: ApiException) {
                // Google Sign-In failed, update UI appropriately
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }



}


