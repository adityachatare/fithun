package com.fithun.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fithun.api.responses.SizeValueData
import com.fithun.clickInterfaces.SizeListener
import com.fithun.databinding.ListsBinding
import com.fithun.utils.capitalizeFirstLetter


class SizeAdaptor(
    var context: Context,
    var data:List<SizeValueData>,
    var dismissSizeListener: SizeListener
) :
    RecyclerView.Adapter<SizeAdaptor.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
           data[position].apply {
               holder.binding.contentTextView.text = "$weight ${unit.capitalizeFirstLetter()}"

               holder.binding.root.setOnClickListener{
                   dismissSizeListener.sizeUnitListener(unit,weight,price,id)
               }






           }


        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
            return data.size
    }


    inner class ViewHolder( val binding: ListsBinding) : RecyclerView.ViewHolder(binding.root)








}