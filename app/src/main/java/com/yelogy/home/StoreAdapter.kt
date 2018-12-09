package com.os.drewel.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yelogy.R


/**
 * Created by sharukhb on 3/13/2018.
 */

class StoreAdapter(val mContext: Context) : RecyclerView.Adapter<StoreAdapter.StoreHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_store, parent, false)
        return StoreHolder(view)
    }

    override fun onBindViewHolder(holder: StoreHolder, position: Int) {

    }


    override fun getItemCount(): Int {
        return 5
    }


    class StoreHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}
