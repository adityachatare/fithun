package com.fithun.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fithun.R
import com.fithun.api.responses.Lists
import com.fithun.clickInterfaces.ViewOrderHistory
import com.fithun.databinding.OrderHistoryItemBinding
import com.fithun.ui.activities.product.ProductDetailsActivity
import com.fithun.utils.DateFormat

class OrderHistoryAdapter(
    var context: Context,
    var viewOrder:ViewOrderHistory,
    var list : ArrayList<Lists>
) : RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OrderHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            list[position].apply {
                holder.binding.itemName.text = products[0].productId.productName

                for (price in products[0].productId.size_value.indices){
                    if (products[0].productId.size_value[price].id == products[0].sizeValueId){
                        holder.binding.price.text = "Price: ${products[0].productId.size_value[price].price}"
                    }
                }

                holder.binding.qty.text = "Qty: ${products[0].quantity}"
                holder.binding.txtOrderID.text = "OrderId: ${id}"
                holder.binding.txtDeliveryDate.text = "Delivery Date: ${DateFormat.contestDateFormat(DateFormat.dateFormater(updatedAt))}"
                holder.binding.txtStatus.text = status

                if (status == "ACTIVE"){
                    holder.binding.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.green_color))
                }

                Glide.with(context).load(products[0].productId.productImage[0]).into(holder.binding.image)
                holder.binding.root.setOnClickListener {
                    viewOrder.viewOrder(id,"orderStatus")
                }


                holder.binding.image.setOnClickListener {
                    val intent = Intent(context, ProductDetailsActivity::class.java)
                    intent.putExtra("productId",products.getOrNull(0)!!.productId._id)
                    context.startActivity(intent)
                }


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