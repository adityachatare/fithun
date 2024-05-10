package com.fithun.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.fithun.api.Constants.NO_INTERNET
import com.fithun.api.responses.BrandListResponse
import com.fithun.api.responses.CartViewResponse
import com.fithun.api.responses.CheckOutCartResponse
import com.fithun.api.responses.CommonResponse
import com.fithun.api.responses.PojoClass
import com.fithun.api.responses.ProductListResponse
import com.fithun.api.responses.ProductViewResponse
import com.fithun.repository.DreamWalkRepository
import com.fithun.utils.AndroidExtension
import com.fithun.utils.NetworkHelper
import com.fithun.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel

class ProductFlowViewModel @Inject constructor(app:Application, private val repo:DreamWalkRepository, private val networkHelper: NetworkHelper):AndroidViewModel(app) {

    private val productListData: MutableStateFlow<Resource<ProductListResponse>> = MutableStateFlow(Resource.Empty())
    val productListResponseData: StateFlow<Resource<ProductListResponse>> = productListData


    private val productViewData: MutableStateFlow<Resource<ProductViewResponse>> = MutableStateFlow(Resource.Empty())
    val productViewResponseData: StateFlow<Resource<ProductViewResponse>> = productViewData


    private val addToCartData: MutableStateFlow<Resource<CommonResponse>> = MutableStateFlow(Resource.Empty())
    val addToCartResponseData: StateFlow<Resource<CommonResponse>> = addToCartData


    private val viewCartData: MutableStateFlow<Resource<CartViewResponse>> = MutableStateFlow(Resource.Empty())
    val viewCartResponseData: StateFlow<Resource<CartViewResponse>> = viewCartData


    private val updateCartData: MutableStateFlow<Resource<CommonResponse>> = MutableStateFlow(Resource.Empty())
    val updateCartResponseData: StateFlow<Resource<CommonResponse>> = updateCartData


    private val updateCartQtyData: MutableStateFlow<Resource<CommonResponse>> = MutableStateFlow(Resource.Empty())
    val updateCartQtyResponseData: StateFlow<Resource<CommonResponse>> = updateCartQtyData


    private val deleteProductData: MutableStateFlow<Resource<CommonResponse>> = MutableStateFlow(Resource.Empty())
    val deleteProductResponseData: StateFlow<Resource<CommonResponse>> = deleteProductData


    private val brandListData: MutableStateFlow<Resource<BrandListResponse>> = MutableStateFlow(Resource.Empty())
    val brandListResponseData: StateFlow<Resource<BrandListResponse>> = brandListData


    private val checkOutData: MutableStateFlow<Resource<CheckOutCartResponse>> = MutableStateFlow(Resource.Empty())
    val checkOutResponseData: StateFlow<Resource<CheckOutCartResponse>> = checkOutData





    // Product List Api


    fun productListApi(token :String,page :Int,limit :Int, search :String, brandId:ArrayList<String>,priceRange:String) = viewModelScope.launch {
        productListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.listProductApi(token=token, page=page, limit=limit, search=search,brandId=brandId, priceRange = priceRange)
                .catch { e ->
                    productListData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    productListData.value = getProductResponse(data)
                }
        } else {
            productListData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun getProductResponse(response: Response<ProductListResponse>): Resource<ProductListResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }

    // Product View Api


    fun productViewApi(token :String,id :String) = viewModelScope.launch {
        productViewData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.viewProductApi(token=token, id = id)
                .catch { e ->
                    productViewData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    productViewData.value = viewProductResponse(data)
                }
        } else {
            productViewData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun viewProductResponse(response: Response<ProductViewResponse>): Resource<ProductViewResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }

    // Add To Cart Api


    fun addToCartApi(token: String,productId: String,sizeValueId: String,quantity: String) = viewModelScope.launch {
        addToCartData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.addToCartApi(token=token,productId=productId, sizeValueId=sizeValueId, quantity=quantity)
                .catch { e ->
                    addToCartData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    addToCartData.value = addCartProductResponse(data)
                }
        } else {
            addToCartData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun addCartProductResponse(response: Response<CommonResponse>): Resource<CommonResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }


    // View Cart Api


    fun viewCartApi(token: String,sortByFreightCharges:Boolean,sortByEarliestDelivery:Boolean,sortByRating:Boolean) = viewModelScope.launch {
        viewCartData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.viewCartApi(token=token,sortByFreightCharges, sortByEarliestDelivery, sortByRating)
                .catch { e ->
                    viewCartData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    viewCartData.value = viewCartDataCartProductResponse(data)
                }
        } else {
            viewCartData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun viewCartDataCartProductResponse(response: Response<CartViewResponse>): Resource<CartViewResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }


    // update Cart Api


    fun updateCartApi(token: String,productId:String,sizeValueId:String) = viewModelScope.launch {
        updateCartData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.updateCartApi(token=token,productId=productId,sizeValueId=sizeValueId)
                .catch { e ->
                    updateCartData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    updateCartData.value = updateCartResponseHandle(data)
                }
        } else {
            updateCartData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun updateCartResponseHandle(response: Response<CommonResponse>): Resource<CommonResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }

    // update Cart Api


    fun updateQuantityApi(token: String,productId:String,sizeValueId:String,quantity:Int,quantityType:String) = viewModelScope.launch {
        updateCartQtyData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.updateQuantityApi(token=token,productId=productId,sizeValueId=sizeValueId,quantity=quantity,quantityType=quantityType)
                .catch { e ->
                    updateCartQtyData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    updateCartQtyData.value = updateCartQtyResponseHandle(data)
                }
        } else {
            updateCartQtyData.value = Resource.Error(NO_INTERNET)
        }

    }


    private fun updateCartQtyResponseHandle(response: Response<CommonResponse>): Resource<CommonResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }




    // delete Cart Api


    fun deleteProductApi(token: String,productId:String,sizeValueId:String) = viewModelScope.launch {
        deleteProductData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.deleteItemFromCartApi(token=token,productId=productId,sizeValueId=sizeValueId)
                .catch { e ->
                    deleteProductData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    deleteProductData.value = deleteProductResponseHandle(data)
                }
        } else {
            deleteProductData.value = Resource.Error(NO_INTERNET)
        }

    }


    private fun deleteProductResponseHandle(response: Response<CommonResponse>): Resource<CommonResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }




    // brand list Cart Api


    fun brandListApi(token: String) = viewModelScope.launch {
        brandListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.listAllBrandApi(token=token)
                .catch { e ->
                    brandListData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    brandListData.value = brandListResponseHandle(data)
                }
        } else {
            brandListData.value = Resource.Error(NO_INTERNET)
        }

    }


    private fun brandListResponseHandle(response: Response<BrandListResponse>): Resource<BrandListResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }

    // CheckOut Cart Api


    fun checkOutCartApi(token: String, paymentUrl:String) = viewModelScope.launch {
        checkOutData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.checkoutCartApi(token=token,paymentUrl=paymentUrl)
                .catch { e ->
                    checkOutData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    checkOutData.value = checkOutResponseHandle(data)
                }
        } else {
            checkOutData.value = Resource.Error(NO_INTERNET)
        }

    }


    private fun checkOutResponseHandle(response: Response<CheckOutCartResponse>): Resource<CheckOutCartResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }




}



