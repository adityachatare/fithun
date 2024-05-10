package com.fithun.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fithun.R
import com.fithun.api.responses.StateCityResult
import com.fithun.clickInterfaces.StateCityCommonClick


class OpenDialog(
    var context: Context,
    var data: List<StateCityResult>,
    var flag: String,
    var click: StateCityCommonClick
) :
    RecyclerView.Adapter<OpenDialog.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val mInflater = LayoutInflater.from(context)
        val view = mInflater.inflate(R.layout.lists, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Data = data[position]

        when (flag) {
            "State" -> {
                holder.Names.text = Data.name
                holder.Names.setOnClickListener {
                    Data.name.let { it1 -> click.getCityState(it1,flag,Data.stateCode) }
                }

            }
            "City" -> {
                holder.Names.text = Data.name
                holder.Names.setOnClickListener {
                    Data.name.let { it1 -> click.getCityState(it1,flag,"") }
                }

            }

            else -> {

            }
        }



    }

    override fun getItemCount(): Int {
        return data.size
    }

     class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        var Names: TextView



        init {
            Names = itemView.findViewById(R.id.content_textView)

        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: ArrayList<StateCityResult>) {
        data = filteredList
        notifyDataSetChanged()

    }


}