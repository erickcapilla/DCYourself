package com.erickcapilla.dcyourself.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.erickcapilla.dcyourself.Home
import com.erickcapilla.dcyourself.Login
import com.erickcapilla.dcyourself.R
import com.erickcapilla.dcyourself.SignUp
import com.erickcapilla.dcyourself.databinding.ActivityMainBinding
import com.erickcapilla.dcyourself.data.network.FBAuth


class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.Theme_DCYourself)

    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val firebaseAuth = FBAuth()

    if (firebaseAuth.isUserLogged()) {
      val change = Intent(this, Home::class.java)
      startActivity(change)
    } else {
      setup()
    }

  }

  private fun setup() {
    binding.signUp.setOnClickListener {
      val change = Intent(this, SignUp::class.java)
      startActivity(change)
    }

    binding.logIn.setOnClickListener {
      val change = Intent(this, Login::class.java)
      startActivity(change)
    }
  }

  @Deprecated("Deprecated in Java")
  override fun onBackPressed() {
    AlertDialog.Builder(this@MainActivity)
      .setMessage("¿Salir de la aplicación?")
      .setCancelable(false)
      .setPositiveButton("Si") { _, _ -> finishAffinity() }
      .setNegativeButton("Cancelar") { _, _ -> }
      .show()
  }
}