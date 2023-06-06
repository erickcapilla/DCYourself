package com.erickcapilla.dcyourself

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.erickcapilla.dcyourself.util.UIUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Diagnose : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnose)

        val uiModel = UIUtils()
        val editGlucose = findViewById<EditText>(R.id.editGlucose)
        val editHemoglobin = findViewById<EditText>(R.id.editHemoglobin)
        val editInsulin= findViewById<EditText>(R.id.editInsulin)
        val goBack = findViewById<Button>(R.id.go_back)
        val deviceBtn = findViewById<ImageButton>(R.id.device)

        auth = Firebase.auth
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        var email = ""
        user?.let {
            for (profile in it.providerData) {
                email = profile.email.toString()
            }
        }

        deviceBtn.setOnClickListener {
            val bluetoothManager = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

            if (bluetoothAdapter?.isEnabled == false) {
                /*val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                getResult.launch(enableBtIntent)*/
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        ActivityCompat.requestPermissions(this,
                            listOf(Manifest.permission.BLUETOOTH_CONNECT).toTypedArray(), 2)
                    }
                }
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                requestBluetooth.launch(enableBtIntent)
            }
            if (bluetoothAdapter?.isEnabled == true) {
                val devices = bluetoothAdapter.bondedDevices

                val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
                registerReceiver(receiver, filter)
            }
        }

        val next = findViewById<Button>(R.id.next)
        next.setOnClickListener {
            if(!uiModel.isEditEmpty(listOf(editGlucose, editHemoglobin, editInsulin))) {
                val glucose = editGlucose.text.toString().toFloat()
                val hemoglobin = editHemoglobin.text.toString().toFloat()
                val insulin = editInsulin.text.toString().toFloat()
                if(diagnose(glucose, hemoglobin)) {
                    val change = Intent(this, Positive::class.java)
                    startActivity(change)
                } else {
                    val change = Intent(this, Negative::class.java)
                    startActivity(change)
                }

                db.collection("data").document(email)
                    .set(hashMapOf(
                        "glucose" to glucose,
                        "hemoglobin" to hemoglobin,
                        "insulin" to insulin
                    ))

            } else {
                uiModel.showToast(applicationContext, "Ingresa todos los datos que se solicitan")
            }

        }


        goBack.setOnClickListener{
            finish()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when(intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (ContextCompat.checkSelfPermission(this@Diagnose, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            ActivityCompat.requestPermissions(this@Diagnose,
                                listOf(Manifest.permission.BLUETOOTH_CONNECT).toTypedArray(), 2)
                            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                            requestBluetooth.launch(enableBtIntent)
                        }
                    }
                    val deviceName = device?.name
                    val deviceHardwareAddress = device?.address // MAC address
                    Log.v(TAG, deviceName.toString())
                }
            }
        }
    }

    private fun diagnose(glucose: Float, hemoglobin: Float): Boolean {
        var diabetes = false

        if(glucose > 199) { diabetes = true }

        return diabetes
    }

    /*override fun onDestroy() {
        super.onDestroy()
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver)
    }*/

    private var requestBluetooth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val value = result.data?.getStringExtra("input")
            BluetoothAdapter.STATE_ON
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this@Diagnose)
            .setMessage("¿Salir de la aplicación?")
            .setCancelable(false)
            .setPositiveButton("Si") { dialog, whichButton ->
                finishAffinity() //Sale de la aplicación.
            }
            .setNegativeButton("Cancelar") { dialog, whichButton ->

            }
            .show()
    }
}