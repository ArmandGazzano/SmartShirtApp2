package fr.isen.levreau.smartshirtapp.bdd

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import fr.isen.levreau.smartshirtapp.Crypto
import fr.isen.levreau.smartshirtapp.R
import kotlinx.android.synthetic.main.activity_followup2.*


class FollowupActivity2 : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var userID: String

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followup2)

        sharedPreferences = getSharedPreferences("user_id", Context.MODE_PRIVATE)
        userID = sharedPreferences.getString("mail", "").toString()

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("data")
        val crypto = Crypto()
        firebase_button.setOnClickListener {
            myRef.child(userID).get().addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        }
    }
}