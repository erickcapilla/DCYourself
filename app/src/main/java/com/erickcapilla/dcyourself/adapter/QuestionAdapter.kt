package com.erickcapilla.dcyourself.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erickcapilla.dcyourself.R
import com.erickcapilla.dcyourself.model.DataQuestions

class QuestionAdapter(private val questionsList: List<DataQuestions>):RecyclerView.Adapter<QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return QuestionViewHolder(layoutInflater.inflate(R.layout.item_question, parent, false))
    }

    override fun getItemCount(): Int = questionsList.size

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.render(questionsList[position])
    }
}