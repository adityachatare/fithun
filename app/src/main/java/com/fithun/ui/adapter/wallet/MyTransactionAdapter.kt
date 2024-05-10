package com.fithun.ui.adapter.wallet

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.fithun.R
import com.fithun.api.responses.TransactionList
import com.fithun.databinding.MyTransactionLayoutBinding
import com.fithun.utils.DateFormat
import com.fithun.utils.capitalizeFirstLetter
import com.fithun.utils.loadImageResource

class MyTransactionAdapter(
    var context: Context,
    var data: ArrayList<TransactionList> = arrayListOf(),
) : RecyclerView.Adapter<MyTransactionAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MyTransactionLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            data[position].apply {

                when(txnType){
                    "WINNING" -> {
                        holder.binding.title.text = txnType.capitalizeFirstLetter()
                        holder.binding.ImageLogo.loadImageResource(R.drawable.winning_wallet)
                        holder.binding.price.setTextColor(Color.parseColor("#B07A1D"))
                    }
                    "WITHDRAW" -> {
                        holder.binding.title.text = "$txnType".capitalizeFirstLetter()
                        holder.binding.ImageLogo.loadImageResource(R.drawable.withdraw_wallet)
                        holder.binding.price.setTextColor(Color.parseColor("#DC1C13"))
                    }

                    "CONTEST_CANCELLED" -> {
                        holder.binding.title.text = "Entry Fee Refunded"
                        holder.binding.ImageLogo.loadImageResource(R.drawable.deposit_wallet)
                        holder.binding.price.setTextColor(Color.parseColor("#009D25"))
                    }
                    else -> {

                        if (amount.toString().startsWith("-")) {
                            holder.binding.title.text = "Entry Fee"
                            holder.binding.ImageLogo.loadImageResource(R.drawable.withdraw_wallet)
                            holder.binding.price.setTextColor(Color.parseColor("#DC1C13"))
                        } else {
                            holder.binding.title.text = "Deposit"
                            holder.binding.ImageLogo.loadImageResource(R.drawable.deposit_wallet)
                            holder.binding.price.setTextColor(Color.parseColor("#009D25"))
                        }


                    }
                }
                val (formattedStartDate, formattedStartTime) = DateFormat.dateForForHistory(createdAt)

                holder.binding.time.text = "$formattedStartDate, $formattedStartTime"
                holder.binding.price.text = "₩"+ amount.toString()


            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }


    inner class ViewHolder( val binding: MyTransactionLayoutBinding) : RecyclerView.ViewHolder(binding.root)

}