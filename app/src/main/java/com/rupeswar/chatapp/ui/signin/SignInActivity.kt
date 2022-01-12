package com.rupeswar.chatapp.ui.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.rupeswar.chatapp.ui.main.MainActivity
import com.rupeswar.chatapp.R
import com.rupeswar.chatapp.databinding.ActivitySignInBinding
import com.rupeswar.chatapp.models.User
import com.rupeswar.chatapp.utils.AuthUtil
import com.rupeswar.chatapp.utils.VolleySingleton
import org.json.JSONObject

class SignInActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 123
    }

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.signInButton.setOnClickListener { signIn() }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        Log.d("Sign In", "Starting Activity for Result")
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            Log.d("Sign In", "Received the result")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            binding.progressBar.visibility = View.VISIBLE
            binding.signInButton.visibility = View.GONE

            val account = task.getResult(ApiException::class.java)

            verifyIdToken(account)
        } catch (e: ApiException) {
            Log.w("Sign In", "signInResult:failed code=${e.statusCode}")
            e.printStackTrace()
            updateUI(null)
        }
    }

    private fun verifyIdToken(account: GoogleSignInAccount?) {

        if (account == null || account.idToken == null) {
            updateUI(null)
            return
        }

        val idToken = account.idToken
        val jsonObject = JSONObject()
        jsonObject.put("token", idToken)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            "${getString(R.string.server_base_url)}/verifyIdToken",
            jsonObject,
            {
                val isIdTokenValid = it.getBoolean("isIdTokenValid")
                Log.d("Sign In Activity", it.toString())

                if (isIdTokenValid) {
                    val isUserNameDefined = it.getBoolean("isUserNameDefined")
                    updateUI(account, isUserNameDefined)
                    AuthUtil.currentUser = User.fromJson(it.getJSONObject("user"))
                }
                else {
                    updateUI(null)
                    Log.e("Sign In Activity", "Invalid Token")
                }
            },
            {
                Log.d("Sign In ID Token", "An Error Occurred")
            })

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onStart() {
        super.onStart()

        val account = GoogleSignIn.getLastSignedInAccount(this)

        verifyIdToken(account)
    }

    private fun updateUI(account: GoogleSignInAccount?, isUserNameDefined: Boolean = false) {
        if (account != null) {
            val intent = Intent(
                this, if (isUserNameDefined) MainActivity::class.java
                else SetUserNameActivity::class.java
            )
            startActivity(intent)
            finish()
        } else {
            binding.signInButton.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }
}