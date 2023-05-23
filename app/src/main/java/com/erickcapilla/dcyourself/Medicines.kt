package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.erickcapilla.dcyourself.adapter.MedicineAdapter
import com.erickcapilla.dcyourself.databinding.ActivityMedicinesBinding
import com.erickcapilla.dcyourself.model.DataMedicines
import com.erickcapilla.dcyourself.provider.services.firebase.MedicineProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class Medicines : AppCompatActivity() {
    private lateinit var binding: ActivityMedicinesBinding
    private var medicineMutableList: MutableList<DataMedicines> = MedicineProvider.medicinesList.toMutableList()
    private lateinit var adapter: MedicineAdapter
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
        adapter = MedicineAdapter(
            medicineList = medicineMutableList,
            onClickListener = { position, medicineMutableList -> onItemSelected(position, medicineMutableList) }
        )
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = adapter

        val bundle = intent.extras
        val deletedPosition= bundle?.getInt("position")
        val deleted = bundle?.getBoolean("delete")
        if(deleted == true) {
            if (deletedPosition != null) {
                medicineMutableList.removeAt(deletedPosition)
                adapter.notifyItemRemoved(deletedPosition)
                Toast.makeText(applicationContext, "Eliminado $deletedPosition", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onItemSelected(position: Int, medicineMutableList: DataMedicines) {
        val change = Intent(this, UpdateMedicines::class.java)
        change.putExtra("position", position)
        change.putExtra("name", medicineMutableList.medicineName)
        change.putExtra("dose", medicineMutableList.medicineDose)
        change.putExtra("days", medicineMutableList.medicineDays)
        change.putExtra("time", medicineMutableList.medicineTime)
        change.putExtra("quantity", medicineMutableList.medicineQuantity)
        startActivity(change)
        change.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        overridePendingTransition(0,0)
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