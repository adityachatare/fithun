package com.fithun.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.fithun.R
import com.fithun.api.responses.WinnerPriceData
import com.fithun.databinding.WinningContestListBinding

class WinningsContestAdapter(
    var context: Context,
    var list : ArrayList<WinnerPriceData> = arrayListOf(),
    var typeSpot : Boolean
) : RecyclerView.Adapter<WinningsContestAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = WinningContestListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {

            holder.binding.Header.isVisible = position == 0

            if (position %2 != 0){
                holder.binding.details.background = ContextCompat.getDrawable(context, R.drawable.light_grey_background)
            }else{
                holder.binding.details.background = ContextCompat.getDrawable(context, R.drawable.white_background)
            }



            holder.binding.rank.text = "# ${list[position].rank}"
            holder.binding.price.text = "₩ ${list[position].price}"

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class ViewHolder( val binding: WinningContestListBinding) : RecyclerView.ViewHolder(binding.root)

}