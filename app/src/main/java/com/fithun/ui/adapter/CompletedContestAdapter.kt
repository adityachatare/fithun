package com.fithun.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fithun.R
import com.fithun.api.responses.CompletedContest
import com.fithun.databinding.CompletedContestListLayoutBinding
import com.fithun.ui.activities.contest.WinnerUsersActivity
import com.fithun.utils.DateFormat


class CompletedContestAdapter(
    var context: Context,
    var arrayList: ArrayList<CompletedContest>
) : RecyclerView.Adapter<CompletedContestAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CompletedContestListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            arrayList[position].contest.apply {

                holder.binding.title.text = heading
                holder.binding.subTitle.text = subHeading
                holder.binding.sports.text = maximum_spot.toString()
                holder.binding.entryFee.text = "₩ $entryFee"
                holder.binding.rank.text = "${arrayList[position].winner.rank}"


                /*if (status.uppercase() == "CANCELED"){

                    Glide.with(context).load(R.drawable.cross_red).into(holder.binding.iconContest)
                    Glide.with(context).load(R.drawable.cancel_trophy).into(holder.binding.imageTrophy)
                    Glide.with(context).load(R.drawable.cancel_text).into(holder.binding.textValue)
                    holder.binding.status.text = "Canceled"
                    holder.binding.status.text = "Canceled"
                    holder.binding.root.isEnabled = false

                }else{
                    Glide.with(context).load(R.drawable.check_mark).into(holder.binding.iconContest)
                    holder.binding.rank.text = arrayList[position].winner.rank.toString()

                    if ("$maximum_spot" == "2"  && "${arrayList[position].winner.rank}" == "2"){
                        Glide.with(context).load(R.drawable.lost_text).into(holder.binding.textValue)
                        Glide.with(context).load(R.drawable.cancel_trophy).into(holder.binding.imageTrophy)

                    }else{
                        Glide.with(context).load(R.drawable.winner_image).into(holder.binding.textValue)

                    }




                }*/




                val (formattedStartDate, formattedStartTime) = DateFormat.dateForForHistory(startDate)


                holder.binding.date.text= "$formattedStartDate"
                holder.binding.time.text= "$formattedStartTime".uppercase()


                if (currentPricePool.isNotEmpty()){
                    holder.binding.poolPrice.text = "₩"+(currentPricePool[0].price).toString()
                }

                holder.binding.root.setOnClickListener {
                    val intent =  Intent(context, WinnerUsersActivity::class.java)
                    intent.putExtra("contestId", _id)
                    context.startActivity(intent)
                }



            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder( val binding: CompletedContestListLayoutBinding) : RecyclerView.ViewHolder(binding.root)

}