package com.yelogy.category

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.yelogy.category.reuestresponse.CategoryListResponse

class CategoryViewModel(private val categoryRepostiory: CategoryRepostiory) : ViewModel() {

    val mCategoryListResponse= categoryRepostiory.categoryResponse


    fun getCategroyies(storeId: String) {
        categoryRepostiory.getCategoryList(storeId)
    }
}