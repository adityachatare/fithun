package com.fithun.ui.adapter

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fithun.api.responses.BannerResult
import com.fithun.databinding.BannerHomeLayoutBinding
import com.fithun.utils.load


class BannerAdapter(
    var context: Context,
    var data: List<BannerResult>,
) : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BannerHomeLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    fun openLink(context: Context, link: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Handle the case where there is no app to handle the link
//            Toast.makeText(context, "No app to handle this link", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // Handle other exceptions that may occur
//            Toast.makeText(context, "Error opening the link", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            data[position].apply {
                holder.binding.imgBanner.load(bannerImage)
                holder.binding.imgBanner.setOnClickListener {
                    val link = link
                    if (link!!.isNotEmpty()) {
                        openLink(context, link)
                    }
//                    else {
//                        Toast.makeText(context, "Invalid link for this banner", Toast.LENGTH_SHORT).show()
//                    }
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