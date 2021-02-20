package fr.isen.levreau.smartshirtapp.menu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import fr.isen.levreau.smartshirtapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var sharedPreferences = getSharedPreferences("user_id", Context.MODE_PRIVATE)

        login_button.setOnClickListener {
            when {
                TextUtils.isEmpty(login_id.text.toString().trim { it <= ' '}) -> {
                    Toast.makeText(this,"Email manquant", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(login_mdp.text.toString().trim { it <= ' '}) -> {
                    Toast.makeText(this,"Mot de passe manquant", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val email: String = login_id.text.toString().trim { it <= ' ' }
                    val password: String = login_mdp.text.toString().trim { it <= ' ' }

                    val editor = sharedPreferences.edit()
                    val mail = email.replace(".", " ")
                    editor.putString("mail", mail)
                    editor.apply()

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!

                                Toast.makeText(this, "Connexion réussi", Toast.LENGTH_SHORT)
                                    .show()

                                val intent = Intent(this, HomeActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, task.exception?.message.toString(), Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                }
            }
        }

        inscription_button.setOnClickListener{
            val intent = Intent(this, InscriptionActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}
