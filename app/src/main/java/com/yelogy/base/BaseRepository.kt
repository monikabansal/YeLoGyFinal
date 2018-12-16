package com.yelogy.base

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
import com.yelogy.deliveryaddress.NearByStoreRequest
import com.yelogy.deliveryaddress.NearByStoreResponse
import com.yelogy.login.LoginRequest
import com.yelogy.signup.SignupResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Maani on 11-12-2018.
 */
open class BaseRepository(private val context: Context) : GernalCallBack by context as GernalCallBack {

    private var disposable: Disposable? = null
    val nearByStoreResponse = MutableLiveData<NearByStoreResponse>()

    fun getNearByStores(nearByStoreRequest: NearByStoreRequest):LiveData<NearByStoreResponse> {
        if (NetworkUtils.isConnected()) {
            showProgressBar()
            val json = Gson().toJson(nearByStoreRequest)// obj is your object

            val result = Gson().fromJson<Map<String, String>>(json, Map::class.java)

            val signUpObservable = YelogyApplication.getInstance().getRequestQueue().create(YelogyApi::class.java).nearByStore(result)
            disposable = signUpObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        hideProgressBar()
                        nearByStoreResponse.value = result

                    }, { error ->
                        hideProgressBar()
                        Log.e("TAG", "{$error.message}")
                    }
                    )
        }
        return  nearByStoreResponse
    }


}