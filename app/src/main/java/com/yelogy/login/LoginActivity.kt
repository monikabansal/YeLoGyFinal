package com.yelogy.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yelogy.MainActivity
import com.yelogy.R
import com.yelogy.base.BaseActivity
import com.yelogy.databinding.ActivityLoginBinding
import com.yelogy.databinding.ActivitySignupBinding
import com.yelogy.signup.SignupActivity
import com.yelogy.signup.SignupResponse
import com.yelogy.signup.SignupViewModel
import com.yelogy.utill.InjectorUtils

class LoginActivity : BaseActivity() {
    override fun hasToolBar(): Boolean {
        return false
    }

    lateinit var mViewModel: LoginViewModel
    lateinit var  binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewModel()
         binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel=mViewModel
        subscribeLoginResponse()
        setClicks()
    }

    private fun setClicks() {
        binding.dontHaveAccountTv.setOnClickListener{
            val i=Intent(this,SignupActivity::class.java)
            startActivity(i)
        }
    }

    private fun setViewModel(){
        val factory= InjectorUtils.provideLoginViewModelFactory(this)
         mViewModel = ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)
    }

    private fun subscribeLoginResponse(){
        // Create the observer which updates the UI.
        val nameObserver =

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mViewModel.loginResponse.observe(this,  Observer<SignupResponse> { signupResponse ->
            // Update the UI, in this case, a TextView.
          if( signupResponse?.status==true){
              val intent = Intent(this, MainActivity::class.java)
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
              startActivity(intent)
              finish()

          }
        })
    }


}