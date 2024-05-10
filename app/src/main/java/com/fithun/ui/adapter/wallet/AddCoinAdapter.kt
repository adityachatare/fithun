package com.fithun.ui.adapter.wallet

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.fithun.R
import com.fithun.api.responses.AddCoinList
import com.fithun.clickInterfaces.AddCoin
import com.fithun.databinding.AddCoinLayoutBinding

class AddCoinAdapter(
    var context: Context,
    var data: List<AddCoinList>,
    var addCoin: AddCoin,
    val walletAmount: Int,
    val isScreenFor : String,

) : RecyclerView.Adapter<AddCoinAdapter.ViewHolder>() {

    var previousPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AddCoinLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            data[position].apply {
                holder.binding.coinValue.text = "₩ $price"

                holder.binding.llPriceSelected.setOnClickListener {


                    if (price.toInt() > walletAmount && isScreenFor == "Fund"){
                        Toast.makeText(context, "Not Enough money in wallet", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }


                        if (position != previousPosition) {
                            data[position].isSelected = !data[position].isSelected

                            if (previousPosition != -1) {
                                data[previousPosition].isSelected = false
                            }
                            previousPosition = position
                            addCoin.addCoin(data[position].price)
                            notifyDataSetChanged()
                        }
                }

                if (data[position].isSelected) {
                    holder.binding.llPriceSelected.setBackgroundDrawable(context.getDrawable(R.drawable.blue_border_background))
                } else {
                    holder.binding.llPriceSelected.setBackgroundDrawable(context.getDrawable(R.drawable.white_border_background))
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder( val binding: AddCoinLayoutBinding) : RecyclerView.ViewHolder(binding.root)

}