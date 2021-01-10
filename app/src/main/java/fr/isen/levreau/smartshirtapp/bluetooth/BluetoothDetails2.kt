package fr.isen.levreau.smartshirtapp.bluetooth


import android.bluetooth.*
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.levreau.smartshirtapp.R
import kotlinx.android.synthetic.main.activity_bluetooth_details2.*
import java.util.*


class BluetoothDetails2 : AppCompatActivity() {

    private var bluetoothGatt: BluetoothGatt? = null
    private var TAG: String = "services"
    private lateinit var adapter: BluetoothDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_details2)

        cache.visibility = View.INVISIBLE
        t_shirt.visibility = View.INVISIBLE
        points.visibility = View.INVISIBLE
        atten.visibility = View.INVISIBLE

        val device: BluetoothDevice? = intent.getParcelableExtra("ble_device")
        device_name.text = device?.name
        bluetoothGatt = device?.connectGatt(this, true, gattCallback)

        disconnect_button.setOnClickListener {
            t_shirt.visibility = View.VISIBLE
            cache.visibility = View.VISIBLE
        }
    }

    //apk

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
                        //name.text = device?.name
                    }
                    bluetoothGatt?.discoverServices()
                    Log.i(TAG, "Connected to GATT")
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    runOnUiThread {
                        device_statut.text = STATE_DISCONNECTED
                    }
                    Log.i(TAG, "Disconnected from GATT")
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            runOnUiThread {
                detailsView.adapter = BluetoothDetailsAdapter(
                    gatt?.services?.map {
                        BLEService(
                            it.uuid.toString(),
                            it.characteristics
                        )
                    }?.toMutableList() ?: arrayListOf(), this@BluetoothDetails2, gatt
                )
                detailsView.layoutManager = LinearLayoutManager(this@BluetoothDetails2)
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            val value = characteristic.getStringValue(0)
            Log.e(
                "TAG",
                "onCharacteristicRead: " + value + " UUID " + characteristic.uuid.toString()
            )
            runOnUiThread {
                detailsView.adapter?.notifyDataSetChanged()
            }
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            val value = characteristic.value
            Log.e(
                "TAG",
                "onCharacteristicWrite: " + value + " UUID " + characteristic.uuid.toString()
            )
            runOnUiThread {
                detailsView.adapter?.notifyDataSetChanged()
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic
        ) {
            val hex = characteristic.value.joinToString("") { byte -> "%02x".format(byte) }
                .toUpperCase(Locale.FRANCE)

            val x = hex.subSequence(0, 4)
            val y = hex.subSequence(4, 8)
            val z = hex.subSequence(8, 12)

            val xx = test(Integer.parseInt(x.toString(), 16))
            val yy = test(Integer.parseInt(y.toString(), 16))
            val zz = test(Integer.parseInt(z.toString(), 16))

            val value = "Valeur : $hex "

            Log.e(
                "TAG",
                "onCharacteristicChanged: $hex X:$xx Y:$yy Z:$zz" + " UUID " + characteristic.uuid.toString()
            )

            runOnUiThread {
                detailsView.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun byteArrayToHexString(array: ByteArray): String {
        val result = StringBuilder(array.size * 2)
        for (byte in array) {
            val toAppend = String.format("%X", byte) // hexadecimal
            result.append(toAppend).append("-")
        }
        result.setLength(result.length - 1) // remove last '-'
        return result.toString()
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
        private const val STATE_DISCONNECTED = "Statut : Déconnecté"
        private const val STATE_CONNECTED = "Statut : Connecté"
    }
}