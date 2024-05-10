package com.fithun.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fithun.R
import com.fithun.api.responses.LeaderBoardList
import com.fithun.databinding.LeaderboardListLayoutBinding
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.setTextViewValue

class LeaderboardContestAdapter(
    var context: Context,
    var dataList: List<LeaderBoardList>,
    val isJoinedUser: Boolean
) : RecyclerView.Adapter<LeaderboardContestAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LeaderboardListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {

            if (isJoinedUser){
                holder.binding.afterJoining.isVisible =  true
                holder.binding.beforeJoining.isVisible =  false

            }else{
                holder.binding.afterJoining.isVisible =  false
                holder.binding.beforeJoining.isVisible =  true
            }
            dataList[position].apply {
                holder.binding.Header.isVisible = position == 0
                holder.binding.headerNew.isVisible = position == 0
                val myUserId =  SavedPrefManager.getStringPreferences(context,SavedPrefManager.userId).toString()

                if (myUserId == id){
                    holder.binding.details.background = ContextCompat.getDrawable(context, R.drawable.light_grey_background)
                    holder.binding.detailsAll.background = ContextCompat.getDrawable(context, R.drawable.light_grey_background)
                }else{
                    holder.binding.details.background = ContextCompat.getDrawable(context, R.drawable.white_background)
                    holder.binding.detailsAll.background = ContextCompat.getDrawable(context, R.drawable.white_background)
                }

                holder.binding.textview.text = userId.name
                holder.binding.username.text = userId.userName
                holder.binding.rank.text = if (step.stepCount.isEmpty() || step.stepCount == "0") "0" else "${position+1}"
                holder.binding.stepCount.setTextViewValue(step.stepCount)


                Glide.with(context).load(userId.profilePic).thumbnail(0.25f).placeholder(R.drawable.profile_placeholder).into(holder.binding.image)
                Glide.with(context).load(userId.profilePic).thumbnail(0.25f).placeholder(R.drawable.profile_placeholder).into(holder.binding.profilePic)





            }





        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(val binding: LeaderboardListLayoutBinding) : RecyclerView.ViewHolder(binding.root)

}