package com.erickcapilla.dcyourself

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SignUp2 : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    /*
    * Instancia de la base de datos
    * */
    private val db = Firebase.firestore
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up2)

        auth = Firebase.auth

        val bundle = intent.extras
        val name = bundle?.getString("name")
        val lastName = bundle?.getString("lastName")
        val lastName2 = bundle?.getString("lastName2")

        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editPassword = findViewById<EditText>(R.id.editPassword)
        val editConfirmPassword = findViewById<EditText>(R.id.editConfirmPassword)
        val accept = findViewById<CheckBox>(R.id.accept)

        val signUp = findViewById<Button>(R.id.signUp)
        signUp.setOnClickListener {
            if(!editEmpty(editEmail) || !editEmpty(editPassword) || !editEmpty(editConfirmPassword)) {
                if(editPassword.text.toString() == editConfirmPassword.text.toString()) {
                    if(accept.isChecked) {
                        /*
                        * Crea un nuevo usuario con el correo y contraseña
                        * @param email Email del usuario
                        * @param password Contraseña del usuario
                        * */
                        auth.createUserWithEmailAndPassword(editEmail.text.toString(),
                            editPassword.text.toString()).addOnCompleteListener(this) {
                                if(it.isSuccessful) {
                                    Toast.makeText(applicationContext, "Usuario registrado",
                                        Toast.LENGTH_SHORT).show()
                                    /*
                                    * user: Instancia para los usuario registrados en la base de datos
                                    * */
                                    val user = Firebase.auth.currentUser!!

                                    val credential = EmailAuthProvider
                                        .getCredential(editEmail.text.toString(), editPassword.text.toString())
                                    /*
                                    * Guarda los datos del usuario en la base de datos
                                    * @para credential Credenciales del usuario autenticado
                                    * @param email Correo del usuario al que se asocian los datos
                                    * @param data Datos del usuario (nombre, apellido paterno, apellido materno)
                                    * */
                                    user.reauthenticate(credential)
                                        .addOnCompleteListener {
                                            db.collection("user").document(editEmail.text.toString())
                                                .set(hashMapOf(
                                                    "name" to name,
                                                    "lastName" to lastName,
                                                    "lastName2" to lastName2
                                                ))
                                        }
                                    Firebase.auth.signOut()
                                    val change = Intent(this, Login::class.java)
                                    startActivity(change)
                                } else {
                                    Toast.makeText(applicationContext, "Se ha producido un error, Vuelve a intentarlo",
                                        Toast.LENGTH_SHORT).show()
                                }
                        }


                    } else {
                        Toast.makeText(applicationContext, "Debes aceptar los términos y condiciones", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Ingresa todos los datos que se solicitan", Toast.LENGTH_SHORT).show()
            }
        }

        val goBack = findViewById<Button>(R.id.go_back)
        goBack.setOnClickListener{
            finish()
        }
    }

    private fun editEmpty(edit: EditText): Boolean {
        return edit.text.toString().trim().isEmpty()
    }
}