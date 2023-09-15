package com.erickcapilla.dcyourself.ui.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erickcapilla.dcyourself.data.model.LoginModel

class LoginViewModel : ViewModel() {
  val loginModel = MutableLiveData<LoginModel>()


}