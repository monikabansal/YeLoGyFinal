package com.yelogy.signup

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class SignupViewModelFactory(private val signupRepository: SignupRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignupViewModel(signupRepository) as T
    }
}