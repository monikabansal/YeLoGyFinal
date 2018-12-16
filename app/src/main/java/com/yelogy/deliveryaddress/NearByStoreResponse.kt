package com.yelogy.deliveryaddress

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NearByStoreResponse {

    @SerializedName("status")
    @Expose
    var status: Boolean? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("data")
    @Expose
    var data: List<Any>? = null

}



