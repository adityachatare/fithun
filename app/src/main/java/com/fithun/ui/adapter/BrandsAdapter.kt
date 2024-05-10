package com.fithun.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fithun.api.responses.BrandListResult
import com.fithun.databinding.BrandsListLayoutBinding

class BrandsAdapter(
    var context: Context,
    var data: List<BrandListResult>,
) : RecyclerView.Adapter<BrandsAdapter.ViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BrandsListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val binding = holder.binding
            val brandItem = data[position]

            binding.txtName.text = brandItem.brandName
            binding.brandCB.isChecked = position == selectedPosition

            binding.brandCB.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedPosition = holder.bindingAdapterPosition
                    notifyDataSetChanged()
                }
                brandItem.isSelected = isChecked
            }

            binding.root.setOnClickListener {
                binding.brandCB.isChecked = true
                brandItem.isSelected = true
                selectedPosition = holder.bindingAdapterPosition
                notifyDataSetChanged()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(val binding: BrandsListLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: ArrayList<BrandListResult>) {
        data = filteredList
        notifyDataSetChanged()
    }
}
