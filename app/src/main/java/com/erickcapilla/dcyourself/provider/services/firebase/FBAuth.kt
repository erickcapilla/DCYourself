package com.erickcapilla.dcyourself.provider.services.firebase

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class FBAuth {
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser

    /**
     * Is user logged
     *
     * @return The user is logged into an account
     */
    fun isUserLogged() : Boolean {
        return user != null
    }

    fun signUpUser(name: String, lastName: String, lastName2: String, email: String, password: String): Boolean {
        var state = false
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                state = true
                val user = Firebase.auth.currentUser!!
                val credential = EmailAuthProvider
                    .getCredential(email, password)
                user.reauthenticate(credential)
                    .addOnCompleteListener {
                        db.collection("user").document(email)
                            .set(hashMapOf(
                                "name" to name,
                                "lastName" to lastName,
                                "lastName2" to lastName2
                            ))
                    }
                Firebase.auth.signOut()
                Log.v(TAG, "Estado 1: " + state.toString())
            }
        }
        return false
    }
}