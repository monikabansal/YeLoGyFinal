package com.yelogy.category

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.os.drewel.adapter.StoreAdapter
import com.yelogy.R
import com.yelogy.base.BaseActivity
import com.yelogy.category.adapter.CategoryAdapter
import com.yelogy.category.reuestresponse.CategoryListResponse
import com.yelogy.databinding.ActivityCategoryBinding
import com.yelogy.utill.AppIntentExtrakeys.STORE_ID
import com.yelogy.utill.GridDividerItemDecoration
import com.yelogy.utill.InjectorUtils
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.app_bar_main2.*
import kotlinx.android.synthetic.main.fragment_home.*

class CategoryActivity : BaseActivity() {
    lateinit var mViewModel: CategoryViewModel
    private var categoryAdapter: CategoryAdapter? = null

    override fun hasToolBar(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewModel()
        val storeId = intent.getStringExtra(STORE_ID) ?: ""
        mViewModel.getCategroyies(storeId)
        val binding: ActivityCategoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_category)
        //binding.categoryViewModel = mViewModel
        toolbarTitle.text = getString(R.string.shop_by_category)
        subscribeLoginResponse()
        // setCategoryListAdapter(categoryListResponse.data)
    }

    private fun setCategoryListAdapter(categoryData: List<CategoryListResponse.Datum>?) {
        if (categoryAdapter == null) {
            val dra=ContextCompat.getDrawable(this, R.drawable.item_divider)!!
            val itemDecorator = GridDividerItemDecoration(dra,dra,2)

            val storeAdapter = CategoryAdapter(this, categoryData!!)
            categoryRv.layoutManager = GridLayoutManager(this,2)
            categoryRv.adapter = storeAdapter
            //categoryRv.addItemDecoration(itemDecorator)
        } else {
            categoryAdapter?.notifyDataSetChanged()
        }
    }


    private fun setViewModel() {

        val factory = InjectorUtils.provideCategoryViewModelFactory(this)
        mViewModel = ViewModelProviders.of(this, factory).get(CategoryViewModel::class.java)
    }

    private fun subscribeLoginResponse() {
        mViewModel.mCategoryListResponse.observe(this, Observer<CategoryListResponse> { categoryListResponse ->
            // Update the UI, in this case, a TextView.
            if (categoryListResponse?.status == true) {
                setCategoryListAdapter(categoryListResponse.data)
            }
        })
    }


}
