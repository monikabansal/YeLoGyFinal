package com.yelogy.utill

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by monikab on 3/13/2018.
 */
object ValidationUtil {


    fun isEmailValid(email: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun isPhoneValid(phone: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PHONE_PATTERN = "^[0-9]{9,12}$"
        pattern = Pattern.compile(PHONE_PATTERN)
        matcher = pattern.matcher(phone)
        return matcher.matches()
    }
}