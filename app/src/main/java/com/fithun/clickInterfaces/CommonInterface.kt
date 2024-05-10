package com.fithun.clickInterfaces

import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.fithun.api.responses.SizeValueData
import com.fithun.api.responses.SubCategory

interface AddressClick {
    fun addressClick(id: String)
    fun deleteClick(position: Int, id: String)
}

interface CommonClick {
    fun applyClick()
}


interface JoinStepContest {
    fun join(id: String?)
}

interface StateCityCommonClick {
    fun getCityState(data: String, flag: String, code: String)
}


interface SizeListener {
    fun sizeUnitListener(unit: String, size: String, price: Number, id: String)
}

interface GetUnitDetails {
    fun getUnitDetails(unit: String, size: String, price: Number, id: String)
}

interface HowToPlayCategory {
    fun getHowToPlayCategory(subCategoryData: ArrayList<SubCategory>)
}


interface LocationDenyInterface {

    fun openSettings(isForLocation: String)
}


interface AddProductCart {
    fun addToCartProduct(
        productId: String,
        productSizeValue: String,
        quantity: Int,
        buyNowClick: RelativeLayout,
        incDesign: LinearLayout,
        position: Int
    )

    fun addToCartProductTab(
        productId: String,
        productSizeValue: String,
        quantity: Int,
        buyNowClick: RelativeLayout,
        incDesign: LinearLayout,
        position: Int
    )


    fun incrementItem(
        productId: String,
        productSizeValue: String,
        quantity: Int,
        buyNowClick: RelativeLayout,
        incDesign: LinearLayout,
        position: String,
        stopLoading: LinearLayout,
        loaderLl: RelativeLayout,
        loader: LottieAnimationView
    )
    fun decrementItem(
        productId: String,
        productSizeValue: String,
        quantity: Int,
        buyNowClick: RelativeLayout,
        incDesign: LinearLayout,
        position: String,
        stopLoading: LinearLayout,
        loaderLl: RelativeLayout,
        loader: LottieAnimationView
    )




}


interface CartSizeDetailsClick {
    fun openSizeDetails(sizeValue: List<SizeValueData>, id: String)
    fun updateCartClick(
        productId: String,
        sizeId: String,
        qty: Int,
        type: String,
        loader: LottieAnimationView,
        stopLoading: LinearLayout,
        loaderLl: RelativeLayout
    )

    fun deleteItemFromCart(productId: String, sizeId: String, position: Int)
}


interface ProductFilterInterface {
    fun openBrands(brandsName: TextView)
    fun applyFilter(priceValues: String)
}


interface JoinContest {
    fun joinNow(id: String, endDate: String, startDate: String, heading: String, firstPrice: String)
}

interface ViewOrderHistory {
    fun viewOrder(orderId: String,orderStatus:String)
}

interface AddCoin {
    fun addCoin(price: String)
}


interface WalletFilter {
    fun filterWalletClick(startDate: String, endDate: String, type: String)

}


interface EventFilterClick {
    fun filterEvents(contestType: String, day: String, entryFee: String, spot: String)
}


interface ProductImagePreviewClick {
    fun viewImage(imagePosition:Int)
}

interface PaymentDoneListener {
    fun paymentDone()
    fun failedDone()

    fun cancelPayment()
}


interface ClickForTracking {
    fun trackOrder(awb: String)
}


interface ContestEndClick {
    fun contestEnds()
}

interface CheckVersionStatus{
    fun checkStatus()
}

interface HomeFragmentClickListener {
    fun productClickListener()
    fun contestClickListener()
}
