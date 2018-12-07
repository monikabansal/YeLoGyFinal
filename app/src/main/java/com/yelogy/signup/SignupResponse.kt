package com.yelogy.signup

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SignupResponse {

    @SerializedName("status")
    @Expose
    var status: Boolean? = null
    @SerializedName("data")
    @Expose
    var data: Data? = null
    @SerializedName("message")
    @Expose
    var message: String? = null


    inner class Data {

        @SerializedName("first_name")
        @Expose
        var firstName: String? = null
        @SerializedName("last_name")
        @Expose
        var lastName: String? = null
        @SerializedName("mobile")
        @Expose
        var mobile: String? = null
        @SerializedName("email")
        @Expose
        var email: String? = null
        @SerializedName("pincode")
        @Expose
        var pincode: String? = null
        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null
        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null
        @SerializedName("id")
        @Expose
        var id: Int? = null
        @SerializedName("roles")
        @Expose
        var roles: List<Role>? = null


        inner class Pivot {

            @SerializedName("model_id")
            @Expose
            var modelId: Int? = null
            @SerializedName("role_id")
            @Expose
            var roleId: Int? = null
            @SerializedName("model_type")
            @Expose
            var modelType: String? = null

        }


        inner class Role {

            @SerializedName("id")
            @Expose
            var id: Int? = null
            @SerializedName("name")
            @Expose
            var name: String? = null
            @SerializedName("guard_name")
            @Expose
            var guardName: String? = null
            @SerializedName("created_at")
            @Expose
            var createdAt: String? = null
            @SerializedName("updated_at")
            @Expose
            var updatedAt: String? = null
            @SerializedName("pivot")
            @Expose
            var pivot: Pivot? = null

        }

    }
}