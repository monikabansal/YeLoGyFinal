package com.yelogy.category.reuestresponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class CategoryListResponse {

    @SerializedName("status")
    @Expose
     val status: Boolean? = null
    @SerializedName("message")
    @Expose
     val message: String? = null
    @SerializedName("data")
    @Expose
     val data: List<Datum>? = null



    inner class Datum {

        @SerializedName("id")
        @Expose
        var id: Int? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("children")
        @Expose
        var children: List<Child>? = null

    }

    inner class Child {

        @SerializedName("id")
        @Expose
         val id: Int? = null
        @SerializedName("name")
        @Expose
         val name: String? = null
        @SerializedName("user_id")
        @Expose
         val userId: Int? = null
        @SerializedName("parent_category_id")
        @Expose
         val parentCategoryId: Int? = null

    }

}


