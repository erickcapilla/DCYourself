package com.erickcapilla.dcyourself

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.erickcapilla.dcyourself.databinding.ActivitySignUpBinding
import com.erickcapilla.dcyourself.util.Utils


class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uiModel = Utils()

        binding.next.setOnClickListener {
            if(!uiModel.isEditEmpty(listOf(binding.editName, binding.editLastName, binding.editLastName2))) {
                if(uiModel.isNotMin(listOf(binding.editName, binding.editLastName, binding.editLastName2))) {
                    binding.editName.error = getString(R.string.three_characters_min)
                    binding.editLastName.error = getString(R.string.three_characters_min)
                    binding.editLastName2.error = getString(R.string.three_characters_min)
                    return@setOnClickListener
                }
                val change = Intent(this, SignUp2::class.java)
                change.putExtra("name", binding.editName.text.toString().trim())
                change.putExtra("lastName", binding.editLastName.text.toString().trim())
                change.putExtra("lastName2", binding.editLastName2.text.toString().trim())
                startActivity(change)
            } else {
                uiModel.showToast(applicationContext, "Ingresa todos los datos que se solicitan")
            }
        }

        binding.goBack.setOnClickListener{
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this@SignUp)
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