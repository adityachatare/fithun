package com.fithun.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.fithun.R
import com.fithun.api.responses.Steps
import com.fithun.databinding.HowItWorksTrackBinding

class HowItWorkTrackAdapter(val context : Context, private val itemList: List<Steps>) :
    RecyclerView.Adapter<HowItWorkTrackAdapter.LastOrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastOrderViewHolder {
        val binding = HowItWorksTrackBinding.inflate(LayoutInflater.from(context), parent, false)
        return LastOrderViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor", "ResourceType")
    override fun onBindViewHolder(holder: LastOrderViewHolder, position: Int) {
        try {
            itemList[position].apply {

                holder.binding.progressLine.setProgressColor(ContextCompat.getColor(context, R.color.check_color))
                holder.binding.number.text = "${position+1}"
                holder.binding.itemHeader.text = heading
                holder.binding.itemDesc.text = subHeading

                if (itemList.size -1 == position){
                    holder.binding.progressLine.isVisible = false
                }

            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class LastOrderViewHolder(val binding : HowItWorksTrackBinding) : RecyclerView.ViewHolder(binding.root)

}