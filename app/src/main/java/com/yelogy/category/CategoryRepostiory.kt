package com.yelogy.category

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.util.Log
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils
import com.yelogy.apiclient.YelogyApi
import com.yelogy.application.YelogyApplication
import com.yelogy.callbacks.GernalCallBack
import com.yelogy.category.reuestresponse.CategoryListResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CategoryRepostiory(val context : Context): GernalCallBack by context as GernalCallBack {

    private var disposable: Disposable? =null
     val categoryResponse=MutableLiveData<CategoryListResponse>()

    fun getCategoryList(storeId: String) {
        if(NetworkUtils.isConnected()){
            showProgressBar()


            val catListObservable = YelogyApplication.getInstance().getRequestQueue().create(YelogyApi::class.java).categorySubCat(storeId)
            disposable = catListObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    hideProgressBar()
                    ToastUtils.showShort(result.message)
                    if(result.status==true)
                    {
                        categoryResponse.value=result
                       // Prefs.getInstance(context).setPreferenceStringData(Prefs.KEY_USER_Detail, Gson().toJson(result.data))
                    }

                }, { error ->

                    hideProgressBar()
                    Log.e("TAG", "{$error.message}")
                }
                )
        }

    }
    }
