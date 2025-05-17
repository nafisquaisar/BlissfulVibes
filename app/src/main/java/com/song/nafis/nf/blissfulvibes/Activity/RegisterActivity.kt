package com.song.nafis.nf.blissfulvibes.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.song.nafis.nf.blissfulvibes.MainActivity
import com.song.nafis.nf.blissfulvibes.R
import com.song.nafis.nf.blissfulvibes.UI.AuthViewModel
import com.song.nafis.nf.blissfulvibes.databinding.ActivityRegisterBinding
import com.song.nafis.nf.blissfulvibes.resource.Loading
import com.song.nafis.nf.blissfulvibes.resource.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            viewModel.googleSignIn(credential)

        } catch (e: Exception) {
            Toast.makeText(this, "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreateAccount.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etRePassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.register(email, password)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.authState.collectLatest { state ->
                when (state) {
                    is Resource.Loading -> {
                        Loading.show(this@RegisterActivity)
                    }

                    is Resource.Success -> {
                        Loading.hide()
                        Toast.makeText(this@RegisterActivity, "Authentication Successful", Toast.LENGTH_SHORT).show()

                        val currentUser = FirebaseAuth.getInstance().currentUser

                        val name = currentUser?.displayName ?: binding.etName.text.toString().trim()
                        val phone = currentUser?.phoneNumber ?: binding.etNumber.text.toString().trim()
                        val email = currentUser?.email ?: binding.etEmail.text.toString().trim()

                        Timber.tag("AuthViewModel").d("isNewUser: ${viewModel.isNewUser}")

                        if (viewModel.isNewUser) {
                            viewModel.saveUserData(name = name, phoneNumber = phone, email = email, password = "")
                        }

                        startActivity(Intent(this@RegisterActivity, DashBoard::class.java))
                        finish()
                    }

                    is Resource.Error -> {
                        Loading.hide()
                        Toast.makeText(this@RegisterActivity, state.message ?: "Something went wrong", Toast.LENGTH_LONG).show()
                    }

                    else -> Loading.hide()
                }
            }
        }


        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Get this from google-services.json
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

            binding.btnGoogle.setOnClickListener {
                    signInLauncher.launch(googleSignInClient.signInIntent)
            }

    }




}
