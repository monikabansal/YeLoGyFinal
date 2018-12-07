package com.yelogy.signup

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SignUpRequest() {


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
    @SerializedName("email")
    @Expose
     var email: String ? = null
    @SerializedName("first_name")
    @Expose
     var firstName: String ? = null
    @SerializedName("last_name")
    @Expose
     var lastName: String ? = null
    @SerializedName("mobile")
    @Expose
     var mobileNumber: String ? = null
    @SerializedName("pincode")
    @Expose
     var pincode: String ? = null
    @SerializedName("password")
    @Expose
     var password: String ? = null

    var confirmPassword: String ? = null


}
