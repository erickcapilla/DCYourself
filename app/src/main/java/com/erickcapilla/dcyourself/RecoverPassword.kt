package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.erickcapilla.dcyourself.databinding.ActivityMainBinding
import com.erickcapilla.dcyourself.databinding.ActivityRecoverPasswordBinding
import com.erickcapilla.dcyourself.ui.MainActivity
import com.erickcapilla.dcyourself.util.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecoverPassword : AppCompatActivity() {

  private lateinit var binding: ActivityRecoverPasswordBinding
  private lateinit var auth: FirebaseAuth

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityRecoverPasswordBinding.inflate(layoutInflater)
    setContentView(binding.root)


    auth = Firebase.auth
    val uiModel = Utils()

    binding.next.setOnClickListener {
      if (uiModel.isEditEmpty(listOf(binding.editEmail))) {
        uiModel.showToast(applicationContext, "Ingresa tu correo")
        return@setOnClickListener
      }

      val email = binding.editEmail.text.toString()

      if (!uiModel.isEmailValid(email)) {
        uiModel.showToast(applicationContext, "Email no valido")
        return@setOnClickListener
      }

      binding.progressBar.visibility = View.VISIBLE
      binding.next.isClickable = false
      binding.goBack.isClickable = false

      auth.sendPasswordResetEmail(email)
        .addOnCompleteListener {
          if (it.isSuccessful) {
            uiModel.showToast(this, "Email enviado. ¡Revisa tu correo!")
            binding.editEmail.setText("")
            binding.progressBar.visibility = View.GONE
            binding.next.isClickable = true
            binding.goBack.isClickable = true
          } else {
            uiModel.showToast(this, "Hubo un problema")
            binding.editEmail.setText("")
            binding.progressBar.visibility = View.GONE
            binding.next.isClickable = true
            binding.goBack.isClickable = true
          }
        }
    }

    binding.goBack.setOnClickListener { finish() }
  }

  private fun goMain() {
    val intent = Intent(this, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    startActivity(intent)
  }

  @Deprecated("Deprecated in Java")
  override fun onBackPressed() {
    AlertDialog.Builder(this@RecoverPassword)
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