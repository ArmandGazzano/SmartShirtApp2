package fr.isen.levreau.smartshirtapp

import android.os.Bundle
import android.os.Environment
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

        readFromFile()
    }

    private fun readFromFile(){

        var text = ""

        val path = this.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val file = File(path, "data.txt")


        val inputStream: InputStream = file.inputStream()
        val lineList = mutableListOf<String>()

        inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it)} }
        lineList.forEach{lineToTab(it)}
        //excel.text = data.toString()
    }

    private fun lineToTab(string: String){
        var array = arrayOf<Int>()
        val s: Scanner = Scanner(string).useDelimiter("\\s*:\\s*")

        /*var a = s.nextInt()
        var b = s.nextInt()
        var c = s.nextInt()*/

        for (j in 0..2) {
            array += s.nextInt()
        }

        println(array)
        //println("$a / $b / $c")
        data += array
    }
}