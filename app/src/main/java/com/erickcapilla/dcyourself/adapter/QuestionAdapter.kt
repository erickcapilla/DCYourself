package com.erickcapilla.dcyourself.adapter

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.erickcapilla.dcyourself.R
import com.erickcapilla.dcyourself.model.DataQuestions

class QuestionAdapter(private val questionsList: List<DataQuestions>):RecyclerView.Adapter<QuestionViewHolder>() {
    private val selectedGroups = mutableListOf<Int>()
    private val selectedGAnswers = mutableListOf<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return QuestionViewHolder(layoutInflater.inflate(R.layout.item_question, parent, false))
    }

    override fun getItemCount(): Int = questionsList.size

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.render(questionsList[position])
        holder.itemView.findViewById<RadioGroup>(R.id.radioGroup).setOnCheckedChangeListener { _, checkedId ->
            val selectedPosition = position
            val selectedRadioButton = holder.itemView.findViewById<RadioButton>(checkedId)
            val selectedOption = selectedRadioButton.text.toString()
            if (selectedRadioButton != null && selectedRadioButton.isChecked) {
                selectedGroups.add(selectedPosition)
                selectedGAnswers.add(selectedOption)
            }
        }
    }

    fun todosLosGruposSeleccionados(): Boolean {
        return selectedGroups.size == itemCount || selectedGroups.size > itemCount
    }

    fun getAnswers(): MutableList<String> {
        return selectedGAnswers
    }
}