package com.fithun.ui.activities.product

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.fithun.R
import com.fithun.api.responses.AddressResult
import com.fithun.clickInterfaces.AddressClick
import com.fithun.clickInterfaces.CommonClick
import com.fithun.databinding.ActivityChooseDeliveryAddressBinding
import com.fithun.ui.adapter.ChooseDeliveryAddressAdapter
import com.fithun.utils.AndroidExtension
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.displayToast
import com.fithun.viewModel.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChooseDeliveryAddressActivity : AppCompatActivity(), AddressClick,CommonClick {

    private lateinit var binding:ActivityChooseDeliveryAddressBinding

    private var dataAddress: ArrayList<AddressResult> = arrayListOf()
    private var positionDelete = 0
    private var addressId = ""
    private var addressIdRequest = ""
    var token = ""
    var isFrom = ""
    lateinit var adapterDeliveryAddress : ChooseDeliveryAddressAdapter

    private val viewModel : AddressViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseDeliveryAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        token = SavedPrefManager.getStringPreferences(this,SavedPrefManager.AccessToken).toString()

        intent.getStringExtra("isFrom")?.let { isFrom = it }



        if (isFrom == "Menu"){
            binding.heading.text = "My Address"
        }




        binding.backButton.setOnClickListener { finishAfterTransition() }

        binding.addNewAddress.setOnClickListener {
            val intent = Intent(this,AddAddressActivity::class.java)
            intent.putExtra("isFor","Add")
            startActivity(intent)
        }

        binding.NextButton.setOnClickListener {
            viewModel.addOrderAddressAPi(token=token, addressId = addressIdRequest)

        }

        deleteAddressObserver()
        getAddressListObserver()
        selectAddressObserver()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAddressApi(token=token)
        binding.NextButton.isVisible = false
    }

    private fun setDeliveryAddressAdaptor() {
        binding.chooseDeliveryAddress.layoutManager = LinearLayoutManager(this)
        adapterDeliveryAddress = ChooseDeliveryAddressAdapter(this, addressData = dataAddress, click = this,isFrom=isFrom)
        binding.chooseDeliveryAddress.adapter = adapterDeliveryAddress

    }

    override fun addressClick(id: String) {
        val isFilter =  dataAddress.filter { it.isSelected }
        addressIdRequest = id
        binding.NextButton.isVisible = isFilter.isNotEmpty() && isFrom != "Menu"

    }

    override fun deleteClick(position: Int,id:String) {
        positionDelete = position
        addressId = id
        AndroidExtension.showDialogDelete(this,this)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun applyClick() {
        dataAddress.removeAt(positionDelete)

        adapterDeliveryAddress.notifyItemRemoved(positionDelete)
        adapterDeliveryAddress.notifyItemRangeChanged(positionDelete, dataAddress.size)

        binding.NoDataFound.isVisible = dataAddress.isEmpty()

        val jsonObject = JsonObject()
        jsonObject.addProperty("_id",addressId)

        viewModel.deleteAddressApi(token=token,id= addressId)
    }

    private fun getAddressListObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getAddressResponseData.collect { response ->
                    when(response){

                        is Resource.Loading ->{
                            Progress.start(this@ChooseDeliveryAddressActivity)
                            binding.NoDataFound.isVisible = false
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200){
                                    dataAddress.clear()
                                    dataAddress =  response.data.result
                                    binding.NoDataFound.isVisible = dataAddress.isEmpty()
                                    setDeliveryAddressAdaptor()
                                }


                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error ->{
                            Progress.stop()
                            binding.NoDataFound.isVisible = true
                            binding.chooseDeliveryAddress.isVisible = false

                            response.message?.let {
                                AndroidExtension.alertBox(it, this@ChooseDeliveryAddressActivity)
                            }


                        }


                        is Resource.Empty ->{

                        }


                    }


                }

            }
        }
    }

    private fun deleteAddressObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.deleteResponseAddressData.collect { response ->
                    when(response){

                        is Resource.Loading ->{
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.statusCode == 200){


                                }


                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error ->{



                        }


                        is Resource.Empty ->{

                        }


                    }


                }

            }
        }
    }

    private fun selectAddressObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.addAddressForOrderResponseData.collect { response ->
                    when(response){

                        is Resource.Loading ->{
                            Progress.start(this@ChooseDeliveryAddressActivity)
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.statusCode == 200){
                                    displayToast(response.data.responseMessage)
                                    startActivity(Intent(this@ChooseDeliveryAddressActivity, OrderReviewActivity::class.java))
                                    finishAfterTransition()

                                }


                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error ->{
                            Progress.stop()

                        }


                        is Resource.Empty ->{

                        }


                    }


                }

            }
        }
    }



}