package fr.isen.levreau.smartshirtapp

import android.app.Activity
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_bluetooth.*
import kotlinx.android.synthetic.main.activity_bluetooth_details.*
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private var mScanning: Boolean = false
    private var bluetoothGatt: BluetoothGatt? = null
    private var TAG: String = "services"

    //var bluetoothGatt: BluetoothGatt? = null

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val isBLEEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        test_button.setOnClickListener {
            when {
                isBLEEnabled -> {
                    //init scan
                    initBLEScan()
                    initScan()
                }
                bluetoothAdapter != null -> {
                    //ask for permission
                    val enableBTIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableBTIntent, BluetoothActivity.REQUEST_ENABLE_BT)
                }
                else -> {
                    //device is not compatible with your device
                    bleTextFailed.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initScan() {
        handler = Handler()
        scanLeDevice(true)
    }

    private fun scanLeDevice(enable: Boolean) {
        bluetoothAdapter?.bluetoothLeScanner?.apply {
            if (enable) {
                Log.w("BLE", "Scanning for devices")
                handler.postDelayed({
                    mScanning = false
                    stopScan(leScanCallback)
                }, SCAN_PERIOD)
                mScanning = true
                startScan(leScanCallback)
            } else {
                mScanning = false
                stopScan(leScanCallback)
            }
        }
    }

    private val leScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.w("BLE", "${result.device}")
            if (result.device.address == "EC:94:D4:51:4A:41") {
                bluetoothGatt = result.device.connectGatt(applicationContext, true, gattCallback)
                scanLeDevice(false)
            }
        }
    }

    private fun initBLEScan() {
        handler = Handler()

        scanLeDevice(true)
    }

    private fun onDeviceClicked(device: BluetoothDevice) {
        val intent = Intent(this, BluetoothDetails::class.java)
        intent.putExtra("ble_device", device)
        Toast.makeText(this, device.address, Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                if (isBLEEnabled) {
                    Toast.makeText(this, "Bluetooth has been enabled", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Bluetooth has been disabled", Toast.LENGTH_SHORT).show()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Bluetooth enabling has been canceled", Toast.LENGTH_SHORT)
                    .show()
            }
        }
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
                        //connectionState.text = STATE_CONNECTED
                        //name.text = device?.name
                    }
                    bluetoothGatt?.discoverServices()
                    Log.i(TAG, "Connected to GATT")
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    runOnUiThread {
                        //connectionState.text = STATE_DISCONNECTED
                    }
                    Log.i(TAG, "Disconnected from GATT")
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            if (gatt != null) {
                setCharacteristicNotificationInternal(
                    gatt,
                    gatt.services[2].characteristics[1],
                    true
                )
                Log.e(
                    "TAG",
                    "Services : actif"
                )
            }
            runOnUiThread {

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

            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic
        ) {
            val value = byteArrayToHexString(characteristic.value)
            Log.e(
                "TAG",
                "onCharacteristicChanged: " + value + " UUID " + characteristic.uuid.toString()
            )
            runOnUiThread {
                if (gatt != null) {
                    Toast.makeText(applicationContext,gatt.services[2].characteristics[1].value.toString() ,Toast.LENGTH_SHORT
                    ).show()
                    // + " / y : " + byteArrayToHexString(gatt.services[2].characteristics[2].value) +
                                //" / z : " + byteArrayToHexString(gatt.services[2].characteristics[3].value),
                }
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

    private fun setCharacteristicNotificationInternal(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        enabled: Boolean
    ) {
        gatt.setCharacteristicNotification(characteristic, enabled)

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
                gatt.writeDescriptor(descriptor)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        scanLeDevice(false)
        bluetoothGatt?.close()
    }

    companion object {
        private const val SCAN_PERIOD: Long = 60000
        private const val REQUEST_ENABLE_BT = 44
        private const val STATE_DISCONNECTED = "Statut : Déconnecté"
    }
}
