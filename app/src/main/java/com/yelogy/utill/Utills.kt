package com.yelogy.utill

import android.content.Context
import com.google.gson.Gson
import com.os.drewel.prefrences.Prefs
import com.yelogy.signup.SignupResponse

/**
 * Created by Maani on 08-12-2018.
 */
object Utills {

    fun getLoginResponse(context: Context): SignupResponse.Data? {

        val userDataStr = Prefs.getInstance(context).getPreferenceStringData(Prefs.KEY_USER_Detail)
        val userData = Gson().fromJson<SignupResponse.Data>(userDataStr, SignupResponse.Data::class.java)
        return userData

    }
}