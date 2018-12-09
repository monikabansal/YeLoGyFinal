package com.yelogy.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.util.Log
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.os.drewel.prefrences.Prefs
import com.yelogy.apiclient.YelogyApi
import com.yelogy.application.YelogyApplication
import com.yelogy.callbacks.GernalCallBack
import com.yelogy.signup.SignupResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoginRepository(val context: Context) : GernalCallBack by context as GernalCallBack{


    private var disposable: Disposable? = null
     val loginResponse =  MutableLiveData<SignupResponse>()

    fun onLoginApiCalling(loginRequest: LoginRequest){
        if(NetworkUtils.isConnected()){
            showProgressBar()
            val json = Gson().toJson(loginRequest)// obj is your object

            val result =  Gson().fromJson<Map<String,String>>(json, Map::class.java)

            val signUpObservable = YelogyApplication.getInstance().getRequestQueue().create(YelogyApi::class.java).login(result)
            disposable = signUpObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    hideProgressBar()
                    ToastUtils.showShort(result.message)
                    if(result.status==true)
                    {
                        loginResponse.value=result
                        Prefs.getInstance(context).setPreferenceStringData(Prefs.KEY_USER_Detail,Gson().toJson(result.data))
                    }
                    /* hide progress bar*/

                }, { error ->
                    hideProgressBar()
                    Log.e("TAG", "{$error.message}")
                }
                )
        }

    }

}