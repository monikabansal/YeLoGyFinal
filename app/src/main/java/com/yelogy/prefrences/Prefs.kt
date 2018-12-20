package com.os.drewel.prefrences

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by monikab on 3/14/2018.
 */
class Prefs(internal  var context: Context?) {

    private val PREFS_FILENAME = "com.yelogy.prefs"


    private val sharedPreferences: SharedPreferences = context!!.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);




    companion object {

        var prefs: Prefs? = null

        fun getInstance(context: Context?): Prefs {
            if (prefs == null) {
                prefs = Prefs(context)
            }
            return prefs as Prefs
        }


        val KEY_USER_Detail = "KEY_USER_Detail"
        val KEY_DELIVERY_ADDRESS = "KEY_DELIVERY_ADDRESS"
        val KEY_DELIVERY_LAT = "KEY_DELIVERY_LAT"
        val KEY_DELIVERY_LANG = "KEY_DELIVERY_LANG"
        val KEY_DELIVERY_PIN_CODE = "KEY_DELIVERY_PIN_CODE"

    }

    fun setPreferenceStringData(preferenceKey: String, preferenceData: String) {

        sharedPreferences.edit().putString(preferenceKey, preferenceData).apply()

    }

    fun setPreferenceIntData(preferenceKey: String, preferenceData: Int) {

        sharedPreferences.edit().putInt(preferenceKey, preferenceData).apply()

    }

    fun getPreferenceIntData(preferenceKey: String): Int {

        return sharedPreferences.getInt(preferenceKey, 0)
    }

    fun getPreferenceStringData(preferenceKey: String): String {

        return sharedPreferences.getString(preferenceKey, "")
    }


    fun setPreferenceBooleanData(preferenceKey: String, preferenceData: Boolean) {

        sharedPreferences.edit().putBoolean(preferenceKey, preferenceData).apply()
    }

    fun getPreferenceBooleanData(preferenceKey: String): Boolean {

        return sharedPreferences.getBoolean(preferenceKey, false)
    }
    fun clearSharedPreference() {


    }
}