package com.yelogy.category

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.yelogy.signup.SignupViewModel

class CategoryViewModelFactory (val categoryRepostiory: CategoryRepostiory): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(categoryRepostiory) as T
    }
}