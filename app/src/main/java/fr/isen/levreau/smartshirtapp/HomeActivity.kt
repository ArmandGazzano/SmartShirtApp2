package fr.isen.levreau.smartshirtapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sport_button.setOnClickListener {
            val intent = Intent(this, SportActivity::class.java)
            startActivity(intent)
        }
    }
}
