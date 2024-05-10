package com.fithun.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fithun.api.responses.ProductListDocs
import com.fithun.clickInterfaces.AddProductCart
import com.fithun.databinding.CatalogueLayoutBinding
import com.fithun.ui.activities.product.ProductDetailsActivity
import com.fithun.utils.AndroidExtension.initLoader
import com.fithun.utils.capitalizeFirstLetter


class CatalogueAdapter(
    var context: Context,
    private val docs: List<ProductListDocs>,
    private val productClick:AddProductCart
) : RecyclerView.Adapter<CatalogueAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CatalogueLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {

            val binding =  holder.binding
            docs[position].apply {

                binding.title.text = productName.capitalizeFirstLetter()
                binding.description.text = productDescription.trim()
                binding.quantity.text =  "$quantity"
                binding.price.text = "Rs ${sizeValue.getOrNull(0)!!.price}"
                Glide.with(context)
                    .load(productImage.getOrNull(0))
                    .thumbnail(0.25f).into(binding.productImage)


                binding.root.setOnClickListener {
                    val intent = Intent(context, ProductDetailsActivity::class.java)
                    intent.putExtra("productId",id)
                    context.startActivity(intent)
                }

                binding.BuyNowClick.setOnClickListener {

                    binding.BuyNowClick.isVisible = false
                    binding.incDesign.isVisible = true
                    productClick.addToCartProduct(id,sizeValue[0].id,quantity,binding.BuyNowClick,binding.incDesign,position)
                }

                binding.decrement.setOnClickListener {
                    binding.loader.initLoader(true)
                    binding.stopLoading.isVisible = false
                    binding.loaderLl.isVisible = true
                    if (quantity == 1){
                        binding.BuyNowClick.isVisible = true
                        binding.incDesign.isVisible = false

                        productClick.decrementItem(id,sizeValue[0].id,quantity,binding.BuyNowClick,binding.incDesign,sizeValue.getOrNull(0)!!.id,binding.stopLoading,binding.loaderLl,binding.loader)
                        return@setOnClickListener
                    }

                    quantity -= 1
                    binding.quantity.text =  quantity.toString()
                    productClick.decrementItem(id,sizeValue[0].id,quantity,binding.BuyNowClick,binding.incDesign,sizeValue.getOrNull(0)!!.id,binding.stopLoading,binding.loaderLl,binding.loader)
                }

                binding.increment.setOnClickListener {
                    quantity += 1
                    binding.loader.initLoader(true)
                    binding.stopLoading.isVisible = false
                    binding.loaderLl.isVisible = true
                    binding.quantity.text = quantity.toString()
                    productClick.incrementItem(id,sizeValue[0].id,quantity,binding.BuyNowClick,binding.incDesign, sizeValue.getOrNull(0)!!.id,binding.stopLoading,binding.loaderLl,binding.loader)
                }


            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }





    override fun getItemCount(): Int {
        return docs.size
    }


    inner class ViewHolder( val binding: CatalogueLayoutBinding) : RecyclerView.ViewHolder(binding.root)



}






