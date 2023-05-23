package com.erickcapilla.dcyourself

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.erickcapilla.dcyourself.adapter.QuestionAdapter
import com.erickcapilla.dcyourself.databinding.ActivityTestBinding
import com.erickcapilla.dcyourself.model.DataQuestions
import com.erickcapilla.dcyourself.provider.services.firebase.QuestionProvider

class Test : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    private var questionMutableList: MutableList<DataQuestions> = QuestionProvider.questionsList.toMutableList()
    private lateinit var adapter: QuestionAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
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