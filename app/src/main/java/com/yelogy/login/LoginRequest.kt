package com.yelogy.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginRequest() {


    /*first_name:virender
    last_name:saini
    email:virender@gmail.com
    phone_number:9898989898
    pincode:303105
    password:123456*/

    @SerializedName("device_id")
    @Expose
     var deviceId: String? = null
    @SerializedName("device_type")
    @Expose
     var deviceType: String ? = null

    @SerializedName("mobile")
    @Expose
     var mobileNumber: String ? = null

    @SerializedName("password")
    @Expose
     var password: String ? = null



}