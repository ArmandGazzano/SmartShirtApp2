package fr.isen.levreau.smartshirtapp.menu

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper.HORIZONTAL
import fr.isen.levreau.smartshirtapp.CalibrationActivity
import fr.isen.levreau.smartshirtapp.bdd.FollowupActivity2
import fr.isen.levreau.smartshirtapp.R
import fr.isen.levreau.smartshirtapp.bluetooth.BluetoothActivity
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    val mItent: ArrayList<Intent> = ArrayList()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val choix: ArrayList<String> = ArrayList()
        choix.add("Nouvelle séance")
        choix.add("Calibration")
        choix.add("Suivi")
        choix.add("Compte")

        val mImage: ArrayList<Int> = ArrayList()
        mImage.add(R.drawable.sport)
        mImage.add(R.drawable.calibration)
        mImage.add(R.drawable.suivi)
        mImage.add(R.drawable.settings)

        mItent.add(Intent(this, BluetoothActivity::class.java))
        mItent.add(Intent(this, CalibrationActivity::class.java))
        mItent.add(Intent(this, FollowupActivity2::class.java))
        mItent.add(Intent(this, CompoteActivity::class.java))

        recyclerView.layoutManager= LinearLayoutManager(this, HORIZONTAL, false)
        recyclerView.adapter = MenuAdapter(choix, mImage, mItent, ::onDeviceClicked)
    }
    private fun onDeviceClicked(mItent: Intent) {
        startActivity(mItent)
    }
}