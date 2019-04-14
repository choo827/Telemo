package com.choo827.telemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth


class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build()

        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ic_login)
                .setTheme(R.style.AuthTheme)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
//            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                val gotomain = Intent(this, MainActivity::class.java)
                if (user != null) {
                    gotomain.putExtra("uid", user.uid)
                    gotomain.putExtra("email", user.email)
                    gotomain.putExtra("name", user.displayName)
                }

                startActivity(gotomain)
                finish()
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // ...
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }
}
