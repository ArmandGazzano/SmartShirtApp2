package fr.isen.levreau.smartshirtapp.menu

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import fr.isen.levreau.smartshirtapp.AppExecutors
import fr.isen.levreau.smartshirtapp.R
import kotlinx.android.synthetic.main.activity_inscription.*

class InscriptionActivity : AppCompatActivity() {
    lateinit var appExecutors: AppExecutors


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscription)

        appExecutors = AppExecutors()

        validateButton.setOnClickListener {
            when {
                TextUtils.isEmpty(mail.text.toString().trim { it <= ' '}) -> {
                    Toast.makeText(this,"Email manquant",Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(password.text.toString().trim { it <= ' '}) -> {
                    Toast.makeText(this,"Mot de passe manquant",Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val email: String = mail.text.toString().trim { it <= ' ' }
                    val password: String = password.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!

                                Toast.makeText(this, "Inscription réussi", Toast.LENGTH_SHORT)
                                    .show()

                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", firebaseUser.uid)
                                intent.putExtra("user_id", firebaseUser.uid)
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

        back_to_home.setOnClickListener {
            goToHome()
        }
    }

    private fun goToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}