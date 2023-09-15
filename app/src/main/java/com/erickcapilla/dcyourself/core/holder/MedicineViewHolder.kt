package com.erickcapilla.dcyourself.core.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.erickcapilla.dcyourself.data.model.DataMedicines
import com.erickcapilla.dcyourself.databinding.ItemMedicineBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MedicineViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemMedicineBinding.bind(view)
    fun render(medicineModel: DataMedicines, onClickListener: (String, medicineModel: DataMedicines) -> Unit) {
            binding.medName.text = medicineModel.medicineName
            binding.medTime.text = "Próximo en " + medicineModel.medicineTime
            binding.medQuantity.text = "Cant. " + medicineModel.medicineQuantity
            binding.medDays.text = "Días: " + medicineModel.medicineDays
            binding.medDose.text = medicineModel.medicineDose + " tabletas"

        itemView.setOnClickListener { onClickListener(medicineModel.id, medicineModel) }
    }
}
