package fr.isen.levreau.smartshirtapp.bdd

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

        crypto_button.setOnClickListener {
            val crypto = Crypto()
            val testcrypto = crypto.cipherCoordinates(
                DatabaseValue(
                    "100dqzqzdqsdz",
                    "0",
                    "-100"
                )
            )
            Log.i("crypto",testcrypto.toString())
            val decrypt = crypto.decipherCoordinates(testcrypto)
            Log.i("crypto",decrypt.toString())
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("data")
            myRef.child(userID).setValue(testcrypto)
        }

        sharedPreferences = getSharedPreferences("user_id", Context.MODE_PRIVATE)
        userID = sharedPreferences.getString("mail", "").toString()

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("data")
        val crypto = Crypto()
        firebase_button.setOnClickListener {
            myRef.child(userID).get().addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")
            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }
        }
    }
}