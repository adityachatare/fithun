package com.fithun.ui.bottomTab

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.fithun.R
import com.fithun.api.responses.ExpectedPricePool
import com.fithun.api.responses.ProductListDocs
import com.fithun.clickInterfaces.AddProductCart
import com.fithun.databinding.FragmentHomeBinding
import com.fithun.ui.activities.contest.StepContestActivity
import com.fithun.ui.activities.contest.StepContestUpdateActivity
import com.fithun.ui.activities.contest.ViewStepContestActivity
import com.fithun.ui.adapter.BannerAdapter
import com.fithun.ui.adapter.CatalogueAdapter
import com.fithun.ui.bottomSheet.WalletDialog
import com.fithun.uiModalClass.BannerImages
import com.fithun.utils.AndroidExtension
import com.fithun.utils.AndroidExtension.initLoader
import com.fithun.utils.DateFormat
import com.fithun.utils.ManageStack
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.SpeedRotationUpdater
import com.fithun.utils.displayToast
import com.fithun.utils.load
import com.fithun.utils.setTextViewValue
import com.fithun.viewModel.HomeFlowViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.time.Period
import java.util.*


import kotlin.math.roundToInt
import com.fithun.clickInterfaces.HomeFragmentClickListener

@AndroidEntryPoint

class HomeFragment : Fragment(), AddProductCart {

    private var _binding: FragmentHomeBinding? =  null

    private val viewModel: HomeFlowViewModel by viewModels()
    private val binding get() = _binding!!


    private var clickListener: HomeFragmentClickListener? = null


    private lateinit var speedRotationUpdater: SpeedRotationUpdater

    lateinit var  adapter : CatalogueAdapter
    var token = ""
    private var docs: List<ProductListDocs> = listOf()

    private val sliderHandler: Handler = Handler()

    private var unitRequest = ""


    private lateinit var countDownTimer: CountDownTimer


    private var stopLoadingProgress: LinearLayout? = null
    private var startLoading: RelativeLayout? = null
    private var loaderAnimation: LottieAnimationView? = null





    companion object{
        @JvmStatic
        fun newInstance() = HomeFragment()
    }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.AccessToken).toString()

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getBannerDataApi()
            viewModel.upComingContestApi(token=token)
            viewModel.winnerBannerList(token = token)
            val brandId= ArrayList<String>()
            viewModel.productListApi(token=token, page=1, limit=10, search="", brandId = brandId, priceRange = "" )
            binding.swipeRefresh.isRefreshing = false
        }



        viewModel.getBannerDataApi()


        viewModel.winnerBannerList(token = token)

        val brandId= ArrayList<String>()

        viewModel.productListApi(token=token, page=1, limit=10, search="", brandId = brandId, priceRange = "" )

        binding.ViewAllContest.setOnClickListener {
            clickListener?.contestClickListener()

        }

        binding.ViewAllCatalogue.setOnClickListener {
            clickListener?.productClickListener()
        }



        speedRotationUpdater =  SpeedRotationUpdater(requireActivity(),binding, 1000)
        speedRotationUpdater.startUpdating()

        val sliderRunnable = Runnable {
            lifecycleScope.launch {
                val currentItem = binding.storeViewpager.currentItem
                val lastItem = binding.storeViewpager.adapter?.itemCount?.minus(1) ?: 0

                if (currentItem == lastItem) {
                    binding.storeViewpager.currentItem = 0
                } else {
                    binding.storeViewpager.currentItem = currentItem + 1
                }
            }
        }

        val callback: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 2000)
            }
        }

        binding.storeViewpager.registerOnPageChangeCallback(callback)



        binding.speedoMeter.setOnClickListener {

            parentFragmentManager.let {
                WalletDialog("Speed Meter!", "Walk on a road to count your Steps").show(it, "MyCustomFragment")
            }

        }





        return binding.root
    }


    override fun onStart() {
        super.onStart()
        viewModel.upComingContestApi(token=token)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        speedRotationUpdater.startUpdating()
        val stepsCount = (SavedPrefManager.getFloatPreferences(requireContext(), SavedPrefManager.totalDistance) * 1.3).roundToInt()
        val distanceWalked =  SavedPrefManager.getFloatPreferences(requireContext(),SavedPrefManager.totalDistance)/1000
        val totalStepCount = formatDynamicValue(stepsCount.toString(), 2)
        val totalDistanceWalked =convertDistance(distanceWalked.toDouble())
        val c = Calendar.getInstance().time



        val currentDate = LocalDate.now()
        val formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val compareCurrentDate =  currentDate.format(formatterDate)
        if (totalDistanceWalked == "0" && totalStepCount =="0"){
//            viewModel.viewStepsApi(token=token, contestId = "",date= compareCurrentDate)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllBanner()
        productResponseObserver()
        productAddToCartResponseObserver()
        createStepObserver()
        viewStepObserver()
        getUpcomingContest()
        getWinnerList()
        updateCartObserver()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getUpcomingContest(){
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.upcommingContestResponseData.collect { response ->
                    when(response){

                        is Resource.Loading -> {
                            binding.upcomingContestView.isVisible = false
                            binding.upcomingHeader.isVisible = false
                        }

                        is Resource.Success -> {
                            try {
                                try {
                                    if (response.data?.responseCode == 200){

                                        with(response.data.result){

                                            if(!heading.isNullOrEmpty()) {
                                                binding.upcomingContestView.visibility = View.VISIBLE
                                                binding.upcomingHeader.visibility = View.VISIBLE
                                                binding.contestImage.load(contestImage)
                                                binding.contestTitle.setTextViewValue(heading)
                                                binding.contestSubTitle.setTextViewValue(subHeading)


                                                val list : ArrayList<ExpectedPricePool> = arrayListOf()

                                                if(spot.toInt() > 0 ){
                                                    list.addAll(current_pricePool)


                                                }else{
                                                    list.addAll(expectedPricePool)
                                                }

                                                if (list.isNotEmpty()){
                                                    binding.firstPrice.text = "First Price ₩${list[0].price}"
                                                }else{
                                                    binding.firstPrice.text = "First Price ₩$firstPrice"
                                                }


                                                val poolValue = 0.2 * (entryFee.toInt()* maximumSpot.toInt())
                                                val finalAmount = (entryFee.toInt()* maximumSpot.toInt()) - poolValue
                                                val setValue = finalAmount.toInt()
                                                binding.poolPrice.setTextViewValue("₩${setValue}")




                                                binding.entryPrice.setTextViewValue("₩${entryFee}")
                                                binding.spot.setTextViewValue("$maximumSpot")
                                                binding.startTime.setTextViewValue(DateFormat.convert24To12(startDate))
                                                binding.endTime.setTextViewValue(DateFormat.convert24To12(endDate))



                                                val (formattedDate, formattedTime) = DateFormat.dateForCountdown(startDate)

                                                val convert12HourTo24Hour = DateFormat.convert12HourTo24Hour(formattedTime)


                                                val currentDate = LocalDate.now()
                                                val formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                                val compareCurrentDate =  currentDate.format(formatterDate)

                                                val currentTime = LocalTime.now()
                                                val formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss")
                                                val compareCurrentTime = currentTime.format(formatterTime)
                                                val formatter = DateTimeFormatter.ofPattern("hh:mm a")
                                                val finalFormattedTime = currentTime.format(formatter)




                                                if (status.uppercase() == "CANCELED"){
                                                    binding.joinNowClick.isEnabled = false
                                                    binding.joinNowClickButton.text = "Canceled"
                                                    binding.countDownTimer.text = "Canceled"
                                                    binding.joinNowClick.setBackgroundResource(R.drawable.red_background)
                                                }else{
                                                    binding.joinNowClick.isEnabled = true


                                                    if (joined){
                                                        binding.joinNowClick.setBackgroundResource(R.drawable.green_background)
                                                        binding.joinNowClickButton.text = "View"
                                                    }else{
                                                        binding.joinNowClick.setBackgroundResource(R.drawable.red_background)
                                                        binding.joinNowClickButton.text = "Join"
                                                    }

                                                    val dateDifference = getDateDifference(compareCurrentDate,formattedDate)

                                                    if (compareCurrentDate == formattedDate){
                                                        if (joined){
                                                            val remainingTimeMillis = DateFormat.calculateRemainingTime(convert12HourTo24Hour)
                                                            startCountdown(remainingTimeMillis)
                                                        }else{
                                                            "Today".also { binding.countDownTimer.text = it }
                                                        }

                                                    }else if (dateDifference.days == 1){
                                                        "Tomorrow".also { binding.countDownTimer.text = it }
                                                    }else {
                                                        binding.countDownTimer.text = DateFormat.contestDateFormat(formattedDate)
                                                    }


                                                }



                                                binding.joinNowClick.setOnClickListener {
                                                    if (!joined){
                                                        val intent = Intent(requireContext(), ViewStepContestActivity::class.java)
                                                        intent.putExtra("id", id)
                                                        intent.putExtra("endDate", endDate)
                                                        intent.putExtra("startDate", startDate)
                                                        intent.putExtra("heading", heading)
                                                        intent.putExtra("firstPrice", binding.firstPrice.text.toString())
                                                        startActivity(intent)
                                                        return@setOnClickListener
                                                    }


                                                    val intent = Intent(requireContext(), StepContestUpdateActivity::class.java)
                                                    intent.putExtra("id", id)
                                                    intent.putExtra("endDate", endDate)
                                                    intent.putExtra("startDate", startDate)
                                                    intent.putExtra("heading", heading)
                                                    startActivity(intent)



                                                }

                                                binding.ViewAllContest.setOnClickListener {
                                                    val intent = Intent(requireContext(),
                                                        StepContestActivity::class.java)
                                                    intent.putExtra("categoryId",categoryId.id)
                                                    startActivity(intent)
                                                }


                                            }else{
                                                binding.upcomingContestView.visibility = View.GONE
                                                binding.upcomingHeader.visibility = View.GONE
                                            }

                                        }






                                    }
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }


                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error ->{
                            binding.upcomingContestView.isVisible = false
                            binding.upcomingHeader.isVisible = false
                        }

                        is Resource.Empty ->{

                        }

                    }

                }

            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)


    private fun getAllBanner() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.allBannerResponseData.collect { response ->
                    when(response){

                        is Resource.Loading ->{
                            Progress.start(requireContext())
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                try {
                                    if (response.data?.responseCode == 200){
                                        BannerImages.data = emptyList()
                                        BannerImages.data =  response.data.result
                                        setBannerImageAdaptor()

                                    }
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }

                            }catch (e:Exception){
                                
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error ->{
                            Progress.stop()
                            displayToast(response.message)
                        }

                        is Resource.Empty ->{

                        }
                    }
                }
            }
        }
    }

    private fun getWinnerList() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.winnerBannerListResponseData.collect { response ->
                    when(response){

                        is Resource.Loading ->{
                            binding.winnerBanner.isVisible = false
                        }

                        is Resource.Success -> {
                            try {
                                if (response.data?.responseCode == 200){
                                    response.data.apply {
                                        if (result.isNotEmpty()){
                                            if (result[0].winnerList.isNotEmpty()){
                                                binding.winnerBanner.isVisible = true
                                                Glide.with(this@HomeFragment).load(result[0].winnerList.getOrNull(0)!!._id.profilePic).placeholder(R.drawable.placeholder).into(binding.userProfile)
                                                binding.username.text = result[0].winnerList.getOrNull(0)!!._id.userName
                                                binding.stepsCounts.text = result[0].winnerList.getOrNull(0)!!.totalSteps.toString()
                                            }else{
                                                binding.winnerBanner.isVisible = false
                                            }

                                        }else{
                                            binding.winnerBanner.isVisible = false
                                        }
                                    }

                                }
                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error ->{
                            binding.winnerBanner.isVisible = false
                        }

                        is Resource.Empty ->{

                        }

                    }

                }

            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        try {
            speedRotationUpdater.stopUpdating()
        }catch (e:Exception){
            e.printStackTrace()
        }

    }


    private fun setBannerImageAdaptor() {
        val bannerAdapter = BannerAdapter(requireContext(), BannerImages.data)
        binding.storeViewpager.adapter = bannerAdapter
        binding.indicator.setViewPager(binding.storeViewpager)

    }


    private fun productResponseObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.productListResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200) {
                                    docs =response.data.result.docs
                                    setCatalogueAdaptor()
                                }


                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            displayToast(response.message)

                        }

                        is Resource.Empty -> {

                        }

                    }

                }

            }
        }
    }


    private fun setCatalogueAdaptor() {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.productCatalogue.layoutManager = layoutManager
        adapter = CatalogueAdapter(requireContext(),docs, productClick = this)
        binding.productCatalogue.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun addToCartProduct(
        productId: String,
        productSizeValue: String,
        quantity: Int,
        buyNowClick: RelativeLayout,
        incDesign: LinearLayout,
        position: Int
    ) {
//        buyNowClick.isVisible = true
//        incDesign.isVisible = false
        docs[position].quantity = 1
        adapter.notifyDataSetChanged()

        viewModel.addToCartApi(token=token,productId=productId, sizeValueId=productSizeValue, quantity="$quantity")
    }

    override fun addToCartProductTab(
        productId: String,
        productSizeValue: String,
        quantity: Int,
        buyNowClick: RelativeLayout,
        incDesign: LinearLayout,
        position: Int
    ) {

    }

    override fun incrementItem(
        productId: String,
        productSizeValue: String,
        quantity: Int,
        buyNowClick: RelativeLayout,
        incDesign: LinearLayout,
        position: String,
        stopLoading: LinearLayout,
        loaderLl: RelativeLayout,
        loader: LottieAnimationView
    ) {
        stopLoadingProgress= stopLoading
        startLoading =  loaderLl
        loaderAnimation = loader

        viewModel.updateQuantityApi(
            token = token,
            productId = productId,
            sizeValueId = position,
            quantity = quantity,
            quantityType  ="increment"
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun decrementItem(
        productId: String,
        productSizeValue: String,
        quantity: Int,
        buyNowClick: RelativeLayout,
        incDesign: LinearLayout,
        position: String,
        stopLoading: LinearLayout,
        loaderLl: RelativeLayout,
        loader: LottieAnimationView
    ) {

        if (quantity == 1){
            buyNowClick.isVisible = true
            incDesign.isVisible = false
            adapter.notifyDataSetChanged()
        }


        stopLoadingProgress= stopLoading
        startLoading =  loaderLl
        loaderAnimation = loader
        viewModel.updateQuantityApi(
            token = token,
            productId = productId,
            sizeValueId = position,
            quantity = quantity,
            quantityType = "decrement"
        )
    }

    private fun productAddToCartResponseObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.addToCartResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(requireContext())
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.statusCode == 200) {
                                    displayToast(response.data.responseMessage)
//                                    startActivity(Intent(requireContext(),
//                                        ProductCartActivity::class.java))
                                }


                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it, requireContext())
                            }

                        }


                        is Resource.Empty -> {

                        }


                    }


                }

            }
        }
    }


    private fun formatDynamicValue(value: String, decimalPlaces: Int): String {
        val parts = value.split(".")

        return if (parts.size == 2 && parts[1].length > decimalPlaces) {
            val truncatedDecimal = parts[1].substring(0, decimalPlaces)
            "${parts[0]}.${truncatedDecimal}"
        } else {
            value // The value is already properly formatted
        }
    }



    private fun convertDistance(value: Double): String {
        return if (value >= 1.0) {
            unitRequest = "Km"
            "${"%.1f".format(value)}"
        } else {
            unitRequest = "m"
            "${(value * 1000).toInt()}"
        }
    }


    private fun createStepObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.addStepsResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {

                        }


                        is Resource.Success -> {

                        }

                        is Resource.Error -> {


                        }


                        is Resource.Empty -> {


                        }


                    }


                }

            }
        }
    }

    private fun viewStepObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.viewStepsResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {

                        }


                        is Resource.Success -> {
                            try {
                                if (response.data?.responseCode == 200){

                                    response.data.result.apply {


                                        if (unit.lowercase() == "km"){
                                            val updatedValues = distanceWalked.toFloat()*1000
                                            SavedPrefManager.saveFloatPreferences(requireContext(),SavedPrefManager.totalDistance,updatedValues)

                                        }else{
                                            if (distanceWalked.isNotEmpty()){
                                                SavedPrefManager.saveFloatPreferences(requireContext(),SavedPrefManager.totalDistance,distanceWalked.toFloat())
                                            }

                                        }



                                        SavedPrefManager.saveStringPreferences(requireContext(),SavedPrefManager.Unit,unit)
                                        SavedPrefManager.saveStringPreferences(requireContext(),SavedPrefManager.stepCount,stepCount)

                                    }



                                }
                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {

                        }

                        is Resource.Empty -> {

                        }

                    }

                }

            }
        }
    }






    @RequiresApi(Build.VERSION_CODES.O)
    private fun startCountdown(remainingTimeMillis: Long) {
        countDownTimer = object : CountDownTimer(remainingTimeMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val totalSeconds = millisUntilFinished / 1000
                val hours = totalSeconds / 3600
                val minutes = (totalSeconds % 3600) / 60
                val seconds = totalSeconds % 60

                val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                binding.countDownTimer.text = formattedTime
                println(formattedTime)
            }

            override fun onFinish() {
                binding.countDownTimer.text = "Running!"

            }
        }

        countDownTimer.start()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDateDifference(currentDate: String, formattedDate: String): Period {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val currentDateParsed = dateFormat.parse(currentDate)
        val formattedDateParsed = dateFormat.parse(formattedDate)

        val calendarCurrent = Calendar.getInstance()
        calendarCurrent.time = currentDateParsed!!

        val calendarFormatted = Calendar.getInstance()
        calendarFormatted.time = formattedDateParsed!!

        val years = calendarFormatted.get(Calendar.YEAR) - calendarCurrent.get(Calendar.YEAR)
        val months = calendarFormatted.get(Calendar.MONTH) - calendarCurrent.get(Calendar.MONTH)
        val days = calendarFormatted.get(Calendar.DAY_OF_MONTH) - calendarCurrent.get(Calendar.DAY_OF_MONTH)

        return Period.of(years, months, days)
    }


    override fun onPause() {
        super.onPause()
        try {
            if (::countDownTimer.isInitialized){
                countDownTimer.cancel()
            }


        }catch (e:Exception){
            e.printStackTrace()
        }

    }


    private fun updateCartObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.updateCartQtyResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            try {

                                if (response.data?.statusCode == 200) {
                                    startLoading!!.isVisible = false
                                    stopLoadingProgress!!.isVisible = true
                                    loaderAnimation!!.initLoader(false)
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            startLoading!!.isVisible = false
                            stopLoadingProgress!!.isVisible = true
                            loaderAnimation!!.initLoader(false)
                        }


                        is Resource.Empty -> {

                        }


                    }


                }

            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeFragmentClickListener) {
            clickListener = context
        } else {
            throw RuntimeException("$context must implement HomeFragmentClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        // Reset the click listener to avoid memory leaks
        clickListener = null
    }


}