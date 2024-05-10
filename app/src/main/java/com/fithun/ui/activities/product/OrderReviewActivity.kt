package com.fithun.ui.activities.product

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.fithun.R
import com.fithun.api.Constants
import com.fithun.api.responses.CartViewProducts
import com.fithun.api.responses.SizeValueData
import com.fithun.clickInterfaces.CartSizeDetailsClick
import com.fithun.databinding.ActivityOrderReviewBinding
import com.fithun.ui.adapter.ProductCartAdapter
import com.fithun.utils.AndroidExtension
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.viewModel.ProductFlowViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class OrderReviewActivity : AppCompatActivity(), CartSizeDetailsClick, PaymentResultWithDataListener {
    private lateinit var binding:ActivityOrderReviewBinding
    private val viewModel: ProductFlowViewModel by viewModels()
    var token = ""
    var addressIdRequest = ""
    var isLoaded=  true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =  ActivityOrderReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString()
        binding.cheapestDelivery.isChecked = true

        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }



            binding.cheapestDelivery.setOnCheckedChangeListener { buttonView, isChecked ->
            // Handle the radio button state change
            if (isChecked) {
                viewModel.viewCartApi(token = token, sortByFreightCharges =true,
                    sortByEarliestDelivery = false, sortByRating = false)
            }
        }


        binding.fastestDelivery.setOnCheckedChangeListener { buttonView, isChecked ->
            // Handle the radio button state change
            if (isChecked) {
                viewModel.viewCartApi(token = token, sortByFreightCharges =false,
                    sortByEarliestDelivery = true, sortByRating = false)
            }
        }




        binding.editAddress.setOnClickListener{
            val intent = Intent(this, AddAddressActivity::class.java)
            intent.putExtra("isFor","Edit")
            intent.putExtra("id",addressIdRequest)
            startActivity(intent)
        }


        binding.checkOutButton.setOnClickListener {

            viewModel.checkOutCartApi(token=token,"${Constants.PAYMENT_URl}/api/payment/success")

//            startActivity(Intent(this,PaymentModeActivity::class.java))
        }



        productAddToCartResponseObserver()
        cartCheckoutResponseObserver()
    }

    override fun onStart() {
        super.onStart()
        viewModel.viewCartApi(token = token, sortByFreightCharges =true,
            sortByEarliestDelivery = false, sortByRating = false)
    }


    private fun productAddToCartResponseObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.viewCartResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {

                            if (isLoaded){
                                binding.orderLL.isVisible = false
                                Progress.start(this@OrderReviewActivity)
                            }

                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                isLoaded = false
                                if (response.data?.responseCode == 200) {
                                    response.data.result.apply {

                                        binding.orderLL.isVisible = products.isNotEmpty()
                                        addressIdRequest = addressId.id
                                        setCatalogueAdaptor(products)

                                        binding.subTotalAmount.text = "Rs ${priceDetail.subTotal}"
                                        binding.discountAmount.text = "Rs ${priceDetail.stepsToRedeem}"
                                        binding.totalAmount.text = "Rs ${priceDetail.total}"
                                        binding.shippingTotalAmount.text = "Rs ${priceDetail.shippingCharge}"


                                        binding.grandTotalAmount.text = "Rs ${priceDetail.grandTotal}"


                                        addressId.apply {
                                            binding.firstname.text = "$firstName $lastName"
                                            binding.address.text = "$houseNumber $area $country $state $city $zipCode"
                                        }

                                    }

                                }


                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            binding.orderLL.isVisible = false
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@OrderReviewActivity)
                            }

                        }


                        is Resource.Empty -> {

                        }


                    }


                }

            }
        }
    }


    private fun setCatalogueAdaptor(products: List<CartViewProducts>) {
        binding.productCartRecycler.layoutManager = LinearLayoutManager(this)
        val adapter = ProductCartAdapter(this,products,this,"")
        binding.productCartRecycler.adapter = adapter

    }

    override fun openSizeDetails(sizeValue: List<SizeValueData>, id: String) {

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

    }

    override fun deleteItemFromCart(productId: String, sizeId: String, position: Int) {

    }

    private fun cartCheckoutResponseObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.checkOutResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(this@OrderReviewActivity)
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode== 200) {

                                    if (response.data.resultCheckOutCart.orderId.isNotEmpty()){
//                                        val intent = Intent(this@OrderReviewActivity, PaymentWebViewActivity::class.java)
//                                        intent.putExtra("orderId",response.data.resultCheckOutCart.orderId)
//                                        intent.putExtra("fromScreen","Cart")
//                                        startActivity(intent)
//                                        finish()
//                                        AndroidExtension.alertBox("COMING SOON...", this@OrderReviewActivity)
                                      startPayment(checkOutId = response.data.resultCheckOutCart.orderId)
                                    }
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@OrderReviewActivity)
                            }
                        }


                        is Resource.Empty -> {

                        }


                    }


                }

            }
        }
    }



    private fun startPayment(checkOutId: String) {

        if (checkOutId == ""){
            Toast.makeText(this, "Please Try Again", Toast.LENGTH_SHORT).show()
            return
        }

        val co = Checkout()



        try {

            val options = JSONObject()
            options.put("name", "Fit Hun")

            options.put("image", "https://s3-for-backend.s3.amazonaws.com/ic_launcher_square.png")
//            options.put("theme.color", )
            options.put("currency", "INR")
            options.put("order_id", checkOutId)

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            co.open(this, options)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {



        finishAfterTransition()
        startActivity(Intent(this,OrderHistoryActivity::class.java))
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        when (p0) {
            5 -> {
                AndroidExtension.alertBox("Payment cancelled",this)
            }

            2 -> {
                AndroidExtension.alertBox("Please check your internet connection",this)
            }
        }
    }






}