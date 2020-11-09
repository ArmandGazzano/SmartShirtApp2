package fr.isen.levreau.smartshirtapp


import android.bluetooth.*
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.gazzano.androidtoolbox.BluetoothDetailsAdapter
import kotlinx.android.synthetic.main.activity_bluetooth_details2.*
import java.util.*
import kotlin.math.abs


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

        val device: BluetoothDevice = intent.getParcelableExtra("ble_device")
        nameDevice.text = device.name
        bluetoothGatt = device.connectGatt(this, true, gattCallback)

        commencer.setOnClickListener {
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
                        connectionState.text = STATE_CONNECTED
                        //name.text = device?.name
                    }
                    bluetoothGatt?.discoverServices()
                    Log.i(TAG, "Connected to GATT")
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    runOnUiThread {
                        connectionState.text = STATE_DISCONNECTED
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
                    }?.toMutableList() ?: arrayListOf()
                    , this@BluetoothDetails2, gatt)
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
            val hex = characteristic.value.joinToString("") { byte -> "%02x".format(byte)}.toUpperCase(Locale.FRANCE)

            //val x = hex[0] + hex[1] + hex[2] + hex[3]

            //val dec = Integer.parseInt(hex,16)
            //val nb_m = 0.1 * dec;
            val value = "Valeur : $hex "

            Log.e(
                "TAG",
                "onCharacteristicChanged: " + hex + " UUID " + characteristic.uuid.toString()
            )

            runOnUiThread {
                detailsView.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun byteArrayToHexString(array: ByteArray): String {
        val result = StringBuilder(array.size * 2)
        for ( byte in array ) {
            val toAppend = String.format("%X", byte) // hexadecimal
            result.append(toAppend).append("-")
        }
        result.setLength(result.length - 1) // remove last '-'
        return result.toString()
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