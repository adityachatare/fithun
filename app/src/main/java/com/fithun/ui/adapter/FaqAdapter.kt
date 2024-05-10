package com.fithun.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fithun.R
import com.fithun.api.responses.Docs
import com.fithun.clickInterfaces.HowToPlayCategory
import com.fithun.databinding.FaqsModellayoutBinding
import com.fithun.utils.loadImageResource

class FaqAdapter(
    var context: Context,
    var data: ArrayList<Docs>?,
    private val isFrom:String,
    val clickListener : HowToPlayCategory

):
    RecyclerView.Adapter<FaqAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FaqsModellayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val listData = data!![position]
            if (isFrom == "Faqs") {
                holder.binding.txtHeading.text = listData.question
            } else {
                holder.binding.txtHeading.text = listData.categoryData.name
            }

            holder.binding.txtContent.text = Html.fromHtml(listData.answer)

            if (isFrom == "Faqs"){

                holder.binding.expand.visibility = if (listData.expand) View.VISIBLE else View.GONE
            }

            holder.binding.txtHeading.setOnClickListener {
                if (isFrom == "Faqs") {
                    listData.expand = !listData.expand
                    notifyDataSetChanged()
                } else {
                    listData.subCategoryData?.let { it1 -> clickListener.getHowToPlayCategory(it1) }
                }

            }

            if (listData.expand){
                holder.binding.icon.loadImageResource(R.drawable.arrow_up)
            }else{
                holder.binding.icon.loadImageResource(R.drawable.arrow_down)
            }

        }catch (_:Exception){

        }

    }

    override fun getItemCount(): Int {
        return data!!.size
    }


    inner class ViewHolder(val binding: FaqsModellayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


}