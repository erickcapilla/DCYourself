package com.erickcapilla.dcyourself.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.erickcapilla.dcyourself.databinding.ItemQuestionBinding
import com.erickcapilla.dcyourself.model.DataQuestions

class QuestionViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemQuestionBinding.bind(view)

    fun render(questionsModel: DataQuestions) {
        binding.questionText.text = questionsModel.question
    }
}