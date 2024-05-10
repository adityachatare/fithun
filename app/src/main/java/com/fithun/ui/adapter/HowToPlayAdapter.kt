package com.fithun.ui.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fithun.R
import com.fithun.api.responses.SubCategory
import com.fithun.databinding.HowModellayoutBinding
import com.fithun.utils.load

import com.fithun.utils.loadImageResource

class HowToPlayAdapter(
    var context: Context,
    private var subCategoryData: ArrayList<SubCategory>
) : RecyclerView.Adapter<HowToPlayAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            HowModellayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val listData = subCategoryData[position]

            holder.binding.txtHeading.text = listData.title
            holder.binding.txtContentHowToPlay.text = Html.fromHtml(listData.description)
            listData.media?.let { holder.binding.howToPlayImage.load(it) }

            holder.binding.expandHowToPlay.visibility =
                if (listData.expand) View.VISIBLE else View.GONE

            if (listData.expand) {
                holder.binding.icon.loadImageResource(R.drawable.arrow_up)
            } else {
                holder.binding.icon.loadImageResource(R.drawable.arrow_down)
            }

            holder.binding.txtHeading.setOnClickListener {
                listData.expand = !listData.expand
                notifyDataSetChanged()


            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return subCategoryData.size
    }

    inner class ViewHolder(val binding: HowModellayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}
