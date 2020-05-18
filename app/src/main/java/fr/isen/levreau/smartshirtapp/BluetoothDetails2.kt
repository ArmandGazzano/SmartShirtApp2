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
    var a = 0
    var b = 0
    var c = 0
    var a1 = 0
    var a2 = 0
    var a3 = 0
    var count = 0

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
            //val value = "Valeur : ${String(it)} Hex : 0x$hex"
            val dec = Integer.parseInt(hex,16)
            //val nb_m = 0.1 * dec;
            val value = "Valeur : $dec "
            Log.e(
                "TAG",
                "onCharacteristicChanged: " + value + " UUID " + characteristic.uuid.toString()
            )

            count += 1
            when (characteristic.uuid) {
                UUID.fromString("466c9abc-f593-11e8-8eb2-f2801f1b9fd1") -> {
                    a1 = abs(dec) - abs(a)
                    a = dec
                }
                UUID.fromString("466c1268-f593-11e8-8eb2-f2801f1b9fd1") -> {
                    a2 = abs(dec) - abs(b)
                    b = dec
                }
                UUID.fromString("466c3256-f593-11e8-8eb2-f2801f1b9fd1") -> {
                    a3 = abs(dec) - abs(c)
                    c = dec
                }
            }
            println("$a1 , $a2 , $a3")

            if (a1 > 15 || a2 > 15 || a3 > 15){
                var t = 0
                //atten.visibility = View.VISIBLE
                //points.visibility = View.VISIBLE

                do {
                    t += 1
                    println("attention")
                } while (t < 100000)
            }
            //atten.visibility = View.INVISIBLE
            points.visibility = View.INVISIBLE
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