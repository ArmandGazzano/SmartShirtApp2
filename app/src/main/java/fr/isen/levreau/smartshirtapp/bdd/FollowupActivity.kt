package fr.isen.levreau.smartshirtapp.bdd

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper.HORIZONTAL
import fr.isen.levreau.smartshirtapp.EvolutionAdapter
import fr.isen.levreau.smartshirtapp.R
import fr.isen.levreau.smartshirtapp.SeanceAdapter
import fr.isen.levreau.smartshirtapp.menu.HomeActivity
import kotlinx.android.synthetic.main.activity_followup.*

class FollowupActivity : AppCompatActivity() {

    val hautDos : ArrayList<String> = ArrayList()
    val basDos : ArrayList<String> = ArrayList()
    val centreDos : ArrayList<String> = ArrayList()
    val epG : ArrayList<String> = ArrayList()
    val epD : ArrayList<String> = ArrayList()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followup)

        return_button.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }


        seance_button.setOnClickListener(){
            evolution_button.setBackgroundResource(R.color.colorUnpressed)
            seance_button.setBackgroundResource(R.color.colorPressed)


            txt_evolution.visibility = View.INVISIBLE

            val nseance : ArrayList<String> = ArrayList()
            for (i in 1..10){
                nseance.add("$i")
            }

            initDonnees()

            recycler_seance.layoutManager= LinearLayoutManager(this)
            recycler_seance.adapter = SeanceAdapter(nseance,hautDos,basDos,centreDos,epG,epD)
            recycler_seance.visibility = View.VISIBLE

            recycler_evolution.visibility = View.INVISIBLE
        }

        evolution_button.setOnClickListener(){
            evolution_button.setBackgroundResource(R.color.colorPressed)
            seance_button.setBackgroundResource(R.color.colorUnpressed)

            recycler_seance.visibility = View.INVISIBLE
            txt_evolution.visibility = View.VISIBLE

            val post: ArrayList<String> = ArrayList()

            post.add("Haut du dos")
            post.add("Centre du dos")
            post.add("Bas du dos")
            post.add("Epaule gauche")
            post.add("Epaule droite")

            val mBarre: ArrayList<Int> = ArrayList()
            mBarre.add(R.drawable.barregrande)
            mBarre.add(R.drawable.barremoyenne)
            mBarre.add(R.drawable.barrepetite)
            mBarre.add(R.drawable.barrettpetite)

            initDonnees()

            recycler_evolution.layoutManager= LinearLayoutManager(this,  HORIZONTAL, false)
            recycler_evolution.adapter = EvolutionAdapter(post,mBarre,hautDos,basDos,centreDos,epG,epD)
            recycler_evolution.visibility = View.VISIBLE

        }
    }

    private fun initDonnees(){
        hautDos.add("3")
        hautDos.add("7")
        hautDos.add("1")
        hautDos.add("2")
        hautDos.add("5")
        hautDos.add("0")
        hautDos.add("4")
        hautDos.add("7")
        hautDos.add("1")
        hautDos.add("9")

        basDos.add("7")
        basDos.add("1")
        basDos.add("2")
        basDos.add("5")
        basDos.add("0")
        basDos.add("4")
        basDos.add("7")
        basDos.add("1")
        basDos.add("9")
        basDos.add("3")

        centreDos.add("1")
        centreDos.add("2")
        centreDos.add("5")
        centreDos.add("0")
        centreDos.add("4")
        centreDos.add("7")
        centreDos.add("1")
        centreDos.add("9")
        centreDos.add("3")
        centreDos.add("7")

        epG.add("2")
        epG.add("5")
        epG.add("0")
        epG.add("4")
        epG.add("7")
        epG.add("1")
        epG.add("9")
        epG.add("3")
        epG.add("7")
        epG.add("1")

        epD.add("5")
        epD.add("0")
        epD.add("4")
        epD.add("7")
        epD.add("1")
        epD.add("9")
        epD.add("3")
        epD.add("7")
        epD.add("1")
        epD.add("2")

    }
}