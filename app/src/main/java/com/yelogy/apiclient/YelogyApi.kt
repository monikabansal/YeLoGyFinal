package com.yelogy.apiclient

import com.yelogy.signup.SignupResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

interface YelogyApi {
    companion object {
        //        const val BASE_URL = "http://192.168.1.92/drewel/web_services/";
        const val BASE_URL = "https://laravel.planetwebsolution.com/virender_test/api/"

    }

    @FormUrlEncoded
    @POST("signup")
    fun signup(@FieldMap signupRequest: Map<String, String>): Observable<SignupResponse>
}