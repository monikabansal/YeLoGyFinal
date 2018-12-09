package com.yelogy.useraccount

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.yelogy.R
import com.yelogy.login.LoginRepository
import com.yelogy.login.LoginRequest
import com.yelogy.signup.SignupResponse

class MyAccountViewModel() : ViewModel() {
    val isUserLogin = ObservableBoolean()
    val userData = ObservableField<SignupResponse.Data>()
    val userAddress = ObservableField<String>()


}