package com.yelogy.deliveryaddress

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.Gravity
import android.view.View
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
import com.yelogy.MainActivity
import com.yelogy.R
import com.yelogy.base.BaseActivity
import com.yelogy.base.BaseRepository
import com.yelogy.utill.AppRequestCodes
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_location_picker.*
import kotlinx.android.synthetic.main.app_bar_main2.*
import java.util.*

class DeliveryAddessPickupActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraIdleListener,
        LocationListener {
    override fun hasToolBar(): Boolean {
        return true
    }


    private var googleMap: GoogleMap? = null
    private var currentLocationMarker: Marker? = null
    private var locationManager: LocationManager? = null
    private lateinit var baseRepositry: BaseRepository
    private var isFromSearch = false

    private var latitude=0.0
    private var longitude=0.0
    private var pincode=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_picker)
        baseRepositry = BaseRepository(this)
        toolbarTitle.text = getString(R.string.location)
        intializeGUI(savedInstanceState)

        searchDeliveryAddress.setOnClickListener {
            try {
                val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this)
                startActivityForResult(intent, AppRequestCodes.PLACE_AUTOCOMPLETE_REQUEST_CODE)
            } catch (e: GooglePlayServicesRepairableException) {
                ToastUtils.showShort(e.message)
            } catch (e: GooglePlayServicesNotAvailableException) {
                ToastUtils.showShort(e.message)
            }
        }

        mapDoneButton.setOnClickListener {
            Prefs.getInstance(this).setPreferenceStringData(Prefs.KEY_DELIVERY_ADDRESS, deliveryAddresstxt.text.toString())
            Prefs.getInstance(this).setPreferenceStringData(Prefs.KEY_DELIVERY_LAT, latitude.toString())
            Prefs.getInstance(this).setPreferenceStringData(Prefs.KEY_DELIVERY_LANG, longitude.toString())
            Prefs.getInstance(this).setPreferenceStringData(Prefs.KEY_DELIVERY_PIN_CODE, pincode)

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

        this.googleMap!!.uiSettings.isCompassEnabled = false
        this.googleMap!!.uiSettings.isMapToolbarEnabled = false
        this.googleMap!!.uiSettings.isTiltGesturesEnabled = false
        this.googleMap!!.uiSettings.isZoomControlsEnabled = false
        this.googleMap!!.uiSettings.isRotateGesturesEnabled = false


        //        this.googleMap.setOnMarkerClickListener(this);
        this.googleMap!!.setOnCameraMoveStartedListener(this)
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
            this.googleMap!!.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
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

    override fun onCameraMoveStarted(p0: Int) {
        addressll.visibility = View.GONE
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
        locationManager?.removeUpdates(this)

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
            this.googleMap?.isMyLocationEnabled = true
            this.googleMap?.uiSettings?.isMyLocationButtonEnabled = true
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
                            if (addresses.size > 0) {

                                val address = addresses[0]
                                var fullAddress = ""

                                Log.d("addresses", ">>" + Arrays.toString(addresses.toTypedArray()))
                                Log.d("addresses", ">>" + Arrays.toString(addresses.toTypedArray()))
                                val complete_address = addresses[0].getAddressLine(0)
                                if (isFromSearch)
                                    isFromSearch = false
                                else
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

                                checkLocation(address.latitude, address.longitude, address.postalCode)
//
                            }
                        },
                        { error ->
                            // hideLoading()
                            Log.e("TAG", "{$error.message}")
                        },
                        { Log.d("TAG", "completed") }
                )
    }

    private fun checkLocation(latitude: Double, longitude: Double, pincode: String) {
        val nearByRequst = NearByStoreRequest()
        nearByRequst.pincode = pincode
        nearByRequst.latitude = latitude.toString()
        nearByRequst.longitude = longitude.toString()

        this.latitude=latitude
        this.longitude=longitude
        this.pincode=pincode

        val nearByStoreResponse = baseRepositry.getNearByStores(nearByRequst)

        nearByStoreResponse.observe(this@DeliveryAddessPickupActivity, Observer<NearByStoreResponse>
        { reseponse ->

            if (reseponse?.status == true) {
                addressll.visibility = View.VISIBLE
            } else {
                addressll.visibility = View.VISIBLE
                ToastUtils.showLong("Soory, we do not serve in this area.\nPlease select another area.")
            }


        })
    }


    var place: Place? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == AppRequestCodes.PLACE_AUTOCOMPLETE_REQUEST_CODE) {

            when (resultCode) {
                Activity.RESULT_OK -> {
                    isFromSearch = true
                    place = PlaceAutocomplete.getPlace(this, data!!)

                    Log.i("onActivityResult", "Place: " + place!!.name)
                    Log.i("onActivityResult", "Place: " + place!!.address)
                    Log.i("onActivityResult", "Place: " + place!!.latLng)

                    val latLng = LatLng(place!!.latLng.latitude, place!!.latLng.longitude)
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14f)
                    googleMap!!.animateCamera(cameraUpdate)
                    deliveryAddresstxt.text = place!!.address

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


}