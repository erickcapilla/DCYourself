package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.viewpager.widget.ViewPager
import com.erickcapilla.dcyourself.adapter.RecommendationAdapter
import com.erickcapilla.dcyourself.databinding.ActivityRecommendationsBinding
import com.erickcapilla.dcyourself.model.DataRecommendations
import com.erickcapilla.dcyourself.provider.services.firebase.RecommendationProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class Recommendations : AppCompatActivity() {
    private lateinit var binding: ActivityRecommendationsBinding
    private var recommendationNutritionalMutableList: MutableList<DataRecommendations> = RecommendationProvider.recommendationNutritionalList.toMutableList()
    private var recommendationExerciseMutableList: MutableList<DataRecommendations> = RecommendationProvider.recommendationExerciseList.toMutableList()
    private lateinit var adapterNutritional: RecommendationAdapter
    private lateinit var adapterExercise: RecommendationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendations)

        val viewPagerNutritional = findViewById<ViewPager>(R.id.viewPagerNutritional)
        viewPagerNutritional.pageMargin = 10
        adapterNutritional = RecommendationAdapter(recommendationList = recommendationNutritionalMutableList, R.drawable.baseline_food_24)
        viewPagerNutritional.adapter = adapterNutritional

        val viewPagerExercise = findViewById<ViewPager>(R.id.viewPagerExercise)
        viewPagerExercise.pageMargin = 10
        adapterExercise = RecommendationAdapter(recommendationList = recommendationExerciseMutableList, R.drawable.baseline_bike_24)
        viewPagerExercise.adapter = adapterExercise


        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomMenu.selectedItemId = R.id.bottom_recommend

        bottomMenu.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.bottom_recommend -> true
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
        AlertDialog.Builder(this@Recommendations)
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