package fr.isen.levreau.smartshirtapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val KEY_ID = "id"
    private val KEY_PASSWORD = "pass"
    private val USER_PREFS = "user_prefs"
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
        val GOOD_ID = sharedPreferences.getString(KEY_ID, "")
        val GOOD_PASSWORD = sharedPreferences.getString(KEY_PASSWORD, "")

        if (GOOD_ID?.isEmpty() != true && GOOD_PASSWORD?.isEmpty() != true) {
            goToHome()
        }

        login_button.setOnClickListener {
            val idUser = login_id.text.toString()
            val passwordUser = login_mdp.text.toString()

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
}
