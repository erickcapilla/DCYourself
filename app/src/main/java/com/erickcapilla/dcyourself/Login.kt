package com.erickcapilla.dcyourself

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.erickcapilla.dcyourself.databinding.ActivityLoginBinding
import com.erickcapilla.dcyourself.ui.MainActivity
import com.erickcapilla.dcyourself.ui.login.viewmodel.LoginViewModel
import com.erickcapilla.dcyourself.util.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.activity.viewModels

class Login : AppCompatActivity() {

  private lateinit var auth: FirebaseAuth
  private lateinit var binding: ActivityLoginBinding
  //private val loginViewModel: LoginViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)

    auth = Firebase.auth
    val uiModel = Utils()

    var visibility = false

    binding.eyeButton.setOnClickListener {
      if (visibility) {
        visibility = false
        binding.editPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.editPassword.typeface = ResourcesCompat.getFont(this, R.font.droid_sans)
        binding.eyeButton.setImageResource(R.drawable.baseline_visibility_24)
        binding.editPassword.setSelection(binding.editPassword.text.length)
      } else {
        visibility = true
        binding.editPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        binding.eyeButton.setImageResource(R.drawable.baseline_visibility_off_24)
        binding.editPassword.setSelection(binding.editPassword.text.length)
      }
    }

    binding.login.setOnClickListener {
      val email: String
      val password: String

      if (uiModel.isEditEmpty(listOf(binding.editEmail))) {
        binding.editEmail.error = getString(R.string.data_necesary)
        return@setOnClickListener
      }

      if (uiModel.isEditEmpty(listOf(binding.editPassword))) {
        binding.editPassword.error = getString(R.string.data_necesary)
        return@setOnClickListener
      }

      email = binding.editEmail.text.toString()
      password = binding.editPassword.text.toString()

      if (!uiModel.isEmailValid(email)) {
        binding.editEmail.error = getString(R.string.email_valid)
        return@setOnClickListener
      }

      binding.progressBar.visibility = View.VISIBLE
      binding.login.isClickable = false

      auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
        if (it.isSuccessful) {
          binding.progressBar.visibility = View.GONE
          val change = Intent(this, Home::class.java)
          startActivity(change)
        } else {
          binding.login.isClickable = true
          binding.progressBar.visibility = View.GONE
          uiModel.showToast(applicationContext, "El usuario no existe. Revisa tu conexión")
        }
      }
    }

    binding.textForgot.setOnClickListener {
      val change = Intent(this, RecoverPassword::class.java)
      startActivity(change)
    }

    binding.goBack.setOnClickListener {
      val change = Intent(this, MainActivity::class.java)
      startActivity(change)
    }
  }

  @Deprecated("Deprecated in Java")
  override fun onBackPressed() {
    AlertDialog.Builder(this@Login)
      .setMessage("¿Salir de la aplicación?")
      .setCancelable(false)
      .setPositiveButton("Si") { _, _ -> finishAffinity() }
      .setNegativeButton("Cancelar") { _, _ ->}
      .show()
  }
}