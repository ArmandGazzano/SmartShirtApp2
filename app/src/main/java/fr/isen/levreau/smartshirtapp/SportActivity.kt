package fr.isen.levreau.smartshirtapp

import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sport.*
import java.io.File
import java.io.InputStream
import java.util.*

class SportActivity : AppCompatActivity() {

    var data = arrayOf<Array<Int>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sport)

        bas_rouge.visibility = View.INVISIBLE
        milieu_rouge.visibility = View.INVISIBLE
        haut_rouge.visibility = View.INVISIBLE
        ep_d_rouge.visibility = View.INVISIBLE
        ep_g_rouge.visibility = View.INVISIBLE
        danger.visibility = View.INVISIBLE

        start.setOnClickListener {
            readFromFile()
        }
    }

    private fun readFromFile(){

        val path = this.applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val file = File(path, "data.txt")


        val inputStream: InputStream = file.inputStream()
        val lineList = mutableListOf<String>()

        inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it)} }
        lineList.forEach{lineToTab(it)}
    }

    private fun lineToTab(string: String){
        val s: Scanner = Scanner(string).useDelimiter("\\s*:\\s*")

        var x = s.nextInt()
        var y = s.nextInt()
        var z = s.nextInt()

        /*
        for (j in 0..2) {
            println(s.nextInt())
            //array += s.nextInt()

        }
         */

        //println(array[0])
        //println("$a / $b / $c")
    }
}