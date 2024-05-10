package com.fithun.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fithun.R
import com.fithun.api.responses.ContestDetails

import com.fithun.clickInterfaces.JoinStepContest
import com.fithun.databinding.ContestCategoryLayoutBinding
class StepContestAdapter(
    var context: Context,
    val data: ArrayList<ContestDetails>,
    private val click: JoinStepContest
) : RecyclerView.Adapter<StepContestAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ContestCategoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            data[position].apply {
                Glide.with(context).load(categoryImage).thumbnail(0.25f)
                    .placeholder(R.drawable.dummy_product).into(holder.binding.serviceImage)

                holder.binding.heading.text = heading
                holder.binding.contestType.text = contestType
                holder.binding.description.text = description


                holder.binding.JoinAndEarn.setOnClickListener {
                    click.join(id)
                }
            }




        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(val binding: ContestCategoryLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}





