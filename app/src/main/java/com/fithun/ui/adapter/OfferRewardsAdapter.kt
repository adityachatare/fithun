package com.fithun.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.fithun.databinding.YourRewardsLayoutBinding

class OfferRewardsAdapter(
    var context: Context
) : RecyclerView.Adapter<OfferRewardsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = YourRewardsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {

            if (position ==2 || position == 3){
                holder.binding.icon.isVisible =  false
                holder.binding.timer.isVisible =  false
                holder.binding.betterLuck.isVisible =  true

            }else{
                holder.binding.icon.isVisible =  true
                holder.binding.timer.isVisible =  true
                holder.binding.betterLuck.isVisible =  false
            }



        } catch (e: Exception) {
            e.printStackTrace()
        }


    }





    override fun getItemCount(): Int {
        return 4
    }


    inner class ViewHolder( val binding: YourRewardsLayoutBinding) : RecyclerView.ViewHolder(binding.root)










}