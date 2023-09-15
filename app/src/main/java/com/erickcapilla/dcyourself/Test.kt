package com.erickcapilla.dcyourself

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erickcapilla.dcyourself.core.adapter.QuestionAdapter
import com.erickcapilla.dcyourself.databinding.ActivityTestBinding
import com.erickcapilla.dcyourself.data.model.DataQuestions
import com.erickcapilla.dcyourself.data.network.QuestionProvider
import com.erickcapilla.dcyourself.util.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Test : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    private lateinit var questionMutableList: MutableList<DataQuestions>
    private lateinit var adapter: QuestionAdapter
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val textView16 = findViewById<TextView>(R.id.textView16)
        val recycleView = findViewById<RecyclerView>(R.id.recycleView)
        val layout_second = findViewById<ConstraintLayout>(R.id.layout_second)

        var date = ""
        var gender = ""
        var diabetic = ""

        val uiModel = Utils()

        auth = Firebase.auth
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        var email = ""
        user?.let {
            for (profile in it.providerData) {
                email = profile.email.toString()
            }
        }

        val docRefDiag = db.collection("dignosis").document(email)

        docRefDiag.get().addOnSuccessListener { document ->
            if (document != null) {
                diabetic = document.data?.get("diabetic").toString()
            }
        }

        val docRefUser = db.collection("info").document(email)

        docRefUser.get().addOnSuccessListener { document ->
            if(document != null) {
                textView16.visibility = View.VISIBLE
                recycleView.visibility = View.VISIBLE
                layout_second.visibility = View.GONE
                gender = document.data?.get("gender").toString()
                date = document.data?.get("date").toString()
                Log.v(TAG, age(date).toString())
                if(gender == "Mujer") {
                    if(age(date) < 12) {
                        questionMutableList = QuestionProvider.questionsListN.toMutableList()
                        initRecyclerView()
                    }
                    questionMutableList = QuestionProvider.questionsListM.toMutableList()
                    initRecyclerView()
                } else if(age(date) < 18) {
                    questionMutableList = QuestionProvider.questionsListN.toMutableList()
                    initRecyclerView()
                } else {
                    questionMutableList = QuestionProvider.questionsListH.toMutableList()
                    initRecyclerView()
                }
            }
        }

        val next = findViewById<Button>(R.id.next)
        next.setOnClickListener {
            if (adapter.todosLosGruposSeleccionados()) {
                db.collection("diagnosis").document(email)
                    .update("type", "Prediabetes").addOnSuccessListener {
                        textView16.visibility = View.GONE
                        recycleView.visibility = View.GONE
                        layout_second.visibility = View.VISIBLE
                    }
                if(gender == "Mujer") {
                    if(adapter.getAnswers()[0] == "Si") {
                        db.collection("diagnosis").document(email)
                            .update("type", "Diabetes Gestacional").addOnSuccessListener {
                                textView16.visibility = View.GONE
                                recycleView.visibility = View.GONE
                                layout_second.visibility = View.VISIBLE
                            }
                    }
                }

                if(diabetic == "maybe") {
                    db.collection("diagnosis").document(email)
                        .update("type", "Diabetes Tipo 1").addOnSuccessListener {
                            textView16.visibility = View.GONE
                            recycleView.visibility = View.GONE
                            layout_second.visibility = View.VISIBLE
                        }
                }

                if(diabetic == "yes") {
                    db.collection("diagnosis").document(email)
                        .update("type", "Diabetes Tipo 2").addOnSuccessListener {
                            textView16.visibility = View.GONE
                            recycleView.visibility = View.GONE
                            layout_second.visibility = View.VISIBLE
                        }
                }

                if(age(date) < 18) {
                    if(diabetic == "yes") {
                        db.collection("diagnosis").document(email)
                            .update("type", "PreDiabetes").addOnSuccessListener {
                                textView16.visibility = View.GONE
                                recycleView.visibility = View.GONE
                                layout_second.visibility = View.VISIBLE
                            }
                    }
                }
                val change = Intent(this, Type::class.java)
                startActivity(change)
            } else {
                uiModel.showToast(applicationContext, "Constesta todas las preguntas")
            }
        }
    }

    private fun initRecyclerView() {
        adapter = QuestionAdapter(questionsList = questionMutableList)
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = adapter
    }

    fun age(fechaNacimiento: String): Int {
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaNacimientoDate = formatoFecha.parse(fechaNacimiento)

        val calNacimiento = Calendar.getInstance()
        if (fechaNacimientoDate != null) {
            calNacimiento.time = fechaNacimientoDate
        }

        val calActual = Calendar.getInstance()

        var edad = calActual.get(Calendar.YEAR) - calNacimiento.get(Calendar.YEAR)

        if (calNacimiento.get(Calendar.MONTH) > calActual.get(Calendar.MONTH) ||
            (calNacimiento.get(Calendar.MONTH) == calActual.get(Calendar.MONTH) &&
                    calNacimiento.get(Calendar.DAY_OF_MONTH) > calActual.get(Calendar.DAY_OF_MONTH))) {
            edad--
        }

        return edad
    }

    private fun onItemSelected(position: Int, questionMutableList: DataQuestions) {}

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this@Test)
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