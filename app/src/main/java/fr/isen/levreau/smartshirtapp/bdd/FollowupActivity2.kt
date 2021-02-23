package fr.isen.levreau.smartshirtapp.bdd
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import fr.isen.levreau.smartshirtapp.Crypto
import fr.isen.levreau.smartshirtapp.R
import kotlinx.android.synthetic.main.activity_ble_details2.*
import kotlinx.android.synthetic.main.activity_followup2.*
import java.text.SimpleDateFormat
import java.util.*

class FollowupActivity2 : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var userID: String

    lateinit var xSeries: LineGraphSeries<DataPoint>
    lateinit var ySeries: LineGraphSeries<DataPoint>
    lateinit var zSeries: LineGraphSeries<DataPoint>
    lateinit var xSeries2: LineGraphSeries<DataPoint>
    lateinit var ySeries2: LineGraphSeries<DataPoint>
    lateinit var zSeries2: LineGraphSeries<DataPoint>
    lateinit var xSeries3: LineGraphSeries<DataPoint>
    lateinit var ySeries3: LineGraphSeries<DataPoint>
    lateinit var zSeries3: LineGraphSeries<DataPoint>
    private var graphLastXValue = -1.0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followup2)
        sharedPreferences = getSharedPreferences("user_id", Context.MODE_PRIVATE)
        userID = sharedPreferences.getString("mail", "").toString()

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("data/$userID")
        val crypto = Crypto()
        var cal = Calendar.getInstance()

        xSeries = LineGraphSeries()
        ySeries = LineGraphSeries()
        zSeries = LineGraphSeries()

        graphfu1.addSeries(xSeries)
        graphfu1.addSeries(ySeries)
        graphfu1.addSeries(zSeries)
        graphfu1.viewport.isYAxisBoundsManual = true
        graphfu1.viewport.isXAxisBoundsManual = true
        graphfu1.viewport.setMinY(-2000.0)
        graphfu1.viewport.setMaxY(2000.0)
        graphfu1.viewport.setMinX(0.0)
        graphfu1.viewport.setMaxX(40.0)

        xSeries2 = LineGraphSeries()
        ySeries2 = LineGraphSeries()
        zSeries2 = LineGraphSeries()

        graphfu2.addSeries(xSeries2)
        graphfu2.addSeries(ySeries2)
        graphfu2.addSeries(zSeries2)
        graphfu2.viewport.isYAxisBoundsManual = true
        graphfu2.viewport.isXAxisBoundsManual = true
        graphfu2.viewport.setMinY(-2000.0)
        graphfu2.viewport.setMaxY(2000.0)
        graphfu2.viewport.setMinX(0.0)
        graphfu2.viewport.setMaxX(40.0)

        xSeries3 = LineGraphSeries()
        ySeries3 = LineGraphSeries()
        zSeries3 = LineGraphSeries()

        graphfu3.addSeries(xSeries3)
        graphfu3.addSeries(ySeries3)
        graphfu3.addSeries(zSeries3)
        graphfu3.viewport.isYAxisBoundsManual = true
        graphfu3.viewport.isXAxisBoundsManual = true
        graphfu3.viewport.setMinY(-2000.0)
        graphfu3.viewport.setMaxY(2000.0)
        graphfu3.viewport.setMinX(0.0)
        graphfu3.viewport.setMaxX(40.0)

        // Légende
        xSeries.title = "X"
        ySeries.title = "Y"
        zSeries.title = "Z"
        xSeries.color = Color.BLUE
        ySeries.color = Color.GREEN
        zSeries.color = Color.RED
        graphfu1.legendRenderer.isVisible = true
        graphfu1.legendRenderer.align = LegendRenderer.LegendAlign.TOP

        xSeries2.title = "X"
        ySeries2.title = "Y"
        zSeries2.title = "Z"
        xSeries2.color = Color.BLUE
        ySeries2.color = Color.GREEN
        zSeries2.color = Color.RED
        graphfu2.legendRenderer.isVisible = true
        graphfu2.legendRenderer.align = LegendRenderer.LegendAlign.TOP

        xSeries3.title = "X"
        ySeries3.title = "Y"
        zSeries3.title = "Z"
        xSeries3.color = Color.BLUE
        ySeries3.color = Color.GREEN
        zSeries3.color = Color.RED
        graphfu3.legendRenderer.isVisible = true
        graphfu3.legendRenderer.align = LegendRenderer.LegendAlign.TOP

        graphfu1.viewport.isScrollable = true // enables horizontal scrolling

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "dd/MM/yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                datepick.text = sdf.format(cal.getTime())
            }
        }

        datepick.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@FollowupActivity2,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

        firebase_button.setOnClickListener {
            val myFormat = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            val datepicked = sdf.format(cal.time)

            Toast.makeText(this, datepicked, Toast.LENGTH_SHORT)
                .show()

            myRef.get().addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")
                Log.i("count", "Got value ${it.childrenCount}")

                val valueString = it.value.toString()
                /*for (ds in it.children) {
                    if (ds.key!!.contains(datepicked)){
                        Log.i("coord", ds.child("x1").value.toString()) //value of x1
                        Log.i("coord", ds.child("y1").value.toString()) //value of y1
                        Log.i("coord", ds.child("z1").value.toString()) //value of z1

                        val layoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT)
                        layoutParams.column = 1
                        val tvDate = TextView(this)
                        val tvX = TextView(this)
                        val tvY = TextView(this)
                        val tvZ = TextView(this)
                        val newRow = TableRow(this)
                        tvDate.text = datepicked
                        tvX.text = ds.child("x1").value.toString()
                        tvY.text = ds.child("y1").value.toString()
                        tvZ.text = ds.child("z1").value.toString()
                        newRow.addView(tvDate, layoutParams)
                        newRow.addView(tvX, layoutParams)
                        newRow.addView(tvY, layoutParams)
                        newRow.addView(tvZ, layoutParams)
                        tl!!.addView(newRow)
                        /*tl!!.removeAllViews()
                        newRow.addView(tvDate, layoutParams)
                        /*newRow.addView(tvX, layoutParams)
                        newRow.addView(tvY, layoutParams)
                        newRow.addView(tvZ, layoutParams)*/
                        tl.addView(newRow)*/
                    }
                }*/

                for (ds in it.children) {
                    if (ds.key!!.contains(datepicked)){
                        graphLastXValue += 1.0
                        xSeries.appendData(DataPoint(graphLastXValue, ds.child("x1").value.toString().toDouble()), true, 40)
                        ySeries.appendData(DataPoint(graphLastXValue, ds.child("y1").value.toString().toDouble()), true, 40)
                        zSeries.appendData(DataPoint(graphLastXValue, ds.child("z1").value.toString().toDouble()), true, 40)
                        xSeries2.appendData(DataPoint(graphLastXValue, ds.child("x2").value.toString().toDouble()), true, 40)
                        ySeries2.appendData(DataPoint(graphLastXValue, ds.child("y2").value.toString().toDouble()), true, 40)
                        zSeries2.appendData(DataPoint(graphLastXValue, ds.child("z2").value.toString().toDouble()), true, 40)
                        xSeries3.appendData(DataPoint(graphLastXValue, ds.child("x3").value.toString().toDouble()), true, 40)
                        ySeries3.appendData(DataPoint(graphLastXValue, ds.child("y3").value.toString().toDouble()), true, 40)
                        zSeries3.appendData(DataPoint(graphLastXValue, ds.child("z3").value.toString().toDouble()), true, 40)
                    }
                }




            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        }
    }
}