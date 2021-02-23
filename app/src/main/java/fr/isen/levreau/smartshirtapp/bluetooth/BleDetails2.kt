package fr.isen.levreau.smartshirtapp.bluetooth

import android.bluetooth.*
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import fr.isen.levreau.smartshirtapp.Crypto
import fr.isen.levreau.smartshirtapp.R
import fr.isen.levreau.smartshirtapp.bdd.DatabaseValue
import kotlinx.android.synthetic.main.activity_ble_details2.*
import java.text.SimpleDateFormat
import java.util.*


class BleDetails2 : AppCompatActivity() {

    private var bluetoothGatt: BluetoothGatt? = null
    private var TAG = "MyActivity"
    var notifier = false
    lateinit var sharedPreferences: SharedPreferences

    lateinit var userID: String

    lateinit var xSeries: LineGraphSeries<DataPoint>
    lateinit var ySeries: LineGraphSeries<DataPoint>
    lateinit var zSeries: LineGraphSeries<DataPoint>
    private var graphLastXValue = -1.0

    lateinit var x: CharSequence
    lateinit var x2: CharSequence
    lateinit var x3: CharSequence
    lateinit var y: CharSequence
    lateinit var y2: CharSequence
    lateinit var y3: CharSequence
    lateinit var z: CharSequence
    lateinit var z2: CharSequence
    lateinit var z3: CharSequence
    var xx = 0.0
    var xx2 = 0.0
    var xx3 = 0.0
    var yy = 0.0
    var yy2 = 0.0
    var yy3 = 0.0
    var zz = 0.0
    var zz2 = 0.0
    var zz3 = 0.0

    var i_calibr = 0
    var x_calibr = 0.0
    var y_calibr = 0.0
    var z_calibr = 0.0
    var calibration: Boolean = false

    var xcountplus = 0
    var ycountplus = 0
    var zcountplus = 0
    var xcountmoins = 0
    var ycountmoins = 0
    var zcountmoins = 0
    var normalcount = 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_details2)

        sharedPreferences = getSharedPreferences("user_id", Context.MODE_PRIVATE)
        userID = sharedPreferences.getString("mail", "").toString()

        val device: BluetoothDevice? = intent.getParcelableExtra("ble_device")
        device_name.text = device?.name ?: "Unnamed"
        bluetoothGatt = device?.connectGatt(this, false, gattCallback)

        calibration_button.setOnClickListener {
            calibration = true
        }

        notifiy_button.setOnClickListener {
            if (!notifier) {
                notifier = true
                if (bluetoothGatt != null) {
                    setCharacteristicNotificationInternal(
                        bluetoothGatt, bluetoothGatt?.services?.get(
                            2
                        )?.characteristics?.get(1), true
                    )
                }
            } else {
                notifier = false
                if (bluetoothGatt != null) {
                    setCharacteristicNotificationInternal(
                        bluetoothGatt, bluetoothGatt?.services?.get(
                            2
                        )?.characteristics?.get(1), false
                    )
                }
            }
        }

        calibration_button.setOnClickListener {
            calibration = true
        }

        xSeries = LineGraphSeries()
        ySeries = LineGraphSeries()
        zSeries = LineGraphSeries()

        graph.addSeries(xSeries)
        graph.addSeries(ySeries)
        graph.addSeries(zSeries)
        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.setMinY(-2000.0)
        graph.viewport.setMaxY(2000.0)
        graph.viewport.setMinX(0.0)
        graph.viewport.setMaxX(40.0)

        // Légende
        xSeries.title = "X"
        ySeries.title = "Y"
        zSeries.title = "Z"
        xSeries.color = Color.BLUE
        ySeries.color = Color.GREEN
        zSeries.color = Color.RED
        graph.legendRenderer.isVisible = true
        graph.legendRenderer.align = LegendRenderer.LegendAlign.TOP

        graph.viewport.isScrollable = true // enables horizontal scrolling
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(
            gatt: BluetoothGatt,
            status: Int,
            newState: Int
        ) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    runOnUiThread {
                        device_statut.text = STATE_CONNECTED
                    }

                    bluetoothGatt?.discoverServices()
                    Log.i(TAG, "Connected to GATT server.")
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    runOnUiThread {
                        device_statut.text = STATE_DISCONNECTED
                    }
                    Log.i(TAG, "Disconnected from GATT server.")
                }
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            val value = characteristic.getStringValue(0)

            runOnUiThread {

            }

            Log.e(
                "TAG",
                "onCharacteristicRead: " + value + " UUID " + characteristic.uuid.toString()
            )

        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            val value = String(characteristic.value)
            runOnUiThread {

            }
            Log.e(
                "TAG",
                "onCharacteristicWrite: " + value + " UUID " + characteristic.uuid.toString()
            )

        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic
        ) {
            val hex =
                characteristic.value.joinToString("") { byte -> "%02x".format(byte) }.toUpperCase(
                    Locale.FRANCE
                )

            x = hex.subSequence(0, 4)
            y = hex.subSequence(4, 8)
            z = hex.subSequence(8, 12)

            xx = test(Integer.parseInt(x.toString(), 16))
            yy = test(Integer.parseInt(y.toString(), 16))
            zz = test(Integer.parseInt(z.toString(), 16))

            x2 = hex.subSequence(12, 16)
            y2 = hex.subSequence(16, 20)
            z2 = hex.subSequence(20, 24)

            xx2 = test(Integer.parseInt(x2.toString(), 16))
            yy2 = test(Integer.parseInt(y2.toString(), 16))
            zz2 = test(Integer.parseInt(z2.toString(), 16))

            x3 = hex.subSequence(24, 28)
            y3 = hex.subSequence(28, 32)
            z3 = hex.subSequence(32, 36)

            xx3 = test(Integer.parseInt(x3.toString(), 16))
            yy3 = test(Integer.parseInt(y3.toString(), 16))
            zz3 = test(Integer.parseInt(z3.toString(), 16))



            Log.e(
                "TAG",
                "onCharacteristicChanged: X:$xx Y:$yy Z:$zz X2:$xx2 Y2:$yy2 Z2:$zz2 X3:$xx3 Y3:$yy3 Z:$zz3"
            )

            //BDD
            val crypto = Crypto()
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("data")
            val test = /*crypto.cipherCoordinates(*/
                DatabaseValue(
                    xx.toString(),
                    yy.toString(),
                    zz.toString(),
                    xx2.toString(),
                    yy2.toString(),
                    zz2.toString(),
                    xx3.toString(),
                    yy3.toString(),
                    zz3.toString()
                )
            //     )

            val yourmilliseconds = Calendar.getInstance().timeInMillis
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS", Locale.US)
            val calendar = GregorianCalendar(TimeZone.getTimeZone("US/Central"))
            calendar.timeInMillis = yourmilliseconds

            myRef.child(userID).child(sdf.format(calendar.time).toString()).setValue(test)


            runOnUiThread {
                if (calibration) {
                    calibr(xx, yy, zz)
                    calibr(xx2,yy2,zz2)
                    calibr(xx3,yy3,zz3)
                } else {
                    graphLastXValue += 1.0
                    xSeries.appendData(DataPoint(graphLastXValue, xx), true, 40)
                    ySeries.appendData(DataPoint(graphLastXValue, yy), true, 40)
                    zSeries.appendData(DataPoint(graphLastXValue, zz), true, 40)
                    //mouvementEpauleDroite(xx, yy, zz)
                    //mouvementEpauleGauche(xx3, yy3, zz3)
                    mouvementDosHaut(xx2, yy2, zz2)
                }
            }
        }
    }

    private fun setCharacteristicNotificationInternal(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        enabled: Boolean
    ) {
        gatt?.setCharacteristicNotification(characteristic, enabled)

        if (characteristic != null) {
            if (characteristic.descriptors.size > 0) {

                val descriptors = characteristic.descriptors
                for (descriptor in descriptors) {

                    if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) {
                        descriptor.value =
                            if (enabled) BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
                    } else if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_INDICATE != 0) {
                        descriptor.value =
                            if (enabled) BluetoothGattDescriptor.ENABLE_INDICATION_VALUE else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
                    }
                    gatt?.writeDescriptor(descriptor)
                }
            }
        }
    }

    private fun test(num: Int): Double {
        var x = num
        if (x > 32768) {
            x -= 65536
        }
        return x * 0.122
    }

    fun calibr(x: Double, y: Double, z: Double) {
        if (i_calibr == 0) info.text = "Calibrage en cours..."
        if (i_calibr < 5) {
            x_calibr += x
            y_calibr += y
            z_calibr += z
            i_calibr++
        } else {
            x_calibr /= i_calibr
            y_calibr /= i_calibr
            z_calibr /= i_calibr
            info.text = "Calibrage : x: $x_calibr,y: $y_calibr,z: $z_calibr"
            calibration = false
            i_calibr = 0
        }
    }

    fun mouvementEpauleDroite(x: Double, y: Double, z: Double) {
        if (y_calibr + 50 <= y || y_calibr - 50 >= y || z_calibr + 50 <= z || z_calibr - 50 >= z) {
            if (y_calibr + 150 <= y) {
                ycountplus++
                ycountmoins = 0
            }
            if (y_calibr - 300 >= y) {
                ycountmoins++
                ycountplus = 0
            }
            if (z_calibr + 300 <= z) {
                zcountplus++
                zcountmoins = 0
            }
            if (z_calibr - 300 >= z) {
                zcountmoins++
                zcountplus = 0
            }
        } else {
            normalcount++
            ycountplus = 0
            ycountmoins = 0
            zcountplus = 0
            zcountmoins = 0
        }
        if (ycountplus >= 7) info.text = "Épaule Droite trop haute"
        if (ycountmoins >= 7) info.text = "Épaule Droite trop basse"
        if (zcountplus >= 7) info.text = "Épaule Droite trop en avant"
        if (zcountmoins >= 7) info.text = "Épaule Droite trop en arrière"
        if (normalcount >= 7) {
            info.text = "Épaule Droite OK"
            normalcount = 0
        }
    }

    fun mouvementEpauleGauche(x: Double, y: Double, z: Double) {
        if (y_calibr + 50 <= y || y_calibr - 50 >= y || z_calibr + 50 <= z || z_calibr - 50 >= z) {
            if (y_calibr + 300 <= y) {
                ycountplus++
                ycountmoins = 0
            }
            if (y_calibr - 150 >= y) {
                ycountmoins++
                ycountplus = 0
            }
            if (z_calibr + 300 <= z) {
                zcountplus++
                zcountmoins = 0
            }
            if (z_calibr - 300 >= z) {
                zcountmoins++
                zcountplus = 0
            }
        } else {
            normalcount++
            ycountplus = 0
            ycountmoins = 0
            zcountplus = 0
            zcountmoins = 0
        }
        if (ycountmoins >= 7) info.text = "Épaule Gauche trop haute"
        if (ycountplus >= 7) info.text = "Épaule Gauche trop basse"
        if (zcountplus >= 7) info.text = "Épaule Gauche trop en avant"
        if (zcountmoins >= 7) info.text = "Épaule Gauche trop en arrière"
        if (normalcount >= 7) {
            info.text = "Épaule Gauche OK"
            normalcount = 0
        }
    }

    fun mouvementDosHaut(x: Double, y: Double, z: Double) {
        if (z_calibr + 50 <= z || z_calibr - 50 >= z) {
            if (z_calibr + 300 <= z) {
                info.text = "Haut du dos trop en avant"
            } else if (z_calibr - 300 >= z) {
                info.text = "Haut du dos trop en arrière"
            } else {
                info.text = "Haut du dos OK"
            }
        }
    }

    override fun onStop() {
        super.onStop()
        bluetoothGatt?.close()
    }

    companion object {
        private const val STATE_CONNECTED = "Connected"
        private const val STATE_DISCONNECTED = "Disconnected"
    }
}