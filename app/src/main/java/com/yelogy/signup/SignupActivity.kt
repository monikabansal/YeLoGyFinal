package com.yelogy.signup

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yelogy.R
import com.yelogy.base.BaseActivity
import com.yelogy.databinding.ActivitySignupBinding
import com.yelogy.utill.InjectorUtils

class SignupActivity : AppCompatActivity() {

    lateinit var mViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewModel()
        val binding: ActivitySignupBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        binding.viewModel=mViewModel
    }

    private fun setViewModel(){
        val factory= InjectorUtils.provideSignupViewModelFactory(this)
         mViewModel = ViewModelProviders.of(this, factory).get(SignupViewModel::class.java)
    }
}
