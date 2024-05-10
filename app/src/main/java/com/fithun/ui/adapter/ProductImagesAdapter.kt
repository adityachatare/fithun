package com.fithun.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fithun.clickInterfaces.ProductImagePreviewClick
import com.fithun.databinding.BannerHomeLayoutBinding
import com.fithun.utils.load
import com.fithun.utils.setSafeOnClickListener


class ProductImagesAdapter(
    var context: Context,
    var data: List<String>,
    val click:ProductImagePreviewClick
) : RecyclerView.Adapter<ProductImagesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BannerHomeLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            data[position].apply {
                holder.binding.imgBanner.load(this)



                holder.binding.root.setSafeOnClickListener {
                    click.viewImage(position)
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }





    override fun getItemCount(): Int {
        return data.size
    }


    inner class ViewHolder( val binding: BannerHomeLayoutBinding) : RecyclerView.ViewHolder(binding.root)










}