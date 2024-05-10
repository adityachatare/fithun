package com.fithun.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.fithun.api.responses.AddressResult
import com.fithun.clickInterfaces.AddressClick
import com.fithun.databinding.SelectDeliveryAddressLayoutBinding
import com.fithun.ui.activities.product.AddAddressActivity
import com.fithun.utils.setSafeOnClickListener


class ChooseDeliveryAddressAdapter(
    var context: Context,
    val addressData:ArrayList<AddressResult>,
    val click:AddressClick,
    val isFrom:String
) : RecyclerView.Adapter<ChooseDeliveryAddressAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SelectDeliveryAddressLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {

            if (addressData.size == 1){

                holder.binding.delete.isVisible = false
            }

                holder.binding.radioButton.isEnabled = false

            holder.binding.radioButton.isVisible = isFrom =="Cart"

            addressData[position].apply {
                holder.binding.address.text = "$houseNumber $area $country $state $city  $zipCode"
                holder.binding.firstname.text = "$firstName $lastName"
                holder.binding.radioButton.isChecked = isSelected


                holder.binding.chooseDelivery.setOnClickListener {
                    if (isFrom =="Cart"){
                        if (!isSelected) {
                            val previouslySelectedItem = getSelectedItem()
                            previouslySelectedItem?.isSelected = false
                            isSelected = true
                            notifyDataSetChanged()
                        } else {
                            val previouslySelectedItem = getSelectedItem()
                            previouslySelectedItem?.isSelected = true
                            isSelected = false
                            notifyDataSetChanged()
                        }
                        click.addressClick(id)
                    }

                }


                holder.binding.delete.setSafeOnClickListener {
                    click.deleteClick(position, id)
                }

                holder.binding.editaddress.setOnClickListener{
                    val intent = Intent(context, AddAddressActivity::class.java)
                    intent.putExtra("isFor","Edit")
                    intent.putExtra("id",id)
                    context.startActivity(intent)
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }





    override fun getItemCount(): Int {
        return addressData.size
    }


    inner class ViewHolder( val binding: SelectDeliveryAddressLayoutBinding) : RecyclerView.ViewHolder(binding.root)



    private fun getSelectedItem(): AddressResult? {
        return addressData.find { it.isSelected }
    }






}