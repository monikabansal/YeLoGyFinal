package com.yelogy.deliveryaddress

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NearByStoreRequest() {



    @SerializedName("latitude")
    @Expose
     var latitude: String ? = null

    @SerializedName("longitude")
    @Expose
    var longitude: String ? = null


    @SerializedName("pincode")
    @Expose
    var pincode: String ? = null



}