package com.fithun.ui.adapter.notification

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.fithun.api.responses.NotificationDocs
import com.fithun.databinding.NotificationListLayoutBinding
import com.fithun.utils.DateFormat


class NotificationAdapter(
    var context: Context,
    val data: List<NotificationDocs>) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NotificationListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            data[position].apply {
                holder.binding.txtHeading.text = subject
                holder.binding.txtContent.text = body

                val (date,time)= DateFormat.dateForForHistory(createdAt)
                holder.binding.hour.text = "$date, $time"
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