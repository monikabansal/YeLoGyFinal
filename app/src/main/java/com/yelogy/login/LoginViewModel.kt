package com.yelogy.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.yelogy.R
import com.yelogy.signup.SignupResponse
import com.yelogy.utill.ValidationUtil

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    val loginRequest = LoginRequest()
     val loginResponse : LiveData<SignupResponse> = loginRepository.loginResponse

    val mobile_error = ObservableField<String>()
    val passowrd_error = ObservableField<String>()

    fun onLoginClick() {
        loginRepository.hideKeyboard()
        if (validateLogin()) {
            loginRepository.onLoginApiCalling(loginRequest)
        }



    }

    fun onSignupClick(){
        loginRepository.hideKeyboard()

    }
    fun onForgotPasswordClick(){
        loginRepository.hideKeyboard()

    }

    private fun validateLogin(): Boolean {


        if (loginRequest.mobileNumber.isNullOrBlank()) {
            mobile_error.set(loginRepository.context.getString(R.string.enter_phone_number))
            return false
        }
        if (loginRequest.mobileNumber?.length ?: 0 < 8) {
            mobile_error.set(loginRepository.context.getString(R.string.valid_phone_number))
            return false
        }
        mobile_error.set(null)


        if (loginRequest.password.isNullOrBlank()) {
            passowrd_error.set(loginRepository.context.getString(R.string.empty_password))
            return false
        }

        if (loginRequest.password?.length ?: 0 < 6) {
            passowrd_error.set(loginRepository.context.getString(R.string.short_password))
            return false
        }

        passowrd_error.set(null)


        return true
    }


}