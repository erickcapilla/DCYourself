package com.erickcapilla.dcyourself.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.erickcapilla.dcyourself.model.DataMedicines
import com.erickcapilla.dcyourself.databinding.ItemMedicineBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MedicineViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private lateinit var auth: FirebaseAuth
    private val binding = ItemMedicineBinding.bind(view)
    fun render(medicineModel: DataMedicines, onClickListener: (Int, medicineModel: DataMedicines) -> Unit) {
        auth = Firebase.auth
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        var email = ""
        user?.let {
            for (profile in it.providerData) {
                email = profile.email.toString()
            }
        }
        val docRefUser = db.collection("med").document(email)

        docRefUser.get().addOnSuccessListener { document ->
            if (document != null) {
                val name = document.data?.get("name").toString()
                val total = document.data?.get("total").toString()
                val dose = document.data?.get("dose").toString()
                val frequency = document.data?.get("frequency").toString()
                val dateOne = document.data?.get("dateOne").toString()
                val dateTwo = document.data?.get("dateTwo").toString()
                binding.medName.text = name
                binding.medTime.text = "Próxima en $frequency hrs."
                binding.medQuantity.text = "$total tabletas"
                binding.medDays.text = dateOne
                binding.medDose.text = "$dose tabletas"
            }
        }

        itemView.setOnClickListener { onClickListener(absoluteAdapterPosition, medicineModel) }
    }
}
