package com.yelogy.application

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.BitmapDrawable
import android.support.v4.content.ContextCompat
import com.blankj.utilcode.util.Utils
import com.yelogy.apiclient.YelogyApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit

class YelogyApplication : Application() {

    private var retrofit: Retrofit? = null

    companion object {

        lateinit var yelogyApplication: YelogyApplication
        fun getInstance(): YelogyApplication {
            return yelogyApplication
        }
    }


    override fun onCreate() {
        super.onCreate()
        yelogyApplication = this
        Utils.init(this)
    }



    fun getRequestQueue(): Retrofit {

        if (retrofit == null) {
            val logging = HttpLoggingInterceptor()
            // set your desired log level
            logging.level = HttpLoggingInterceptor.Level.BODY

            val builder = OkHttpClient.Builder()
            val okHttpClient = builder.connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(YelogyApi.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }
        return retrofit as Retrofit
    }


}