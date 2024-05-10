package com.fithun.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fithun.R
import com.fithun.api.responses.ViewProducts
import com.fithun.clickInterfaces.ClickForTracking
import com.fithun.databinding.OrderHistoryItemBinding
import com.fithun.utils.DateFormat

class VIewOrderHistoryAdapter(
    var context: Context,
    var list : ArrayList<ViewProducts>,
    val click:ClickForTracking
) : RecyclerView.Adapter<VIewOrderHistoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OrderHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.binding.trackOrderNow.isVisible=  true
            holder.binding.itemName.text = list[position].productId.productName

            for (price in list[position].productId.size_value.indices){
                if (list[position].productId.size_value[price].id == list[position].sizeValueId){
                    holder.binding.price.text = "Price: ${list[position].productId.size_value[price].price}"
                }
            }
            holder.binding.qty.text = "Qty: ${list[position].quantity.toString()}"
            holder.binding.txtOrderID.text = "OrderId: ${list[position].productId._id}"
            holder.binding.txtDeliveryDate.text = "Delivery Date: ${DateFormat.dateFormater(list[position].productId.updatedAt)}"
            holder.binding.txtStatus.text = list[position].productId.status
            Glide.with(context).load(list[position].productId.productImage[0]).into(holder.binding.image)

            if (list[position].productId.status == "ACTIVE"){
                holder.binding.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.green_color))
            }

            holder.binding.trackOrderNow.setOnClickListener {
                click.trackOrder(list[position].awb)
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder( val binding: OrderHistoryItemBinding) : RecyclerView.ViewHolder(binding.root)

}