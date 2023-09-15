package com.erickcapilla.dcyourself.data.network

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FBAuth {

  private val user = Firebase.auth.currentUser

  fun isUserLogged(): Boolean {
    return user != null
  }

}