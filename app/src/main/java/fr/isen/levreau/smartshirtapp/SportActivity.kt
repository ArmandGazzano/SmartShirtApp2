package fr.isen.levreau.smartshirtapp

import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sport.*
import java.io.File
import java.io.InputStream

class SportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sport)

        readFromFile()
    }

    fun readFromFile(){

        var text = ""

        val path = this.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val file = File(path, "data.txt")


        val inputStream: InputStream = file.inputStream()
        val lineList = mutableListOf<String>()

        inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it)} }
        lineList.forEach{text += it + "\n"}
        excel.text = text
    }
}