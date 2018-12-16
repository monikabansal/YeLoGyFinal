package com.yelogy.apiclient

import com.yelogy.deliveryaddress.NearByStoreResponse
import com.yelogy.signup.SignupResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

interface YelogyApi {

    companion object {
        const val BASE_URL = "http://18.220.92.227/virender_test/api/"

    }

    @FormUrlEncoded
    @POST("signup")
    fun signup(@FieldMap signupRequest: Map<String, String>): Observable<SignupResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(@FieldMap loginRequest: Map<String, String>): Observable<SignupResponse>



    @FormUrlEncoded
    @POST("near_by_store")
    fun nearByStore(@FieldMap nearByStoreRequest: Map<String, String>): Observable<NearByStoreResponse>

}