package com.yelogy.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.yelogy.R
import com.yelogy.deliveryaddress.NearByStoreResponse
import com.yelogy.login.LoginRepository
import com.yelogy.login.LoginRequest
import com.yelogy.signup.SignupResponse

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    val nearByStoreResponse : LiveData<NearByStoreResponse> = homeRepository.nearByStoreResponse


    init {
        homeRepository.getNearByStores()
    }



}