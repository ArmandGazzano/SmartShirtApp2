package fr.isen.levreau.smartshirtapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.media.audiofx.DynamicsProcessing
import android.os.Bundle
import android.provider.ContactsContract.DisplayNameSources.EMAIL
import android.provider.ContactsContract.Intents.Insert.EMAIL
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.isen.levreau.smartshirtapp.Credentials.EMAIL
import kotlinx.android.synthetic.main.activity_inscription.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class InscriptionActivity : AppCompatActivity() {
    private val KEY_ID = "id"
    private val KEY_PASSWORD = "pass"
    private val USER_PREFS = "user_prefs"
    lateinit var sharedPreferences: SharedPreferences
    lateinit var appExecutors: AppExecutors

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscription)

        appExecutors = AppExecutors()

        sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)

        validateButton.setOnClickListener {
            val userId = id.text.toString()
            val userPassword = password.text.toString()
            saveCredentials(userId, userPassword)
            sendEmail()
            Toast.makeText(this, "Identifiants incorrectes", Toast.LENGTH_SHORT).show()
        }

        back_to_home.setOnClickListener {
            goToHome()
        }
    }

    private fun saveCredentials(id: String, pass: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_ID, id)
        editor.putString(KEY_PASSWORD, pass)
        editor.apply()
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
                //Creating MimeMessage object
                val mm = MimeMessage(session)
                val emailId = mail.text.toString()
                val name = name.text.toString()
                val userId = id.text.toString()
                val userPassword = password.text.toString()

                //Setting sender address
                mm.setFrom(InternetAddress(Credentials.EMAIL))
                //Adding receiver
                mm.addRecipient(
                    Message.RecipientType.TO,
                    InternetAddress(emailId))
                //Adding subject
                mm.subject = "Bienvenue sur l'application Smart-Shirt"
                //Adding message
                mm.setText("Merci d'avoir rejoint la team Smart-Shirt $name !\n\n Vos identifiants sont: \n - identifiant : $userId \n - mot de passe : $userPassword")

                //Sending email
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
