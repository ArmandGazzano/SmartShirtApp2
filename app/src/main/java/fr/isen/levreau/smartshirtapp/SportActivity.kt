package fr.isen.levreau.smartshirtapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sport.*
import java.io.File
import java.io.InputStream
import java.util.*
import kotlin.math.abs
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


class SportActivity : AppCompatActivity() {

    var a = 0
    var b = 0
    var c = 0
    var count = 0
    val lineList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sport)

        val dialog = AlertDialog.Builder(this)

        dialog.setTitle("Connectez-vous au Smart-Shirt")
        dialog.setNegativeButton("Annuler", DialogInterface.OnClickListener { dialog, which ->  })
        dialog.setPositiveButton("Valider", DialogInterface.OnClickListener {
                _, _ ->
            intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        })
        dialog.show()

        disconnect_button.setOnClickListener {
            readFromFile()
            main()
        }
    }

    private fun readFromFile(){

        val path = this.applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val file = File(path, "data.txt")

        val inputStream: InputStream = file.inputStream()

        inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it)} }
    }

    private fun lineToTab(string: String){
        val s: Scanner = Scanner(string).useDelimiter("\\s*:\\s*")

        var x = s.nextInt()
        var y = s.nextInt()
        var z = s.nextInt()

        if (a != 0) {
            if (abs(a) - abs(x) >= 100 || abs(b) - abs(y) >= 100 || abs(c) - abs(z) >= 100) {
                bas_rouge.visibility = View.VISIBLE
                milieu_rouge.visibility = View.VISIBLE
                haut_rouge.visibility = View.VISIBLE
                ep_d_rouge.visibility = View.VISIBLE
                ep_g_rouge.visibility = View.VISIBLE
                atten.visibility = View.VISIBLE

                Toast.makeText(this, "ATTENTION MOUVEMENT DANGEREUX", Toast.LENGTH_SHORT).show()
                count += 1
            }
        }
        /*
        bas_rouge.visibility = View.INVISIBLE
        milieu_rouge.visibility = View.INVISIBLE
        haut_rouge.visibility = View.INVISIBLE
        ep_d_rouge.visibility = View.INVISIBLE
        ep_g_rouge.visibility = View.INVISIBLE
        danger.visibility = View.INVISIBLE
        */

        a = x
        b = y
        c = z
        excel.text = count.toString()
    }

    fun foo(): Flow<Int> = flow { // flow builder
        var i = 0
        lineList.forEach {
            println(lineList[i])
            emit(i) // emit next value
            i++
        }
    }

    fun main() = runBlocking<Unit> {
        var k = 0
        // Launch a concurrent coroutine to check if the main thread is blocked
        launch {
            lineList.forEach {
                println("I'm not blocked $k")
                //delay(100)
                k++
            }
        }
        // Collect the flow
        foo().collect { value -> println(value) }
    }
}