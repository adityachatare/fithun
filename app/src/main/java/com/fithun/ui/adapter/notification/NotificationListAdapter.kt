package com.fithun.ui.adapter.notification

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fithun.databinding.NotificationListLayoutBinding
import com.fithun.uiModalClass.NotificationListData


class NotificationListAdapter(
    var context: Context,
    val data: List<NotificationListData>) : RecyclerView.Adapter<NotificationListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NotificationListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            data[position].apply {
                holder.binding.txtHeading.text= title
                holder.binding.txtContent.text= description
                holder.binding.hour.text= time




            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }





    override fun getItemCount(): Int {
        return data.size
    }


    inner class ViewHolder( val binding: NotificationListLayoutBinding) : RecyclerView.ViewHolder(binding.root)










}