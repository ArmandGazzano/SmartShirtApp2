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
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class InscriptionActivity : AppCompatActivity() {
    lateinit var appExecutors: AppExecutors


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscription)

        appExecutors = AppExecutors()

        validateButton.setOnClickListener {
            when {
                TextUtils.isEmpty(id.text.toString().trim { it <= ' '}) -> {
                    Toast.makeText(this,"Email manquant",Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(password.text.toString().trim { it <= ' '}) -> {
                    Toast.makeText(this,"Mot de passe manquant",Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val email: String = id.text.toString().trim { it <= ' ' }
                    val password: String = id.text.toString().trim { it <= ' ' }

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

            /*
            val userId = id.text.toString()

            sendEmail()
            Toast.makeText(this, "Bienvenue $userId", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
             */

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

    private fun sendEmail(){
        appExecutors.diskIO().execute {
            val props = System.getProperties()
            props.put("mail.smtp.host", "smtp.gmail.com")
            props.put("mail.smtp.socketFactory.port", "465")
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            props.put("mail.smtp.auth", "true")
            props.put("mail.smtp.port", "465")

            val session =  Session.getInstance(props,
                object : javax.mail.Authenticator() {
                    //Authenticating the password
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(Credentials.EMAIL, Credentials.PASSWORD)
                    }
                })

            try {
                val mm = MimeMessage(session)
                val emailId = mail.text.toString()
                val name = name.text.toString()
                val userId = id.text.toString()
                val userPassword = password.text.toString()

                mm.setFrom(InternetAddress(Credentials.EMAIL))
                mm.addRecipient(
                    Message.RecipientType.TO,
                    InternetAddress(emailId))
                mm.subject = "Bienvenue sur l'application Smart-Shirt"
                mm.setText("Merci d'avoir rejoint la team Smart-Shirt $name !\n\n Vos identifiants sont: \n - identifiant : $userId \n - mot de passe : $userPassword")
                Transport.send(mm)
                appExecutors.mainThread().execute {
                    //Something that should be executed on main thread.
                }

            } catch (e: MessagingException) {
                e.printStackTrace()
            }
        }
    }
}

object Credentials {
    const val EMAIL = "smartshirt.isen@gmail.com"
    const val PASSWORD = "tshirtisen83"
}