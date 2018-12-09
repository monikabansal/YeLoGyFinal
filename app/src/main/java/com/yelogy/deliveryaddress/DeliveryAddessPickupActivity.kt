package com.yelogy

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.blankj.utilcode.util.ToastUtils
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.os.drewel.prefrences.Prefs
import com.yelogy.base.BaseActivity
import com.yelogy.utill.AppRequestCodes
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_location_picker.*
import kotlinx.android.synthetic.main.app_bar_main2.*
import java.util.*

class DeliveryAddessPickupActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraIdleListener,
        LocationListener {
    override fun hasToolBar(): Boolean {
        return true
    }


    private var googleMap: GoogleMap? = null
    private var currentLocationMarker: Marker? = null
    private var locationManager: LocationManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_picker)

        toolbarTitle.text=getString(R.string.location)
        intializeGUI(savedInstanceState)

        searchDeliveryAddress.setOnClickListener{
            try {
                val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this)
                startActivityForResult(intent, AppRequestCodes.PLACE_AUTOCOMPLETE_REQUEST_CODE)
            } catch (e: GooglePlayServicesRepairableException) {
                ToastUtils.showShort(e.message)
            } catch (e: GooglePlayServicesNotAvailableException) {
                ToastUtils.showShort(e.message)
            }
        }

        mapDoneButton.setOnClickListener{
            Prefs.getInstance(this).setPreferenceStringData(Prefs.KEY_DELIVERY_ADDRESS,deliveryAddresstxt.text.toString())
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }


    }

    private fun intializeGUI(savedInstanceState: Bundle?) {


        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        mMapView!!.onCreate(savedInstanceState)
        mMapView!!.getMapAsync(this)


    }

    public override fun onResume() {
        super.onResume()
        mMapView!!.onResume()

        if (!isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGpsRequest()
        }
    }

    public override fun onPause() {
        super.onPause()
        mMapView!!.onPause()
    }


    public override fun onDestroy() {
        super.onDestroy()
        mMapView!!.onDestroy()
        locationManager!!.removeUpdates(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView!!.onLowMemory()
    }

    public override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mMapView!!.onSaveInstanceState(outState)
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

    private fun showGpsRequest() {

        val mAlertDialog = AlertDialog.Builder(this)
        mAlertDialog.setTitle(R.string.location_service_disabled_title)
                .setMessage(R.string.location_service_disabled_text)
                .setPositiveButton(R.string.enable) { _, _ ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }.show()
    }

    override fun onMapReady(googleMap: GoogleMap) {

        val ivPin = ImageView(this)
        ivPin.setImageResource(R.mipmap.map_icon)
        val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
        )
        val density = resources.displayMetrics.density
        params.bottomMargin = (12 * density).toInt()
        ivPin.layoutParams = params
        mMapView!!.addView(ivPin)

        this.googleMap = googleMap

        this.googleMap!!.isBuildingsEnabled = false
        this.googleMap!!.isTrafficEnabled = false
        //this.googleMap!!.isMyLocationEnabled = true
        this.googleMap!!.uiSettings.isCompassEnabled = false
        this.googleMap!!.uiSettings.isMapToolbarEnabled = false
        this.googleMap!!.uiSettings.isTiltGesturesEnabled = false
        this.googleMap!!.uiSettings.isZoomControlsEnabled = false
        this.googleMap!!.uiSettings.isRotateGesturesEnabled = false

        //        this.googleMap.setOnMarkerClickListener(this);
        this.googleMap!!.setOnCameraMoveListener(this)
        this.googleMap!!.setOnCameraIdleListener(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
                ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                requestPermissions(
                        arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        ), 10
                )
            }

        } else {

            if (isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                if (locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {

                    //                mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    onLocationChanged(locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER))

                } else {
                    // showLoading()
                    locationManager!!.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME,
                            MIN_DISTANCE,
                            this
                    ) //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
                    locationManager!!.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME,
                            MIN_DISTANCE,
                            this
                    ) //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
                }

            } else {

                showGpsRequest()
            }
        }
    }


    override fun onCameraIdle() {

        Log.d("---------onCameraIdle", "------------------------")
        Log.d("latitude", ">>" + this.googleMap!!.cameraPosition.target.latitude)
        Log.d("longitude", ">>" + this.googleMap!!.cameraPosition.target.longitude)
        Log.d("zoom", ">>" + this.googleMap!!.cameraPosition.zoom)
        Log.d("--------------------", "------------------------")


        val lat = this.googleMap?.getCameraPosition()?.target?.latitude ?: 0.0
        val longit = this.googleMap?.getCameraPosition()?.target?.longitude ?: 0.0
        val latLng = LatLng(lat, longit)
        geoLocationAddressAPI(latLng)
    }

    override fun onCameraMove() {

//        Log.d("---------onCameraMove", "------------------------");
//        Log.d("latitude", ">>" + this.googleMap?.getCameraPosition()?.target?.latitude);
//        Log.d("longitude", ">>" + this.googleMap?.getCameraPosition()?.target?.longitude);
//        Log.d("zoom", ">>" + this.googleMap?.getCameraPosition()?.zoom);
//
//
//        Log.d("--------------------", "------------------------");
    }

    override fun onLocationChanged(location: Location) {

        // hideLoading()
        val latLng = LatLng(location.latitude, location.longitude)

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14f)

        Log.d("latitude", ">>" + this.googleMap!!.cameraPosition.target.latitude)
        Log.d("longitude", ">>" + this.googleMap!!.cameraPosition.target.longitude)
        Log.d("zoom", ">>" + this.googleMap!!.cameraPosition.zoom)

        if (currentLocationMarker != null) {
            currentLocationMarker!!.remove()
        }

        /*   currentLocationMarker = this.googleMap!!.addMarker(MarkerOptions().position(latLng)
                   .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)).anchor(0.5f, 0.5f))*/
        googleMap!!.animateCamera(cameraUpdate)
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }


    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 10 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            locationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME,
                    MIN_DISTANCE,
                    this
            ) //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
            locationManager!!.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME,
                    MIN_DISTANCE,
                    this
            ) //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
        }
    }

    private fun geoLocationAddressAPI(latLng: LatLng) {

        Observable.fromCallable {
            val geocoder = Geocoder(this, Locale.ENGLISH)
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        }.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { addresses ->
                            if (/*addresses != null && */addresses.size > 0) {

                                val address = addresses[0]
                                var fullAddress = ""

                                Log.d("addresses", ">>" + Arrays.toString(addresses.toTypedArray()))
                                Log.d("addresses", ">>" + Arrays.toString(addresses.toTypedArray()))
                                val complete_address = addresses[0].getAddressLine(0)
                                deliveryAddresstxt.text = complete_address
                                for (i in 0 until address.maxAddressLineIndex + 1) {
                                    Log.d("getAddressLine$i", ">>" + address.getAddressLine(i))
                                    if (i == 0)
                                        fullAddress = address.getAddressLine(i)
                                    else
                                        break
                                }
                                val subThoroughfare: String
                                val thoroughfare: String
                                val subLocality: String
                                val locality: String

                                subThoroughfare = if (address.subThoroughfare != null) address.subThoroughfare + " " else ""

                                thoroughfare = if (address.thoroughfare != null) address.thoroughfare + " " else ""

                                subLocality = if (address.subLocality != null) address.subLocality + " " else ""

                                locality = if (address.locality != null) address.locality else ""

                                val name = subThoroughfare + thoroughfare + subLocality + locality
                                // hideLoading()

                                checkAddress(intent, latLng)
//                                startActivity(intent)
//                                finish()
                            } else {
                                //hideLoading()
                                // Utils.getInstance().showToast(this, getString(R.string.error_address_not_found))
                            }
                        },
                        { error ->
                            // hideLoading()
                            Log.e("TAG", "{$error.message}")
                        },
                        { Log.d("TAG", "completed") }
                )
    }

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private fun checkAddress(intent: Intent, latLng: LatLng) {

//        val addDeliveryAddressRequest = HashMap<String, String>()
//        addDeliveryAddressRequest["user_id"] = pref!!.getPreferenceStringData(pref!!.KEY_USER_ID)
//        addDeliveryAddressRequest["latitude"] = latLng.latitude.toString()
//        addDeliveryAddressRequest["longitude"] = latLng.longitude.toString()
//        addDeliveryAddressRequest["language"] = DrewelApplication.getInstance().getLanguage()
//        val signUpObservable = DrewelApplication.getInstance().getRequestQueue().create(DrewelApi::class.java).checkAddress(addDeliveryAddressRequest)
//        compositeDisposable.add(signUpObservable.subscribeOn(Schedulers.newThread())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ result ->
//                hideLoading()
////                    DrewelApplication.getInstance().logoutWhenAccountDeactivated(result.response!!.isDeactivate!!, this)
//                if (result.response!!.status!!) {
//                    startActivity(intent)
//                    finish()
//                } else {
//                    Utils.getInstance().showToast(this, result.response!!.message!!)
//                }
//            }, { error ->
//                hideLoading()
//                Utils.getInstance().showToast(this, error.message!!)
//                Log.e("TAG", "{$error.message}")
//            }
//            ))
    }

    var place: Place? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == AppRequestCodes.PLACE_AUTOCOMPLETE_REQUEST_CODE) {

            when (resultCode) {
                Activity.RESULT_OK -> {

                    place = PlaceAutocomplete.getPlace(this, data!!)

                    Log.i("onActivityResult", "Place: " + place!!.name)
                    Log.i("onActivityResult", "Place: " + place!!.address)
                    Log.i("onActivityResult", "Place: " + place!!.latLng)

                    val latLng = LatLng(place!!.latLng.latitude, place!!.latLng.longitude)
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14f)
                    googleMap!!.animateCamera(cameraUpdate)
                    searchDeliveryAddress.text = place!!.name

                }
                PlaceAutocomplete.RESULT_ERROR -> {

                    val status = PlaceAutocomplete.getStatus(this, data!!)
                    Log.i("onActivityResult", status.statusMessage)

                }
                Activity.RESULT_CANCELED -> // The user canceled the operation.
                    Log.e("onActivityResult", "canceled")
            }
        }
    }

    companion object {

        private const val MIN_TIME: Long = 400
        private const val MIN_DISTANCE = 1000f
    }

}