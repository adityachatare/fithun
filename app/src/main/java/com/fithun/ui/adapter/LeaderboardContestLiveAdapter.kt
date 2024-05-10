package com.fithun.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fithun.R
import com.fithun.api.responses.MyContestWinnerList
import com.fithun.databinding.LeaderboardUserListLayoutBinding

class LeaderboardContestLiveAdapter(
    var context: Context,
    var isFrom :String,
    var list : ArrayList<MyContestWinnerList>
) : RecyclerView.Adapter<LeaderboardContestLiveAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LeaderboardUserListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {

            if (isFrom == ""){
                holder.binding.Header.isVisible = position == 0

                if (position %2 != 0){
                    holder.binding.detailsAll.background = ContextCompat.getDrawable(context, R.drawable.light_grey_background)
                }else{
                    holder.binding.detailsAll.background = ContextCompat.getDrawable(context, R.drawable.white_background)
                }

            }else{
                holder.binding.Header.isVisible = false

                if (position %2 != 0){
                    holder.binding.detailsAll.background = ContextCompat.getDrawable(context, R.drawable.light_grey_background)
                }else{
                    holder.binding.detailsAll.background = ContextCompat.getDrawable(context, R.drawable.white_background)
                }
            }
            holder.binding.username.text = list[position].userId.userName
            holder.binding.rank.text = list[position].rank.toString()
            holder.binding.stepCount.text = list[position].stepId.stepCount
            Glide.with(context).load(list[position].userId.profilePic).placeholder(R.drawable.placeholder).into(holder.binding.profilePic)





        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun getItemCount(): Int {
        return list.size
    }


    inner class ViewHolder( val binding: LeaderboardUserListLayoutBinding) : RecyclerView.ViewHolder(binding.root)










}