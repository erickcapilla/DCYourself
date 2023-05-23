package com.erickcapilla.dcyourself.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.erickcapilla.dcyourself.model.DataMedicines
import com.erickcapilla.dcyourself.databinding.ItemMedicineBinding

class MedicineViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemMedicineBinding.bind(view)

    fun render(medicineModel: DataMedicines, onClickListener: (Int, medicineModel: DataMedicines) -> Unit) {
        binding.medName.text = medicineModel.medicineName
        binding.medTime.text = medicineModel.medicineTime
        binding.medQuantity.text = medicineModel.medicineQuantity
        binding.medDays.text = medicineModel.medicineDays
        binding.medDose.text = medicineModel.medicineDose

        itemView.setOnClickListener { onClickListener(absoluteAdapterPosition, medicineModel) }
    }
}
