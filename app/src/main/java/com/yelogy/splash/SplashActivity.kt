package com.yelogy.splash

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.os.drewel.prefrences.Prefs
import com.yelogy.deliveryaddress.DeliveryAddessPickupActivity
import com.yelogy.MainActivity
import com.yelogy.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        callNextActivity()
    }


    private fun callNextActivity() {
        //   var compositeDisposable: CompositeDisposable? = CompositeDisposable()
        disposable = Observable.timer(3000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            if(Prefs.getInstance(this).getPreferenceStringData(Prefs.KEY_DELIVERY_ADDRESS).isEmpty())
                                startActivity(Intent(this, DeliveryAddessPickupActivity::class.java))

                            else
                                startActivity(Intent(this, MainActivity::class.java))

                            finish()
                        },
                        { error -> Log.e("TAG", "{$error.message}") },
                        { Log.d("TAG", "completed") })
    }


    override fun onDestroy() {
        super.onDestroy()
        if (disposable != null)
            disposable.dispose()
    }
}
