package com.yelogy.home


import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.os.drewel.adapter.StoreAdapter
import com.yelogy.DeliveryAddessPickupActivity

import com.yelogy.R
import com.yelogy.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
                inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStoreAdapter()
        setHomeBannerAdapter()
    }



    private fun setHomeBannerAdapter() {
        bannerPager.adapter = SlidingImageAdapterHomeBanner(activity!!)
        pageIndicatorView.setViewPager(bannerPager)

    }

    private fun setStoreAdapter(){
        val storeAdapter=StoreAdapter(requireContext())
        recyclerView.layoutManager=LinearLayoutManager(requireContext())
        recyclerView.adapter=storeAdapter
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.address -> {
               val i= Intent(requireContext(),DeliveryAddessPickupActivity::class.java)
                startActivity(i)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }



}// Required empty public constructor
