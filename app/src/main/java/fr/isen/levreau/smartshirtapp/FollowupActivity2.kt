package fr.isen.levreau.smartshirtapp

import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_followup2.*


class FollowupActivity2 : AppCompatActivity() {

    lateinit var mSeries: LineGraphSeries<DataPoint>
    private var graphLastXValue = -1.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followup2)

        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                graphLastXValue += 1.0
                mSeries.appendData(DataPoint(graphLastXValue, progress.toDouble()), true, 40)
                println("<Dessin> progress : $progress")
            }
        })

        /*
        val series1 = LineGraphSeries<DataPoint>(
            arrayOf<DataPoint>(
                DataPoint(0.0, 1.0),
                DataPoint(1.0, 5.0),
                DataPoint(2.0, 3.0),
                DataPoint(3.0, 2.0),
                DataPoint(4.0, 6.0)
            )
        )
        graph.addSeries(series1)

        val series2 = BarGraphSeries(
            arrayOf<DataPoint>(
                DataPoint(0.0, 1.0),
                DataPoint(1.0, 5.0),
                DataPoint(2.0, 3.0),
                DataPoint(3.0, 2.0),
                DataPoint(4.0, 6.0)
            )
        )
        graph.addSeries(series2)
         */

        // Exemple 3 : avec la seekbar
        graph.title = "Accelerometre"
        graph.titleTextSize = 40f
        graph.titleColor = Color.BLUE

        graph.addSeries(mSeries)
        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.setMinY(0.0)
        graph.viewport.setMaxY(100.0)
        graph.viewport.setMinX(0.0)
        graph.viewport.setMaxX(40.0)

        // Légende
        mSeries.title = "SeekBar"
        graph.legendRenderer.isVisible = true
        graph.legendRenderer.align = LegendRenderer.LegendAlign.TOP

        /*
        // Zooming and scrolling
        graph.viewport.isScalable = true // enables horizontal zooming and scrolling
        graph.viewport.setScalableY(true) // enables vertical zooming and scrolling
        graph.viewport.isScrollable = true // enables horizontal scrolling
        graph.viewport.setScrollableY(true) // enables vertical scrolling
         */
    }
}