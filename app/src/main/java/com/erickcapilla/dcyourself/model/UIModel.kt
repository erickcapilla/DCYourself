package com.erickcapilla.dcyourself.model

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity

class UIModel {
    fun isEditEmpty(edits: List<EditText>): Boolean {
        var empty : Boolean = false
        for (edit in edits) {
            if (edit.text.toString().trim().isEmpty()) empty = true
        }
        return empty
    }

    fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun isEmailValid( email: String ): Boolean {
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
        return EMAIL_REGEX.toRegex().matches(email);
    }
}