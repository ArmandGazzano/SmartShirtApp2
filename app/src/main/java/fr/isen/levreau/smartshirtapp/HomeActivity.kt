package fr.isen.levreau.smartshirtapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper.HORIZONTAL
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
        choix.add("Réglages")
        choix.add("Suivi")

        val mImage: ArrayList<Int> = ArrayList()
        mImage.add(R.drawable.sport)
        mImage.add(R.drawable.calibration)
        mImage.add(R.drawable.settings)
        mImage.add(R.drawable.suivi)

        mItent.add(Intent(this, SportActivity::class.java))
        mItent.add(Intent(this, HomeActivity::class.java))
        mItent.add(Intent(this, MainActivity::class.java))
        mItent.add(Intent(this, SportActivity::class.java))

        recyclerView.layoutManager= LinearLayoutManager(this, HORIZONTAL, false)
        recyclerView.adapter = MenuAdapter(choix, mImage, mItent, ::onDeviceClicked)
    }

    private fun onDeviceClicked(mItent: Intent) {
        startActivity(mItent)
    }

}
