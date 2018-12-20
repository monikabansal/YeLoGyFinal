package com.os.drewel.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yelogy.category.CategoryActivity
import com.yelogy.databinding.ItemStoreBinding
import com.yelogy.deliveryaddress.NearByStoreResponse
import com.yelogy.utill.AppIntentExtrakeys.STORE_ID


/**
 * Created by monika on 3/13/2018.
 */

class StoreAdapter(val mContext: Context, private val data: List<NearByStoreResponse.Datum>) : RecyclerView.Adapter<StoreAdapter.StoreHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreHolder {
        return StoreAdapter.StoreHolder(ItemStoreBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: StoreHolder, position: Int) {
        val store = data[position]
        holder.apply {
            bind(createOnClickListener(store.id.toString()), store)
        }
    }


    override fun getItemCount(): Int {
        return data.size
    }


    private fun createOnClickListener(storeId: String): View.OnClickListener {
        return View.OnClickListener {
            val intent = Intent(mContext, CategoryActivity::class.java)
            intent.putExtra(STORE_ID, storeId)
            mContext.startActivity(intent)
        }
    }


    class StoreHolder(val binding: ItemStoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: View.OnClickListener, item: NearByStoreResponse.Datum) {
            binding.apply {
                clickListener = listener
                store = item
                executePendingBindings()
            }
        }
    }

}
