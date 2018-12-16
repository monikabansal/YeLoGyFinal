package com.yelogy.base

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.KeyboardUtils
import com.yelogy.R
import com.yelogy.callbacks.GernalCallBack
import kotlinx.android.synthetic.main.app_bar_main2.*

abstract class BaseActivity : AppCompatActivity(), GernalCallBack {

    private var progressBar: View? = null
     val MIN_TIME: Long = 400
     val MIN_DISTANCE = 1000f
    override fun hideKeyboard() {
        KeyboardUtils.hideSoftInput(this)
    }

    override fun hideProgressBar() {
        runOnUiThread {
            progressBar?.visibility = View.GONE
        }
    }

    override fun showProgressBar() {
        runOnUiThread {
            progressBar?.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onStart() {
        super.onStart()
        if (hasToolBar()) {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    abstract fun hasToolBar(): Boolean

    fun init() {
        progressBar = layoutInflater.inflate(R.layout.progress_dialog_loading, null) as View
        val v = this.findViewById<View>(android.R.id.content).rootView
        val viewGroup = v as ViewGroup
        viewGroup.addView(progressBar)
    }

    fun showGpsRequest() {

        val mAlertDialog = AlertDialog.Builder(this)
        mAlertDialog.setTitle(R.string.location_service_disabled_title)
                .setMessage(R.string.location_service_disabled_text)
                .setPositiveButton(R.string.enable) { _, _ ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }.show()
    }
}