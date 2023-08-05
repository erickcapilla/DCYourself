package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.marginTop
import com.erickcapilla.dcyourself.model.DataMedicines
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Graph : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)


        val textDiagnose = findViewById<TextView>(R.id.diagnose)
        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        val bottomAdd = findViewById<ImageButton>(R.id.buttonAdd)
        val imgState = findViewById<ImageView>(R.id.imageState)
        val status = findViewById<TextView>(R.id.status)
        val horizontalLine = findViewById<View>(R.id.horizontalLine)
        val glucoseText = findViewById<TextView>(R.id.glucoseText)
        val hemoglobinText = findViewById<TextView>(R.id.hemoglobinText)
        val insulinText = findViewById<TextView>(R.id.insulinText)
        val verticalLine = findViewById<View>(R.id.verticalLine)
        val graph1 = findViewById<TextView>(R.id.graph1)
        val graph2 = findViewById<TextView>(R.id.graph2)
        val graph3 = findViewById<TextView>(R.id.graph3)
        val userState = findViewById<TextView>(R.id.userState)
        val textButton = findViewById<TextView>(R.id.textButton)

        auth = Firebase.auth
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        var email = ""
        user?.let {
            for (profile in it.providerData) {
                email = profile.email.toString()
            }
        }

        val docRefUser = db.collection("data").document(email)
        val medicinesData = docRefUser.collection("userData")

        medicinesData.get().addOnSuccessListener { documents ->
            var sumGlucose = 0.0
            var sumHemoglobin = 0.0
            var sumInsulin = 0.0

            if(documents.isEmpty) {
                textDiagnose.visibility = View.VISIBLE

                bottomAdd.visibility = View.GONE
                imgState.visibility = View.GONE
                status.visibility = View.GONE
                horizontalLine.visibility = View.GONE
                glucoseText.visibility = View.GONE
                hemoglobinText.visibility = View.GONE
                insulinText.visibility = View.GONE
                verticalLine.visibility = View.GONE
                graph1.visibility = View.GONE
                graph2.visibility = View.GONE
                graph3.visibility = View.GONE
                userState.visibility = View.GONE
                textButton.visibility = View.GONE
            } else {
                for (document in documents) {
                    val data = document.data
                    val glucose = data["glucose"].toString().toFloat()
                    val hemoglobin = data["hemoglobin"].toString().toFloat()
                    val insulin = data["insulin"].toString().toFloat()

                    sumGlucose += glucose
                    sumHemoglobin += hemoglobin
                    sumInsulin += insulin

                    textDiagnose.visibility = View.GONE

                    bottomAdd.visibility = View.VISIBLE
                    imgState.visibility = View.VISIBLE
                    status.visibility = View.VISIBLE
                    horizontalLine.visibility = View.VISIBLE
                    glucoseText.visibility = View.VISIBLE
                    hemoglobinText.visibility = View.VISIBLE
                    insulinText.visibility = View.VISIBLE
                    verticalLine.visibility = View.VISIBLE
                    graph1.visibility = View.VISIBLE
                    graph2.visibility = View.VISIBLE
                    graph3.visibility = View.VISIBLE
                    userState.visibility = View.VISIBLE
                    textButton.visibility = View.VISIBLE
                }

                sumGlucose /= documents.size()
                sumHemoglobin /= documents.size()
                sumInsulin /= documents.size()

                if(sumGlucose > 199) {
                    val layoutParams = graph1.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.setMargins(layoutParams.leftMargin, 5, layoutParams.rightMargin, layoutParams.bottomMargin)
                    graph1.layoutParams = layoutParams
                    graph1.setBackgroundResource(R.drawable.graph_red)
                    imgState.setImageResource(R.drawable.triste)
                    status.text = "¡Tu estado de salud es malo!"
                    userState.text = "Riesgo Alto"
                }

                if(sumGlucose > 129 && sumGlucose < 200) {
                    val layoutParams = graph1.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.setMargins(layoutParams.leftMargin, 250, layoutParams.rightMargin, layoutParams.bottomMargin)
                    graph1.layoutParams = layoutParams
                    graph1.setBackgroundResource(R.drawable.graph_yellow)
                    imgState.setImageResource(R.drawable.triste)
                    status.text = "¡Tu estado de salud es regular!"
                    userState.text = "Riesgo Medio"
                }

                if(sumGlucose < 130) {

                    val layoutParams = graph1.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.setMargins(layoutParams.leftMargin, 500, layoutParams.rightMargin, layoutParams.bottomMargin)
                    graph1.layoutParams = layoutParams
                    graph1.setBackgroundResource(R.drawable.graph_green)
                    imgState.setImageResource(R.drawable.feliz)
                    status.text = "¡Tu estado de salud es bueno!"
                    userState.text = "Riesgo Bajo"
                }

                if(sumHemoglobin > 6.4) {
                    val layoutParams = graph2.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.setMargins(layoutParams.leftMargin, 5, layoutParams.rightMargin, layoutParams.bottomMargin)
                    graph2.layoutParams = layoutParams
                    graph2.setBackgroundResource(R.drawable.graph_red)
                }

                if(sumHemoglobin > 5.6 && sumHemoglobin < 6.5) {
                    val layoutParams = graph2.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.setMargins(layoutParams.leftMargin, 250, layoutParams.rightMargin, layoutParams.bottomMargin)
                    graph2.layoutParams = layoutParams
                    graph2.setBackgroundResource(R.drawable.graph_yellow)
                }

                if(sumHemoglobin < 5.8) {
                    val layoutParams = graph2.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.setMargins(layoutParams.leftMargin, 500, layoutParams.rightMargin, layoutParams.bottomMargin)
                    graph2.layoutParams = layoutParams
                    graph2.setBackgroundResource(R.drawable.graph_green)
                }

                if(sumInsulin > 25) {
                    val layoutParams = graph3.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.setMargins(layoutParams.leftMargin, 5, layoutParams.rightMargin, layoutParams.bottomMargin)
                    graph3.layoutParams = layoutParams
                    graph3.setBackgroundResource(R.drawable.graph_yellow)
                }

                if(sumInsulin > 2 && sumInsulin < 25) {
                    val layoutParams = graph3.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.setMargins(layoutParams.leftMargin, 250, layoutParams.rightMargin, layoutParams.bottomMargin)
                    graph3.layoutParams = layoutParams
                    graph3.setBackgroundResource(R.drawable.graph_green)
                }

                if(sumInsulin < 3) {
                    val layoutParams = graph3.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.setMargins(layoutParams.leftMargin, 500, layoutParams.rightMargin, layoutParams.bottomMargin)
                    graph3.layoutParams = layoutParams
                    graph3.setBackgroundResource(R.drawable.graph_red)
                }
            }

        }

        bottomMenu.selectedItemId = R.id.bottom_data

        bottomAdd.setOnClickListener {
            val change = Intent(this, AddData::class.java)
            startActivity(change)
        }

        bottomMenu.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.bottom_data-> true
                R.id.bottom_recommend -> {
                    val change = Intent(this, Recommendations::class.java)
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
                R.id.bottom_home -> {
                    val change = Intent(this, Home::class.java)
                    startActivity(change)
                    change.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.bottom_med -> {
                    val change = Intent(this, Medicines::class.java)
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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this@Graph)
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