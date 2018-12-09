package com.yelogy

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.yelogy.base.BaseActivity
import com.yelogy.databinding.ActivityMainBinding
import com.yelogy.home.HomeFragment
import com.yelogy.useraccount.UserAccountFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main2.*

class MainActivity : BaseActivity() {
    override fun hasToolBar(): Boolean {
        return false
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_account -> {
                toolbarTitle.text=getString(R.string.my_account)
                setFragment(UserAccountFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_home -> {
                toolbarTitle.text=getString(R.string.app_name)
                setFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowHomeEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitle.text=getString(R.string.app_name)
        setFragment(HomeFragment())
    }


    private fun setFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
    }



}
