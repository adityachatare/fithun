package com.fithun.ui.activities.product

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fithun.R
import com.fithun.api.responses.SizeValueData
import com.fithun.clickInterfaces.GetUnitDetails
import com.fithun.clickInterfaces.ProductImagePreviewClick
import com.fithun.databinding.ActivityProductDetailsBinding
import com.fithun.ui.adapter.ProductImagesAdapter
import com.fithun.ui.bottomSheet.ImageShowDialog
import com.fithun.ui.bottomSheet.SelectSizeBottomSheet
import com.fithun.utils.AndroidExtension
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.displayToast
import com.fithun.viewModel.ProductFlowViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsActivity : AppCompatActivity(), GetUnitDetails, ProductImagePreviewClick {

    private lateinit var binding: ActivityProductDetailsBinding

    private val viewModel: ProductFlowViewModel by viewModels()
    var token = ""
    var productId = ""
    var sizeDetails : List<SizeValueData> =  arrayListOf()
    var priceRealtedId = ""

    private var productImages:List<String> =  listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations =  R.style.Fade


        token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString()
        intent.getStringExtra("productId")?.let { productId  = it }

        viewModel.productViewApi(token = token, id = productId)


        binding.genderSpinner.setOnClickListener {
            if (sizeDetails.size > 1){
                supportFragmentManager.let { it1 ->
                    SelectSizeBottomSheet(
                        this,
                        sizeDetails,
                        this
                    ).show(it1, "ModalBottomSheet")
                }
            }


        }




        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }


        binding.NextButton.setOnClickListener {
            viewModel.addToCartApi(token=token,productId=productId, sizeValueId=priceRealtedId, quantity="1")

        }

        productResponseObserver()
        productAddToCartResponseObserver()
    }

    private fun productResponseObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.productViewResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(this@ProductDetailsActivity)
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200) {
                                    response.data.result.apply {
                                        binding.productName.text = productName
                                        binding.description.text = productDescription
                                        sizeDetails =  sizeValue
                                        priceRealtedId = sizeValue.getOrNull(0)?.id!!
                                        binding.productPrice.text = "Rs ${sizeValue.getOrNull(0)?.price}"
                                        binding.genderSpinner.text = "${sizeValue.getOrNull(0)?.weight} ${sizeValue.getOrNull(0)?.unit}"

                                        binding.indicator.isVisible = productImage.size > 1
                                        productImages =  emptyList()
                                        productImages =  productImage
                                        setImageAdaptor()

                                    }
                                }


                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@ProductDetailsActivity)
                            }

                        }


                        is Resource.Empty -> {


                        }


                    }


                }

            }
        }
    }
    private fun productAddToCartResponseObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.addToCartResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(this@ProductDetailsActivity)
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.statusCode == 200) {
                                    displayToast(response.data.responseMessage)
                                    startActivity(Intent(this@ProductDetailsActivity,ProductCartActivity::class.java))
                                }


                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()


                        }


                        is Resource.Empty -> {
                            response.message?.let { message ->
                                if (!message.lowercase().contains("data not found")){
                                    AndroidExtension.alertBox(message, this@ProductDetailsActivity)

                                }

                            }

                        }


                    }


                }

            }
        }
    }

    private fun setImageAdaptor() {
        val bannerAdapter = ProductImagesAdapter(this, productImages,this)
        binding.storeViewpager.adapter = bannerAdapter
        binding.indicator.setViewPager(binding.storeViewpager)

    }

    override fun getUnitDetails(unit: String, size: String, price: Number, id: String) {
        binding.productPrice.text = "Rs $price"
        binding.genderSpinner.text = "$size $unit"
        priceRealtedId = id

    }

    override fun viewImage(imagePosition: Int) {
        ImageShowDialog(productImages,imagePosition).show(supportFragmentManager, "ShowImage")
    }
}