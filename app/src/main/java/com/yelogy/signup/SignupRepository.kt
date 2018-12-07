package com.yelogy.signup

import android.content.Context
import android.util.Log
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.yelogy.apiclient.YelogyApi
import com.yelogy.application.YelogyApplication
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SignupRepository(val context:Context){


    private var disposable: Disposable? = null

    fun onSignupApiCalling(signUpRequest: SignUpRequest){
        if(NetworkUtils.isConnected()){
            val json = Gson().toJson(signUpRequest)// obj is your object

            val result =  Gson().fromJson<Map<String,String>>(json, Map::class.java)

            val signUpObservable = YelogyApplication.getInstance().getRequestQueue().create(YelogyApi::class.java).signup(result)
            disposable = signUpObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    ToastUtils.showShort("dfdfdf")
                    /* hide progress bar*/

                }, { error ->

                    Log.e("TAG", "{$error.message}")
                }
                )
        }

    }

}