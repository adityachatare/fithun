package com.fithun.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fithun.api.responses.CartViewProducts
import com.fithun.clickInterfaces.CartSizeDetailsClick
import com.fithun.databinding.ProductCartLayoutBinding
import com.fithun.utils.AndroidExtension.initLoader
import com.fithun.utils.capitalizeFirstLetter
import com.fithun.utils.setSafeOnClickListener


class ProductCartAdapter(
    var context: Context,
    private val products: List<CartViewProducts>,
    private val productCartClick:CartSizeDetailsClick,
    private val clickFrom:String
) : RecyclerView.Adapter<ProductCartAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductCartLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val sizeId =  products[position].sizeValueId
            products[position].productId.apply {
                holder.binding.itemName.text = productName.capitalizeFirstLetter()
                holder.binding.quantity.text = products[position].quantity.toString()

                holder.binding.expectedDate.isVisible = clickFrom ==""
                holder.binding.expectedDate.text = "Expected Delivery Date: ${products[position].etd}"

                for (item in sizeValue.indices){
                    if (sizeId == sizeValue[item].id){
                        holder.binding.genderSpinner.text = "${sizeValue[item].weight} ${sizeValue[item].unit}"
                        holder.binding.price.text = "Rs ${sizeValue[item].price}"
                    }
                }


                Glide.with(context)
                    .load(productImage.getOrNull(0))
                    .thumbnail(0.25f).into(holder.binding.image)



                holder.binding.genderSpinner.setOnClickListener{
                    if (sizeValue.size > 1 && clickFrom == "Cart"){
                        productCartClick.openSizeDetails(sizeValue,id)
                    }
                }



                holder.binding.increment.setSafeOnClickListener {
                    if (clickFrom == ""){
                        return@setSafeOnClickListener
                    }

                    holder.binding.loader.initLoader(true)
                    holder.binding.stopLoading.isVisible = false
                    holder.binding.loaderLl.isVisible = true

                    products[position].quantity += 1

                    productCartClick.updateCartClick(
                        id,
                        sizeId,
                        products[position].quantity,
                        "increment",
                        holder.binding.loader,
                        holder.binding.stopLoading,
                        holder.binding.loaderLl
                    )
                }

                holder.binding.decrement.setSafeOnClickListener {
                    if (clickFrom == ""){
                        return@setSafeOnClickListener
                    }

                    holder.binding.loader.initLoader(true)
                    holder.binding.stopLoading.isVisible = false
                    holder.binding.loaderLl.isVisible = true

                    if (products[position].quantity ==1 ){
                        productCartClick.deleteItemFromCart(id,sizeId,position)
                        return@setSafeOnClickListener
                    }
                    products[position].quantity -= 1

                    productCartClick.updateCartClick(id,sizeId,products[position].quantity,"decrement",holder.binding.loader,holder.binding.stopLoading,holder.binding.loaderLl)
                }



            }


        } catch (e: Exception) {
            e.printStackTrace()
        }


    }





    override fun getItemCount(): Int {
        return products.size
    }


    inner class ViewHolder( val binding: ProductCartLayoutBinding) : RecyclerView.ViewHolder(binding.root)










}