package com.yelogy.signup

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.text.TextUtils
import android.view.View
import com.yelogy.R
import com.yelogy.utill.ValidationUtil

class SignupViewModel(private val signupRepository: SignupRepository) : ViewModel() {

    val signUpRequest = ObservableField<SignUpRequest>()

    val firstname_error = ObservableField<String>()
    val pincode_error = ObservableField<String>()
    val mobile_error = ObservableField<String>()
    val emailError = ObservableField<String>()
    val passowrd_error = ObservableField<String>()
    val confirmpassword_error = ObservableField<String>()
    val signupResponse : LiveData<SignupResponse> = signupRepository.signupResponse
        init {
            signUpRequest.set(SignUpRequest())
        }
    fun onSingupClick() {
        signupRepository.hideKeyboard()
        if (validateSignUp()) {
            signupRepository.onSignupApiCalling(signUpRequest.get()!!)
        }

    }

    private fun validateSignUp(): Boolean {

        if (signUpRequest.get()!!.pincode.isNullOrBlank()) {
            pincode_error.set(signupRepository.context.getString(R.string.enter_pin_code))
            return false
        }
        pincode_error.set(null)
        if (signUpRequest.get()!!.firstName.isNullOrBlank()) {
            firstname_error.set(signupRepository.context.getString(R.string.enter_first_name))
            return false
        }
        firstname_error.set(null)
        if (signUpRequest.get()!!.mobileNumber.isNullOrBlank()) {
            mobile_error.set(signupRepository.context.getString(R.string.enter_phone_number))
            return false
        }
        if (signUpRequest.get()!!.mobileNumber?.length ?: 0 < 8) {
            mobile_error.set(signupRepository.context.getString(R.string.valid_phone_number))
            return false
        }
        mobile_error.set(null)

        if (signUpRequest.get()!!.email.isNullOrBlank()) {
            emailError.set(signupRepository.context.getString(R.string.enter_email_address))
            return false
        }

        if (!ValidationUtil.isEmailValid(signUpRequest.get()!!.email!!.trim())) {
            emailError.set(signupRepository.context.getString(R.string.enter_valid_email_address))
            return false
        }
        emailError.set(null)

        if (signUpRequest.get()!!.password.isNullOrBlank()) {
            passowrd_error.set(signupRepository.context.getString(R.string.empty_password))
            return false
        }

        if (signUpRequest.get()!!.password?.length ?: 0 < 6) {
            passowrd_error.set(signupRepository.context.getString(R.string.short_password))
            return false
        }

        passowrd_error.set(null)

        if (signUpRequest.get()!!.confirmPassword.isNullOrBlank()) {
            confirmpassword_error.set(signupRepository.context.getString(R.string.empty_confirm_password))
            return false
        }


        if (signUpRequest.get()!!.confirmPassword != signUpRequest.get()!!.password) {
            confirmpassword_error.set(signupRepository.context.getString(R.string.dont_match_password))
            return false
        }
        confirmpassword_error.set(null)

        return true
    }


}

