package com.yelogy.signup

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.yelogy.MainActivity
import com.yelogy.R
import com.yelogy.base.BaseActivity
import com.yelogy.databinding.ActivitySignupBinding
import com.yelogy.utill.InjectorUtils
import kotlinx.android.synthetic.main.app_bar_main2.*
import java.util.*

class SignupActivity : BaseActivity(), LocationListener {

    override fun hasToolBar(): Boolean {
        return true
    }

    lateinit var mViewModel: SignupViewModel
    private var locationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        setViewModel()
        val binding: ActivitySignupBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        binding.viewModel = mViewModel
        toolbarTitle.text = getString(R.string.register)
        subscribeLoginResponse()
        askForLocationPermission()
    }

    private fun setViewModel() {
        val factory = InjectorUtils.provideSignupViewModelFactory(this)
        mViewModel = ViewModelProviders.of(this, factory).get(SignupViewModel::class.java)
    }

    private fun subscribeLoginResponse() {
        // Create the observer which updates the UI.

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mViewModel.signupResponse.observe(this, Observer<SignupResponse> { signupResponse ->
            // Update the UI, in this case, a TextView.
            if (signupResponse?.status == true) {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()

            }
        })
    }

    private fun askForLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        ), 10
                )
            }
        }

        if (isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            if (locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {

                onLocationChanged(locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER))

            } else {
                locationManager!!.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME,
                        MIN_DISTANCE,
                        this
                )
                locationManager!!.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME, MIN_DISTANCE,
                        this
                )
            }

        } else {

            showGpsRequest()
        }

    }


    private fun isProviderEnabled(provider: String): Boolean {
        Log.d("isProviderEnabled", ">>isProviderEnabled")

        if (locationManager != null) {
            if (locationManager!!.isProviderEnabled(provider)) {
                return true
            }
        }

        return false
    }


    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    override fun onLocationChanged(location: Location?) {
        location?.let {
            val geocoder = Geocoder(this, Locale.ENGLISH);
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);

            if (addresses != null && addresses.isNotEmpty()) {
                val zipcode = addresses[0].postalCode
                mViewModel.signUpRequest.get()!!.pincode=zipcode
                mViewModel.signUpRequest.notifyChange()
            }

                locationManager?.removeUpdates(this)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 10 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                askForLocationPermission()
       }
    }


}
