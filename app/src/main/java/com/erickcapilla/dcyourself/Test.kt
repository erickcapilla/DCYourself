package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.erickcapilla.dcyourself.adapter.QuestionAdapter
import com.erickcapilla.dcyourself.databinding.ActivityTestBinding
import com.erickcapilla.dcyourself.model.DataQuestions
import com.erickcapilla.dcyourself.provider.services.firebase.QuestionProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Test : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    private lateinit var questionMutableList: MutableList<DataQuestions>
    private lateinit var adapter: QuestionAdapter
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        var email = ""
        user?.let {
            for (profile in it.providerData) {
                email = profile.email.toString()
            }
        }

        val docRefUser = db.collection("info").document(email)

        docRefUser.get().addOnSuccessListener { document ->
            if(document != null) {
                var gender = document.data?.get("gender").toString()
                if(gender == "Mujer") {
                    questionMutableList = QuestionProvider.questionsListM.toMutableList()
                    initRecyclerView()
                } else {
                    questionMutableList = QuestionProvider.questionsListH.toMutableList()
                    initRecyclerView()
                }
            }
        }

        val next = findViewById<Button>(R.id.next)
        next.setOnClickListener {
            val change = Intent(this, Type::class.java)
            startActivity(change)
        }
    }

    private fun initRecyclerView() {
        adapter = QuestionAdapter(questionsList = questionMutableList)
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = adapter
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