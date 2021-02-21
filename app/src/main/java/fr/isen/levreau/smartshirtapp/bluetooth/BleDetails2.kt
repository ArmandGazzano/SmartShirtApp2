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
import fr.isen.levreau.smartshirtapp.bdd.DatabaseValue
import fr.isen.levreau.smartshirtapp.R
import kotlinx.android.synthetic.main.activity_ble_details2.*
import kotlinx.android.synthetic.main.activity_ble_details2.graph
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_details2)

        sharedPreferences = getSharedPreferences("user_id", Context.MODE_PRIVATE)
        userID = sharedPreferences.getString("mail", "").toString()

        val device: BluetoothDevice? = intent.getParcelableExtra("ble_device")
        device_name.text = device?.name ?: "Unnamed"
        bluetoothGatt = device?.connectGatt(this, false, gattCallback)

        disconnect_button.setOnClickListener {
            BluetoothProfile.STATE_DISCONNECTED;
            device_statut.text = STATE_DISCONNECTED
            bluetoothGatt?.close()
            Log.i(TAG, "Disconnected from GATT server.")

        }

        notifiy_button.setOnClickListener {
            if (!notifier){
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

            val x = hex.subSequence(0, 4)
            val y = hex.subSequence(4, 8)
            val z = hex.subSequence(8, 12)

            val xx = test(Integer.parseInt(x.toString(), 16))
            val yy = test(Integer.parseInt(y.toString(), 16))
            val zz = test(Integer.parseInt(z.toString(), 16))

            //BDD
            val crypto = Crypto()
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("data")
            val test = crypto.cipherCoordinates(DatabaseValue(xx.toString(), yy.toString(), zz.toString()))

            val yourmilliseconds = Calendar.getInstance().timeInMillis
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS", Locale.US)
            val calendar = GregorianCalendar(TimeZone.getTimeZone("US/Central"))
            calendar.timeInMillis = yourmilliseconds

            myRef.child(userID).child(sdf.format(calendar.time).toString()).setValue(test)


            runOnUiThread {
                graphLastXValue += 1.0
                xSeries.appendData(DataPoint(graphLastXValue, xx), true, 40)
                ySeries.appendData(DataPoint(graphLastXValue, yy), true, 40)
                zSeries.appendData(DataPoint(graphLastXValue, zz), true, 40)
            }

            Log.e(
                "TAG",
                "onCharacteristicChanged: $hex X:$xx Y:$yy Z:$zz" + " UUID " + characteristic.uuid.toString()
            )
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

    override fun onStop() {
        super.onStop()
        bluetoothGatt?.close()
    }

    companion object {
        private const val STATE_CONNECTED = "Connected"
        private const val STATE_DISCONNECTED = "Disconnected"
    }
}