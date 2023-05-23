package com.erickcapilla.dcyourself.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.erickcapilla.dcyourself.model.DataRecommendations

class RecommendationViewHolder(view: View): RecyclerView.ViewHolder(view) {
    fun render(recommendationModel: DataRecommendations, onClickListener: (Int, recommendationModel: DataRecommendations) -> Unit) {
        itemView.setOnClickListener { onClickListener(absoluteAdapterPosition, recommendationModel) }
    }
}