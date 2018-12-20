/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yelogy.utill

import android.content.Context
import com.yelogy.category.CategoryRepostiory
import com.yelogy.category.CategoryViewModel
import com.yelogy.category.CategoryViewModelFactory
import com.yelogy.home.HomeRepository
import com.yelogy.home.HomeViewModelFactory
import com.yelogy.login.LoginRepository
import com.yelogy.login.LoginViewModelFactory
import com.yelogy.signup.SignupRepository
import com.yelogy.signup.SignupViewModelFactory

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */

object InjectorUtils {

    fun provideSignupViewModelFactory(context: Context): SignupViewModelFactory {

        val signupRepository=SignupRepository(context)
        return SignupViewModelFactory(signupRepository)
    }

    fun provideLoginViewModelFactory(context: Context): LoginViewModelFactory {

        val loginRepository=LoginRepository(context)
        return LoginViewModelFactory(loginRepository)
    }

    fun provideHomeViewModelFactory(context: Context): HomeViewModelFactory {

        val homeRepository=HomeRepository(context)
        return HomeViewModelFactory(homeRepository)
    }

    fun provideCategoryViewModelFactory(context: Context): CategoryViewModelFactory {

        val categoryRepostiory=CategoryRepostiory(context)
        return CategoryViewModelFactory(categoryRepostiory)
    }

}
