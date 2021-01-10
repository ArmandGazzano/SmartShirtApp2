package fr.isen.levreau.smartshirtapp.menu

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import fr.isen.levreau.smartshirtapp.R
import kotlinx.android.synthetic.main.activity_compote.*
import kotlinx.android.synthetic.main.activity_compote_id.view.*

class CompoteActivity : AppCompatActivity() {
    private var idUser = ""
    private val USER_PREFS = "user_prefs"
    private val KEY_ID = "id"
    private val KEY_PASSWORD = "pass"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compote)

        val sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
        var GOOD_ID = sharedPreferences.getString(KEY_ID, "")
        var GOOD_PASSWORD = sharedPreferences.getString(KEY_PASSWORD, "")

        id_user.text = GOOD_ID

        deco_button.setOnClickListener {
            goToHome()
        }

        retour_button.setOnClickListener {
            goToBack()
        }

        suppr_button.setOnClickListener{
            suppCompote()
        }

        changerId_button.setOnClickListener {
            if (GOOD_ID != null) {
                GOOD_ID = changeId(GOOD_ID!!)
            }
        }

        changerMdp_button.setOnClickListener {
            if (GOOD_PASSWORD != null) {
                GOOD_PASSWORD = changePass(GOOD_PASSWORD!!)
            }
        }
    }

    private fun changeId(Good_id: String ): String{
        val dialog = AlertDialog.Builder(this)

        val editView = View.inflate(this, R.layout.activity_compote_id, null)

        var text: String=""
        dialog.setView(editView)
        dialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->  })
        dialog.setPositiveButton("Ok", DialogInterface.OnClickListener {
                _, _ ->
            text = editView.edit_id.text.toString()


            val sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
            var pass = sharedPreferences.getString(KEY_PASSWORD, "")
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            editor.putString(KEY_ID, text)
            editor.putString(KEY_PASSWORD, pass)
            editor.apply()
            id_user.text = text


        })
        dialog.show()
        return text
    }

    private fun changePass(Good_pass: String ): String{
        val dialog = AlertDialog.Builder(this)

        val editView = View.inflate(this, R.layout.activity_compote_id, null)

        var text: String=""
        dialog.setView(editView)
        dialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->  })
        dialog.setPositiveButton("Ok", DialogInterface.OnClickListener {
                _, _ ->
            text = editView.edit_id.text.toString()


            val sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
            var id = sharedPreferences.getString(KEY_ID, "")
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            editor.putString(KEY_PASSWORD, text)
            editor.putString(KEY_ID, id)
            editor.apply()
            id_user.text = text


        })
        dialog.show()
        return text
    }

    private fun suppCompote(){
        val sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
    private fun goToHome() {
        val sharedPreferences = getSharedPreferences(idUser, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun goToBack() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}