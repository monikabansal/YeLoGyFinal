package com.yelogy.signup

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yelogy.MainActivity
import com.yelogy.R
import com.yelogy.base.BaseActivity
import com.yelogy.databinding.ActivitySignupBinding
import com.yelogy.utill.InjectorUtils
import kotlinx.android.synthetic.main.app_bar_main2.*

class SignupActivity : BaseActivity() {
    override fun hasToolBar(): Boolean {
        return true
    }

    lateinit var mViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewModel()
        val binding: ActivitySignupBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        binding.viewModel=mViewModel
        toolbarTitle.text=getString(R.string.register)

        subscribeLoginResponse()
    }

    private fun setViewModel(){
        val factory= InjectorUtils.provideSignupViewModelFactory(this)
         mViewModel = ViewModelProviders.of(this, factory).get(SignupViewModel::class.java)
    }

    private fun subscribeLoginResponse(){
        // Create the observer which updates the UI.

                // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
                mViewModel.signupResponse.observe(this,  Observer<SignupResponse> { signupResponse ->
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
