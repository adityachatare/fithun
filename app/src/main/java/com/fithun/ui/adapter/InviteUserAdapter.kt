package com.fithun.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fithun.R
import com.fithun.api.responses.FriendAction
import com.fithun.databinding.InviteUserLayoutBinding

class InviteUserAdapter(
    var context: Context,
    var list : ArrayList<FriendAction>
) : RecyclerView.Adapter<InviteUserAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = InviteUserLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {


            if (position %2 == 0){
                holder.binding.details.background = ContextCompat.getDrawable(context, R.drawable.light_grey_background)
            }else{
                holder.binding.details.background = ContextCompat.getDrawable(context, R.drawable.white_background)
            }
             holder.binding.name.text = list[position].referTo.userName
            holder.binding.amount.text = list[position].rewardedAmount.toString()
            holder.binding.amountPlayedFor.text ="₩" +list[position].rewardedAmount.toString()


        } catch (e: Exception) {
            e.printStackTrace()
        }


    }





    override fun getItemCount(): Int {
        return list.size
    }


    inner class ViewHolder( val binding: InviteUserLayoutBinding) : RecyclerView.ViewHolder(binding.root)










}