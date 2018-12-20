package com.yelogy.category.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yelogy.category.reuestresponse.CategoryListResponse
import com.yelogy.databinding.ItemCategoryListBinding

class CategoryAdapter(private val context : Context,val categoryList : List<CategoryListResponse.Datum>):RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
     return   categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoryList[position]
        holder.apply {
            bind(createOnClickListener(category.id), category)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCategoryListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    private fun createOnClickListener(plantId: Int?): View.OnClickListener {
        return View.OnClickListener {

        }
    }

    class ViewHolder(private val binding: ItemCategoryListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: CategoryListResponse.Datum) {
            binding.apply {
                category = item
                executePendingBindings()
            }
        }
    }
}