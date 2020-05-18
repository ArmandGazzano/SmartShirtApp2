package fr.isen.levreau.smartshirtapp


import android.bluetooth.*
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.gazzano.androidtoolbox.BluetoothDetailsAdapter
import kotlinx.android.synthetic.main.activity_bluetooth_details.*
import kotlinx.android.synthetic.main.activity_bluetooth_details.bas_gris
import kotlinx.android.synthetic.main.activity_bluetooth_details.bas_rouge
import kotlinx.android.synthetic.main.activity_bluetooth_details.cache
import kotlinx.android.synthetic.main.activity_bluetooth_details.danger
import kotlinx.android.synthetic.main.activity_bluetooth_details.ep_d_gris
import kotlinx.android.synthetic.main.activity_bluetooth_details.ep_d_rouge
import kotlinx.android.synthetic.main.activity_bluetooth_details.ep_g_gris
import kotlinx.android.synthetic.main.activity_bluetooth_details.ep_g_rouge
import kotlinx.android.synthetic.main.activity_bluetooth_details.haut_gris
import kotlinx.android.synthetic.main.activity_bluetooth_details.haut_rouge
import kotlinx.android.synthetic.main.activity_bluetooth_details.milieu_gris
import kotlinx.android.synthetic.main.activity_bluetooth_details.milieu_rouge
import kotlinx.android.synthetic.main.activity_bluetooth_details.tshirt
import java.util.*


class BluetoothDetails : AppCompatActivity() {

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
        setContentView(R.layout.activity_bluetooth_details)
        danger.visibility = View.INVISIBLE

        cache.visibility = View.INVISIBLE
        tshirt.visibility = View.INVISIBLE
        haut_gris.visibility = View.INVISIBLE
        haut_rouge.visibility = View.INVISIBLE
        bas_gris.visibility = View.INVISIBLE
        bas_rouge.visibility = View.INVISIBLE
        ep_d_gris.visibility = View.INVISIBLE
        ep_d_rouge.visibility = View.INVISIBLE
        ep_g_gris.visibility = View.INVISIBLE
        ep_g_rouge.visibility = View.INVISIBLE
        milieu_gris.visibility = View.INVISIBLE
        milieu_rouge.visibility = View.INVISIBLE
        danger.visibility = View.INVISIBLE

        val device: BluetoothDevice = intent.getParcelableExtra("ble_device")
        nameDevice.text = device.name
        bluetoothGatt = device.connectGatt(this, true, gattCallback)

        commencer.setOnClickListener {
            tshirt.visibility = View.VISIBLE
            cache.visibility = View.VISIBLE
            bas_gris.visibility = View.VISIBLE
            haut_gris.visibility = View.VISIBLE
            milieu_gris.visibility = View.VISIBLE
            ep_d_gris.visibility = View.VISIBLE
            ep_g_gris.visibility = View.VISIBLE
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
                , this@BluetoothDetails, gatt)
                detailsView.layoutManager = LinearLayoutManager(this@BluetoothDetails)
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
                    a1 = dec - a
                    a = dec
                }
                UUID.fromString("466c1268-f593-11e8-8eb2-f2801f1b9fd1") -> {
                    a2 = dec - b
                    b = dec
                }
                UUID.fromString("466c3256-f593-11e8-8eb2-f2801f1b9fd1") -> {
                    a3 = dec - c
                    c = dec
                }
            }
            println("$a1 , $a2 , $a3")

            if (a1 > 100 || a2 > 100 || a3 > 260){
                var t = 0
                danger.visibility = View.VISIBLE
                ep_d_rouge.visibility = View.VISIBLE
                ep_g_rouge.visibility = View.VISIBLE
                haut_rouge.visibility = View.VISIBLE
                milieu_rouge.visibility = View.VISIBLE
                bas_rouge.visibility = View.VISIBLE

                do {
                    t += 1
                    println("attention")
                } while (t < 100000)
            }
            danger.visibility = View.INVISIBLE
            ep_d_rouge.visibility = View.INVISIBLE
            ep_g_rouge.visibility = View.INVISIBLE
            haut_rouge.visibility = View.INVISIBLE
            milieu_rouge.visibility = View.INVISIBLE
            bas_rouge.visibility = View.INVISIBLE
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