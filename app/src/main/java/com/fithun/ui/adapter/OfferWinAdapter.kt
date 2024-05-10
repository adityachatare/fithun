package com.fithun.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fithun.databinding.YourWinLayoutBinding

class OfferWinAdapter(
    var context: Context
) : RecyclerView.Adapter<OfferWinAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = YourWinLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {





        } catch (e: Exception) {
            e.printStackTrace()
        }


    }





    override fun getItemCount(): Int {
        return 4
    }


    inner class ViewHolder( val binding: YourWinLayoutBinding) : RecyclerView.ViewHolder(binding.root)










}