package com.yelogy.deliveryaddress

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.util.Log
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.os.drewel.prefrences.Prefs
import com.yelogy.apiclient.YelogyApi
import com.yelogy.application.YelogyApplication
import com.yelogy.base.BaseRepository
import com.yelogy.callbacks.GernalCallBack
import com.yelogy.login.LoginRequest
import com.yelogy.signup.SignupResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DeliveryAddressRepository(val context: Context):BaseRepository(context)  {




}