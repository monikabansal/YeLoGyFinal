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
    var data: List<Datum>? = null

    class Datum {
        @SerializedName("id")
        @Expose
         val id: Int? = null
        @SerializedName("name")
        @Expose
         val name: String? = null
        @SerializedName("latitude")
        @Expose
         val latitude: Any? = null
        @SerializedName("longitude")
        @Expose
         val longitude: Any? = null
        @SerializedName("email")
        @Expose
         val email: String? = null
        @SerializedName("first_name")
        @Expose
         val firstName: String? = null
        @SerializedName("last_name")
        @Expose
         val lastName: String? = null
        @SerializedName("mobile")
        @Expose
         val mobile: String? = null
        @SerializedName("pincode")
        @Expose
         val pincode: Int? = null
        @SerializedName("status")
        @Expose
         val status: String? = null
        @SerializedName("deleted_at")
        @Expose
         val deletedAt: Any? = null
        @SerializedName("created_at")
        @Expose
         val createdAt: String? = null
        @SerializedName("updated_at")
        @Expose
         val updatedAt: String? = null
    }
}



