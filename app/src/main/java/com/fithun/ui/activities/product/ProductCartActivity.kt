package com.fithun.ui.activities.product

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.fithun.R
import com.fithun.api.responses.CartViewProducts
import com.fithun.api.responses.SizeValueData
import com.fithun.clickInterfaces.CartSizeDetailsClick
import com.fithun.clickInterfaces.CommonClick
import com.fithun.clickInterfaces.GetUnitDetails
import com.fithun.databinding.ActivityProductCartBinding
import com.fithun.ui.adapter.ProductCartAdapter
import com.fithun.ui.bottomSheet.SelectSizeBottomSheet
import com.fithun.utils.AndroidExtension
import com.fithun.utils.AndroidExtension.initLoader
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.viewModel.ProductFlowViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductCartActivity : AppCompatActivity(), CartSizeDetailsClick, GetUnitDetails,
    CommonClick {

    private lateinit var binding: ActivityProductCartBinding


    private val viewModel: ProductFlowViewModel by viewModels()
    var token = ""
    var productId = ""
    var isLoaded = true

    private var deleteProductId = ""
    private var deleteProductSizeId = ""
    var deleteProductPosition = 0
    lateinit var adapter: ProductCartAdapter

    var productsData: ArrayList<CartViewProducts> = arrayListOf()


    private var stopLoadingProgress: LinearLayout? = null
    private var startLoading: RelativeLayout? = null
    private var loaderAnimation: LottieAnimationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString()

        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }





        binding.pullToRefresh.setOnRefreshListener {
            isLoaded = true
            viewModel.viewCartApi(token = token, sortByFreightCharges = false,
                sortByEarliestDelivery = false, sortByRating = false
            )
            binding.pullToRefresh.isRefreshing = false
        }



        binding.proceedToCheckOutClick.setOnClickListener {
            val intent =  Intent(this, ChooseDeliveryAddressActivity::class.java)
            intent.putExtra("isFrom", "Cart")
            startActivity(intent)
        }


        productAddToCartResponseObserver()
        updateCartObserver()
        updateCartQtyObserver()
        deleteProductObserver()
    }

    override fun onStart() {
        super.onStart()
        viewModel.viewCartApi(token = token, sortByFreightCharges = false,
            sortByEarliestDelivery = false, sortByRating = false)
    }


    private fun productAddToCartResponseObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.viewCartResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            binding.NoDataFound.isVisible = false
                            if (isLoaded) {
                                binding.mainLayout.isVisible = false
                                binding.llMyCart.isVisible = false
                                Progress.start(this@ProductCartActivity)
                            }

                        }


                        is Resource.Success -> {
                            Progress.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    isLoaded = false
                                    response.data.result.apply {
                                        binding.mainLayout.isVisible = products.isNotEmpty()
                                        binding.llMyCart.isVisible = products.isNotEmpty()
                                        binding.NoDataFound.isVisible = products.isEmpty()
                                        productsData = products
                                        setCatalogueAdaptor()



                                        binding.subTotalAmount.text = "Rs ${priceDetail.subTotal}"
                                        binding.discountAmount.text = "Rs ${priceDetail.stepsToRedeem}"
                                        binding.totalAmount.text = "Rs ${priceDetail.total}"
//                                        binding.shippingTotalAmount.text = "Rs ${priceDetail.shippingCharge}"


                                        binding.grandTotalAmount.text = "Rs ${priceDetail.grandTotal}"
                                        binding.TotalPrice.text = binding.grandTotalAmount.text


                                    }

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }


                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            binding.mainLayout.isVisible = false
                            binding.llMyCart.isVisible = false
                            binding.NoDataFound.isVisible = true
//                            response.message?.let { message ->
//                                if (!message.lowercase().contains("data not found")){
//                                    AndroidExtension.alertBox(message, this@ProductCartActivity)
//                                }
//
//                            }

                            response.message?.let {
                                AndroidExtension.alertBox(it, this@ProductCartActivity)
                            }

                        }


                        is Resource.Empty -> {
                            response.message?.let { message ->
                                if (!message.lowercase().contains("data not found")) {
                                    AndroidExtension.alertBox(message, this@ProductCartActivity)
                                }

                            }

                        }


                    }


                }

            }
        }
    }

    private fun setCatalogueAdaptor() {
        binding.productCartRecycler.layoutManager = LinearLayoutManager(this)
        adapter = ProductCartAdapter(this, productsData, this, "Cart")
        binding.productCartRecycler.adapter = adapter

    }

    override fun openSizeDetails(sizeValue: List<SizeValueData>, id: String) {
        productId = id
        supportFragmentManager.let { it1 ->
            SelectSizeBottomSheet(
                this,
                sizeValue,
                this
            ).show(it1, "ModalBottomSheet")
        }
    }

    override fun updateCartClick(
        productId: String,
        sizeId: String,
        qty: Int,
        type: String,
        loader: LottieAnimationView,
        stopLoading: LinearLayout,
        loaderLl: RelativeLayout
    ) {

        stopLoadingProgress= stopLoading
        startLoading =  loaderLl
        loaderAnimation = loader


        viewModel.updateQuantityApi(
            token = token,
            productId = productId,
            sizeValueId = sizeId,
            quantity = qty,
            type
        )
    }

    override fun deleteItemFromCart(productId: String, sizeId: String, position: Int) {
        deleteProductId = productId
        deleteProductSizeId = sizeId
        deleteProductPosition = position
        AndroidExtension.alertBoxDeleteItem(
            "Are you sure you want to remove this item from cart?",
            this,
            this
        )
    }

    override fun getUnitDetails(unit: String, size: String, price: Number, id: String) {
        viewModel.updateCartApi(token = token, productId = productId, sizeValueId = id)

    }

    private fun updateCartObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.updateCartResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {

                        }


                        is Resource.Success -> {
                            try {

                                if (response.data?.statusCode == 200) {
                                    loaderAnimation!!.initLoader(false)
                                    viewModel.viewCartApi(token = token, sortByFreightCharges = false,
                                        sortByEarliestDelivery = false, sortByRating = false)
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            loaderAnimation!!.initLoader(false)

                        }


                        is Resource.Empty -> {

                        }


                    }


                }

            }
        }
    }

    private fun updateCartQtyObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.updateCartQtyResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {

                        }


                        is Resource.Success -> {
                            try {

                                if (response.data?.statusCode == 200) {
                                    viewModel.viewCartApi(token = token, sortByFreightCharges = false,
                                        sortByEarliestDelivery = false, sortByRating = false)
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()

                        }


                        is Resource.Empty -> {

                        }


                    }


                }

            }
        }
    }

    override fun applyClick() {
        viewModel.deleteProductApi(
            token = token,
            productId = deleteProductId,
            sizeValueId = deleteProductSizeId
        )
    }


    private fun deleteProductObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.deleteProductResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(this@ProductCartActivity)
                        }


                        is Resource.Success -> {
                            Progress.stop()
                            try {

                                if (response.data?.statusCode == 200) {
                                    viewModel.viewCartApi(token = token, sortByFreightCharges = false,
                                        sortByEarliestDelivery = false, sortByRating = false)
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                        }


                        is Resource.Empty -> {

                        }


                    }


                }

            }
        }
    }

}