package com.erickcapilla.dcyourself.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erickcapilla.dcyourself.model.DataMedicines
import com.erickcapilla.dcyourself.R

class MedicineAdapter(private val medicineList: List<DataMedicines>, private val onClickListener: (Int, DataMedicines) -> Unit):RecyclerView.Adapter<MedicineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MedicineViewHolder(layoutInflater.inflate(R.layout.item_medicine, parent, false))
    }

    override fun getItemCount(): Int = medicineList.size

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        holder.render(medicineList[position], onClickListener)
    }
}