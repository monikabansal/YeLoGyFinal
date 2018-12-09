package com.yelogy.useraccount

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.yelogy.R
import android.databinding.DataBindingUtil
import android.support.v7.app.AlertDialog
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.os.drewel.prefrences.Prefs
import com.yelogy.DeliveryAddessPickupActivity
import com.yelogy.MainActivity
import com.yelogy.databinding.FragmentUserAccountBinding
import com.yelogy.login.LoginActivity
import com.yelogy.signup.SignupResponse
import com.yelogy.utill.Utills
import kotlinx.android.synthetic.main.fragment_user_account.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [UserAccountFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [UserAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserAccountFragment : Fragment() {

    lateinit var mViewModel: MyAccountViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val binding = DataBindingUtil.inflate<FragmentUserAccountBinding>(
                inflater, R.layout.fragment_user_account, container, false)
        binding.viewModel = mViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginTv.setOnClickListener {
            onLoginClick()
        }

        tv_sign_out.setOnClickListener{
            showLogoutDialog()
        }
        changeAddressBt.setOnClickListener{
            val i=Intent(requireContext(),DeliveryAddessPickupActivity::class.java)
            startActivity(i)
        }
    }


    fun setViewModel() {

        mViewModel = ViewModelProviders.of(this).get(MyAccountViewModel::class.java)
        mViewModel.userData.set(Utills.getLoginResponse(requireContext()))
        mViewModel.userAddress.set(Prefs.getInstance(requireContext()).getPreferenceStringData(Prefs.KEY_DELIVERY_ADDRESS))
    }


    fun onLoginClick() {
        val intent = Intent(requireActivity(), LoginActivity::class.java);
        startActivity(intent)

    }

    private fun showLogoutDialog() {

        val logoutAlertDialog = AlertDialog.Builder(this.context!!, R.style.DeliveryTypeTheme).create()

        logoutAlertDialog.setTitle(getString(R.string.app_name))
        logoutAlertDialog.setMessage(getString(R.string.want_to_logout))


        logoutAlertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), DialogInterface.OnClickListener { dialog, id ->

            logoutAlertDialog.dismiss()
            Prefs.getInstance(requireContext()).setPreferenceStringData(Prefs.KEY_USER_Detail,"")
            val intent = Intent(activity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity!!.finish()
        })

        logoutAlertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no), DialogInterface.OnClickListener { dialog, id ->

            logoutAlertDialog.dismiss()

        })

        logoutAlertDialog.show()
    }


}
