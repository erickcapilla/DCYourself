package com.erickcapilla.dcyourself.provider.services.firebase

import com.erickcapilla.dcyourself.model.DataMedicines

class MedicineProvider {
    companion object {
        val medicinesList = FBAuth().getMeds()
    }
}