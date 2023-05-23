package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class AddMedicines : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medicines)

        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomMenu.selectedItemId = R.id.bottom_med

        bottomMenu.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.bottom_med -> {
                    val change = Intent(this, Medicines::class.java)
                    startActivity(change)
                    change.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.bottom_data -> {
                    val change = Intent(this, Graph::class.java)
                    startActivity(change)
                    change.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    overridePendingTransition(0,0)
                    finish()
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
}