package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.erickcapilla.dcyourself.core.adapter.MedicineAdapter
import com.erickcapilla.dcyourself.databinding.ActivityMedicinesBinding
import com.erickcapilla.dcyourself.data.model.DataMedicines
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

class Medicines : AppCompatActivity() {
    private lateinit var binding: ActivityMedicinesBinding
    private lateinit var medicineMutableList: MutableList<DataMedicines>
    private lateinit var adapter: MedicineAdapter
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicinesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()


        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomMenu.selectedItemId = R.id.bottom_med

        val addButton = findViewById<Button>(R.id.buttonAdd)
        addButton.setOnClickListener {
            val change = Intent(this, AddMedicines::class.java)
            startActivity(change)
            change.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            overridePendingTransition(0,0)
        }

        bottomMenu.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.bottom_med -> true
                R.id.bottom_data -> {
                    val change = Intent(this, Graph::class.java)
                    startActivity(change)
                    finish()
                    change.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    overridePendingTransition(0,0)
                    true
                }
                R.id.bottom_diagnose -> {
                    val change = Intent(this, Diagnose::class.java)
                    startActivity(change)
                    change.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    overridePendingTransition(0,0)
                    true
                }
                R.id.bottom_recommend -> {
                    val change = Intent(this, Recommendations::class.java)
                    startActivity(change)
                    change.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.bottom_home -> {
                    val change = Intent(this, Home::class.java)
                    startActivity(change)
                    change.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun initRecyclerView() {
        auth = Firebase.auth
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        var email = ""
        user?.let {
            for (profile in it.providerData) {
                email = profile.email.toString()
            }
        }

        val docRefUser = db.collection("med").document(email)
        val medicinesData = docRefUser.collection("meds")

        var medsList = listOf<DataMedicines>()
        medsList = medsList.toMutableList()

        medicinesData.get().addOnSuccessListener { documents ->
            val addMeds = findViewById<TextView>(R.id.addMeds)
            if(documents.isEmpty) {
                addMeds.visibility = View.VISIBLE
                binding.recycleView.visibility = View.GONE
            } else {
                for(document in documents) {
                    val data = document.data
                    val name = data["name"].toString()
                    val total = data["total"].toString()
                    val dose = data["dose"].toString()
                    val frequency = data["frequency"].toString()
                    val dateOne = data["dateOne"].toString()
                    val dateTwo = data["dateTwo"].toString()
                    val idDoc = document.id

                    addMeds.visibility = View.GONE
                    binding.recycleView.visibility = View.VISIBLE

                    val days = calcularDiasEntreFechas(dateOne, dateTwo).toString()

                    medsList.add(
                        DataMedicines(
                        idDoc, name, total, days, frequency, dose, dateTwo)
                    )
                }
            }

            medicineMutableList = medsList.toMutableList()
            adapter = MedicineAdapter(
                medicineList = medicineMutableList,
                onClickListener = { id, medicineMutableList -> onItemSelected(id, medicineMutableList) }
            )
            binding.recycleView.layoutManager = LinearLayoutManager(this)
            binding.recycleView.adapter = adapter
        }
    }

    private fun onItemSelected(id: String, medicineMutableList: DataMedicines) {
        val change = Intent(this, UpdateMedicines::class.java)
        change.putExtra("id", id)
        change.putExtra("name", medicineMutableList.medicineName)
        change.putExtra("dose", medicineMutableList.medicineDose)
        change.putExtra("days", medicineMutableList.medicineDays)
        change.putExtra("time", medicineMutableList.medicineTime)
        change.putExtra("quantity", medicineMutableList.medicineQuantity)
        change.putExtra("date", medicineMutableList.date)
        startActivity(change)
        change.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        overridePendingTransition(0,0)
    }

    fun calcularDiasEntreFechas(fechaInicio: String, fechaFin: String): Long {
        val formato = SimpleDateFormat("dd/MM/yyyy")

        try {
            val fechaInicioDate: Date = formato.parse(fechaInicio)
            val fechaFinDate: Date = formato.parse(fechaFin)

            val diferenciaEnMillis = fechaFinDate.time - fechaInicioDate.time
            return TimeUnit.DAYS.convert(diferenciaEnMillis, TimeUnit.MILLISECONDS)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return 0
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this@Medicines)
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