package com.erickcapilla.dcyourself

import android.Manifest
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
import com.erickcapilla.dcyourself.util.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID


class Diagnose : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnose)

        val uiModel = Utils()
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
            val bluetoothManager = this.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
            val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

            if (bluetoothAdapter?.isEnabled == false) {
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
                Log.v(TAG, "Hola")
                val devices = bluetoothAdapter.bondedDevices
                Log.v(TAG, devices.toString())
                //connectToDevice(bluetoothAdapter.getRemoteDevice("60:E8:5B:B0:EE:2F"))
                // Discover devices
                bluetoothAdapter.startDiscovery()

                Log.v(TAG, bluetoothAdapter.isDiscovering.toString())

                // Connect to a device
                while (bluetoothAdapter.isDiscovering) {
                    val device = bluetoothAdapter.getRemoteDevice("60:E8:5B:B0:EE:2F")
                    Log.v(TAG, "Hola")
                    Log.v(TAG, device.toString())
                    if (device != null) {
                        bluetoothAdapter.cancelDiscovery()
                        connectToDevice(device)
                        break
                    }
                }
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

                val docRefUser = db.collection("data").document(email)
                val medicinesData = docRefUser.collection("userData")

                val useData = hashMapOf(
                    "glucose" to glucose,
                    "hemoglobin" to hemoglobin,
                    "insulin" to insulin
                )

                medicinesData.add(useData).addOnSuccessListener { doc ->
                    diagnose(glucose, hemoglobin)
                }
            } else {
                uiModel.showToast(applicationContext, "Ingresa todos los datos que se solicitan")
            }

        }

        goBack.setOnClickListener{
            finish()
        }
    }

    private fun connectToDevice(device: BluetoothDevice) {
        // Create a BluetoothSocket
        val bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.randomUUID())

        // Connect to the device
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            bluetoothSocket.connect()
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        //bluetoothSocket.connect()


        // Read data from the device
        /*val inputStream = bluetoothSocket.inputStream
        val outputStream = bluetoothSocket.outputStream

        val reader = BufferedReader(InputStreamReader(inputStream))

        while (true) {
            val line = reader.readLine()
            if (line != null) {
                Log.v(TAG, "Recibiendo $line")
            }
        }*/
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
                    Log.v(TAG, "HOla")
                }
            }
        }
    }


    private fun diagnose(glucose: Float, hemoglobin: Float) {
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        var email = ""
        user?.let {
            for (profile in it.providerData) {
                email = profile.email.toString()
            }
        }

        if(glucose > 199) {
            db.collection("diagnosis").document(email)
                .update("diabetic", "yes")
            val change = Intent(this, Positive::class.java)
            startActivity(change)
        }

        if(glucose > 129 && glucose < 200) {
            db.collection("diagnosis").document(email)
                .update("diabetic", "maybe")
            if(glucose < 130 && hemoglobin < 5) {
                db.collection("diagnosis").document(email)
                    .update("diabetic", "no")
                val change = Intent(this, Negative::class.java)
                startActivity(change)
                return
            }
            if(glucose < 150 && hemoglobin < 6) {
                db.collection("diagnosis").document(email)
                    .update("diabetic", "maybe")
                val change = Intent(this, Medium::class.java)
                startActivity(change)
                return
            }
            val change = Intent(this, Medium::class.java)
            startActivity(change)
        }

        if(glucose < 130) {
            db.collection("diagnosis").document(email)
                .update("diabetic", "no")
            if(glucose < 120 && glucose > 90 && hemoglobin > 6.4) {
                db.collection("diagnosis").document(email)
                    .update("diabetic", "maybe")
                val change = Intent(this, Medium::class.java)
                startActivity(change)
                return
            }
            val change = Intent(this, Negative::class.java)
            startActivity(change)
        }
    }

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
