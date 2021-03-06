package fr.isen.levreau.smartshirtapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.levreau.smartshirtapp.bluetooth.BluetoothActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        buttonBle.setOnClickListener {
            intent = Intent(this, BluetoothActivity::class.java)
            startActivity(intent)
        }
    }
}
