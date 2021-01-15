package fr.isen.levreau.smartshirtapp.menu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.isen.levreau.smartshirtapp.R
import kotlinx.android.synthetic.main.activity_inscription.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val KEY_ID = "id"
    private val KEY_PASSWORD = "pass"
    private val USER_PREFS = "user_prefs"
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
        val GOOD_ID = sharedPreferences.getString(KEY_ID, "")
        val GOOD_PASSWORD = sharedPreferences.getString(KEY_PASSWORD, "")

        if (GOOD_ID?.isEmpty() != true && GOOD_PASSWORD?.isEmpty() != true) {
            //goToHome()
        }

        login_button.setOnClickListener {
            val idUser = login_id.text.toString()
            val passwordUser = login_mdp.text.toString()

            auth.signInWithEmailAndPassword(idUser, passwordUser)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithEmail:success")
                        val user = auth.currentUser
                        //updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                        // ...
                    }

                    // ...
                }

            if (idUser == GOOD_ID && passwordUser == GOOD_PASSWORD) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Identifiants incorrectes", Toast.LENGTH_SHORT).show()
            }
        }

        inscription_button.setOnClickListener {
            val intent = Intent(this, InscriptionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        //updateUI(currentUser)
    }
}
