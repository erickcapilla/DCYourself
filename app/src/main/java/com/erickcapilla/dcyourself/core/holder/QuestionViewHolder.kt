package com.erickcapilla.dcyourself.core.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.erickcapilla.dcyourself.databinding.ItemQuestionBinding
import com.erickcapilla.dcyourself.data.model.DataQuestions

class QuestionViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemQuestionBinding.bind(view)

    fun render(questionsModel: DataQuestions) {
        binding.questionText.text = questionsModel.question
    }
}