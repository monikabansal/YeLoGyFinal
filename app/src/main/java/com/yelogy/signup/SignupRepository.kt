package com.yelogy.signup

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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SignupRepository(val context:Context): GernalCallBack by context as GernalCallBack{

    val signupResponse =  MutableLiveData<SignupResponse>()

    private var disposable: Disposable? = null

    fun onSignupApiCalling(signUpRequest: SignUpRequest){
        if(NetworkUtils.isConnected()){
            showProgressBar()
            val json = Gson().toJson(signUpRequest)// obj is your object

            val result =  Gson().fromJson<Map<String,String>>(json, Map::class.java)

            val signUpObservable = YelogyApplication.getInstance().getRequestQueue().create(YelogyApi::class.java).signup(result)
            disposable = signUpObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    hideProgressBar()
                    ToastUtils.showShort(result.message)
                    if(result.status==true)
                    {
                        signupResponse.value=result
                        Prefs.getInstance(context).setPreferenceStringData(Prefs.KEY_USER_Detail,Gson().toJson(result.data))
                    }

                }, { error ->

                    hideProgressBar()
                    Log.e("TAG", "{$error.message}")
                }
                )
        }

    }

}