package com.erickcapilla.dcyourself.provider.services.firebase

import com.erickcapilla.dcyourself.model.DataMedicines

class MedicineProvider {
    companion object {
        val medicinesList = listOf(
            DataMedicines("Med 1", "Cantidad: 15", "Días: 5", "Próxima dosis en: 4 hrs", "2 tabletas"),
            DataMedicines("Med 2", "Cantidad: 5", "Días: 15", "Próxima dosis en: 2 hrs", "1 tableta"),
            DataMedicines("Med 3", "Cantidad: 15", "Días: 5", "Próxima dosis en: 4 hrs", "2 tabletas"),
            DataMedicines("Med 4", "Cantidad: 5", "Días: 15", "Próxima dosis en: 2 hrs", "1 tabletas"),
            DataMedicines("Med 5", "Cantidad: 15", "Días: 5", "Próxima dosis en: 4 hrs", "2 tabletas"),
            DataMedicines("Med 6", "Cantidad: 5", "Días: 15", "Próxima dosis en: 2 hrs", "2 tabletas"),
            DataMedicines("Med 7", "Cantidad: 15", "Días: 5", "Próxima dosis en: 4 hrs", "3 tabletas"),
            DataMedicines("Med 8", "Cantidad: 5", "Días: 15", "Próxima dosis en: 2 hrs", "2 tabletas")
        )
    }
}