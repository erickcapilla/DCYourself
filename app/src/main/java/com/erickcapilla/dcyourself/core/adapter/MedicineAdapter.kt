package com.erickcapilla.dcyourself.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erickcapilla.dcyourself.data.model.DataMedicines
import com.erickcapilla.dcyourself.R
import com.erickcapilla.dcyourself.core.holder.MedicineViewHolder

class MedicineAdapter(private val medicineList: List<DataMedicines>, private val onClickListener: (String, DataMedicines) -> Unit):RecyclerView.Adapter<MedicineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MedicineViewHolder(layoutInflater.inflate(R.layout.item_medicine, parent, false))
    }

    override fun getItemCount(): Int = medicineList.size

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        holder.render(medicineList[position], onClickListener)
    }
}