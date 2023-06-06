package com.erickcapilla.dcyourself.util

import android.content.Context
import android.widget.EditText
import android.widget.Toast

class UIUtils {
    fun isEditEmpty(edits: List<EditText>): Boolean {
        var empty = false
        for (edit in edits) {
            if (edit.text.toString().trim().isEmpty()) empty = true
        }
        return empty
    }

    fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun isEmailValid( email: String ): Boolean {
        val EMAIL_REGEX = "^[A-Za-z0-9](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return EMAIL_REGEX.toRegex().matches(email);
    }
}