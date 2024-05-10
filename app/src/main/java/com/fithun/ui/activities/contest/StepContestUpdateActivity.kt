package com.fithun.ui.activities.contest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.fithun.R
import com.fithun.api.responses.ViewCurrentPriceAndExpectedPool
import com.fithun.clickInterfaces.ContestEndClick
import com.fithun.ui.adapter.ContestViewPagerAdapter
import com.fithun.ui.bottomSheet.WalletDialog
import com.fithun.utils.AndroidExtension
import com.fithun.utils.DateFormat
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.displayToast
import com.fithun.viewModel.ContestViewModel
import com.fithun.databinding.ActivityStepContestUpdateBinding
import com.fithun.ui.bottomSheet.ContestEndDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@AndroidEntryPoint
class StepContestUpdateActivity : AppCompatActivity(), ContestEndClick {

    private val viewModel: ContestViewModel by viewModels()

    private var job: Job? = null

    lateinit var adapter : ContestViewPagerAdapter

    private lateinit var binding: ActivityStepContestUpdateBinding
    var list : ArrayList<ViewCurrentPriceAndExpectedPool> = arrayListOf()


    private var selectedPageLeaderBoard = 0


    var token = ""
    var endDate = ""
    var startDate = ""
    private var startTime = ""
    private var heading = ""
    var contestId = ""
    var isSeeLive = false
    private var typeSpot :Boolean = false

    private var date: Date? = null
    private val inputFormat = "HH:mm"
    private var dateCompareOne: Date? = null
    private var dateCompateTwo: Date? = null

    private var unitRequest = ""

    private var kms = ""


    private var isLiveUpdateData  = false
    private var contestEnd  = false
    private var conteststart  = false

    private var inputParser = SimpleDateFormat(inputFormat, Locale.US)
    var countDownTimer: CountDownTimer? = null



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStepContestUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade


        intent.getStringExtra("id")?.let { contestId=it }
        intent.getStringExtra("endDate")?.let { endDate=it }
        intent.getStringExtra("startDate")?.let { startDate=it }
        intent.getStringExtra("heading")?.let { heading=it }

        binding.contestName.text = heading

        token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString()

        viewModel.joinContestViewApi(token=token,id=contestId)


        binding.tablayout.removeAllTabs()
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Winnings"))
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Leaderboard"))
        binding.tablayout.tabGravity = TabLayout.GRAVITY_FILL

        binding.backButton.setOnClickListener {
            if (isSeeLive) {
                isSeeLive = false
                isLiveUpdateData = false
                binding.seeLiveUpdate.isVisible = true
                binding.UpdateDetailsCV.isVisible = true
                binding.LiveDetailsCV.isVisible = false
                binding.heading.text = "Step Contest"
            } else {
                finishAfterTransition()
            }
        }

        binding.seeLiveUpdate.setOnClickListener {
            isLiveUpdateData = true
            isSeeLive = true
            binding.heading.text = "Live Update"
            binding.seeLiveUpdate.isVisible = false
            binding.UpdateDetailsCV.isVisible = false
            binding.LiveDetailsCV.isVisible = true

        }

        lifecycleScope.launch{
            updatingScreen()
        }






        countDownTimerForBanner()
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isSeeLive) {
                    isSeeLive = false
                    isLiveUpdateData = false
                    binding.seeLiveUpdate.isVisible = true
                    binding.UpdateDetailsCV.isVisible = true
                    binding.LiveDetailsCV.isVisible = false
                    binding.heading.text = "Step Contest"
                } else {
                    finishAfterTransition()
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)


        viewContestResponseObserver()
        myRankObserver()

    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun countDownTimerForBanner() {
        val (formattedStartDate, formattedStartTime) = DateFormat.dateForCountdown(startDate)
        val (formattedEndDate, formattedEndTime) = DateFormat.dateForCountdown(endDate)

        val currentDate = LocalDate.now()
        val formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val compareCurrentDate =  currentDate.format(formatterDate)

        val currentTime = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("hh:mm:ss a")
        val finalFormattedTime = currentTime.format(formatter)
        val formatterTime = DateTimeFormatter.ofPattern("hh:mm:ss a")
        val compareCurrentTime = currentTime.format(formatterTime).uppercase()


        val dateDifferenceInStart = getDateDifference(compareCurrentDate,formattedStartDate)




        if (compareCurrentDate == formattedStartDate){
            timeOfStartEndContest(formattedStartTime,compareCurrentTime,formattedEndDate,formattedEndTime)

        }else if (dateDifferenceInStart.days == 1){
            binding.titleTime.text = "Contest Starts from:"
            binding.endTime.text = "Tomorrow"
            binding.titleTimeLive.text = "Contest Starts from:"
            binding.endTimeLive.text = "Tomorrow"
        }else {
            binding.titleTime.text = "Contest Starts from:"
            binding.endTime.text = DateFormat.contestDateFormat(formattedStartDate)
            binding.titleTimeLive.text = "Contest Starts from:"
            binding.endTimeLive.text = DateFormat.contestDateFormat(formattedStartDate)
        }

    }


    private fun setTabs() {


        adapter = ContestViewPagerAdapter(this,  binding.tablayout.tabCount,contestId, typeSpot, isJoinedUser = true)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tablayout, binding.viewPager) { tab, position ->

            when (position) {
                0 -> tab.text = "Winnings"
                1 -> tab.text = "Leaderboard"

            }

        }.attach()

        binding.viewPager.setCurrentItem(selectedPageLeaderBoard, false)


        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                selectedPageLeaderBoard  = position
            }
        })
    }

    private fun viewContestResponseObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.joinedContestResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            if(!isLiveUpdateData) {
                                Progress.start(this@StepContestUpdateActivity)
                            }
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                response.data?.apply {
                                    if(responseCode== 200) {
                                        try {
                                            list.clear()
                                            result.apply {

                                                startTime = startDate


                                                val poolValue = 0.2 * (entryFee.toInt()* maximumSpot.toInt())
                                                val finalAmount = (entryFee.toInt()* maximumSpot.toInt()) - poolValue
                                                val setValue = finalAmount.toInt()

                                                binding.poolPrize.text=  "₹$setValue"
                                                binding.poolPrizeLive.text=  "₹$setValue"


                                            }


                                            setTabs()
                                        }catch (e:Exception){
                                            e.printStackTrace()
                                        }




                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@StepContestUpdateActivity)
                            }
                        }

                        is Resource.Empty -> {

                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updatingScreen(){

            job = CoroutineScope(Dispatchers.Main).launch {
                while (isActive) {


                    if (isLiveUpdateData){
                        setTabs()
                        viewModel.myRankApi(token=token,id=contestId)
                    }

                    val currentSpeed = SavedPrefManager.getFloatPreferences(this@StepContestUpdateActivity, SavedPrefManager.Speed)
                    val kmPerHourSpeed = currentSpeed * 3.6
                    val totalDistanceKm = SavedPrefManager.getFloatPreferences(this@StepContestUpdateActivity, SavedPrefManager.totalDistanceForContest) / 1000
                    val stepsCount = (SavedPrefManager.getFloatPreferences(this@StepContestUpdateActivity, SavedPrefManager.totalDistanceForContest) * 1.3).roundToInt()


                    binding.totalKm.text = "${convertDistance(totalDistanceKm.toDouble())} $unitRequest"

                    kms = formatDynamicValue(kmPerHourSpeed.toString(), 2)
                    if (kmPerHourSpeed >= 0.3) {
                        binding.kmPerHour.text = kms + "Km/h"
                    } else {
                        binding.kmPerHour.text = kms + "m/s"
                    }
                    binding.stepsCount.text = formatDynamicValue(stepsCount.toString(), 2)
                    delay(5000)
                }

                }
    }




    override fun onDestroy() {
        super.onDestroy()
        try {
            job?.cancel()
            countDownTimer!!.cancel()
        }catch (e:Exception){
            e.printStackTrace()
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


    private fun formatDynamicValue(value: String, decimalPlaces: Int): String {
        val parts = value.split(".")

        return if (parts.size == 2 && parts[1].length > decimalPlaces) {
            val truncatedDecimal = parts[1].substring(0, decimalPlaces)
            "${parts[0]}.${truncatedDecimal}"
        } else {
            value // The value is already properly formatted
        }
    }


    private fun calculateRemainingTime(targetTime: String): Long {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        // Get the current time
        val currentTime = Calendar.getInstance()
        val currentDateString = sdf.format(currentTime.time)
        val currentDate = sdf.parse(currentDateString)

        // Parse the target time
        val targetDate = sdf.parse(targetTime)

        // Calculate the remaining time
        val remainingTime = targetDate!!.time - currentDate!!.time

        return if (remainingTime >= 0) remainingTime else 0
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




    private fun timeOfStartEndContest(
        formattedStartTime: String,
        compareCurrentTime: String,
        formattedEndDate: String,
        formattedEndTime: String
    ) {
        val sdf = SimpleDateFormat("hh:mm:ss a", Locale.US)


        try {
            val startTime = sdf.parse(formattedStartTime)
            val currentTime = sdf.parse(compareCurrentTime)

            // Compare times
            if (startTime != null && currentTime != null) {
                val comparisonResult = currentTime.compareTo(startTime)

                if (comparisonResult >= 0) {
                    binding.seeLiveUpdate.isVisible = true


                    val convert12HourTo24Hour = DateFormat.convert12HourTo24Hour(formattedEndTime)

                    val remainingTimeMillis = calculateRemainingTime(convert12HourTo24Hour)

                    countDownTimer = object : CountDownTimer(remainingTimeMillis, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val totalSeconds = millisUntilFinished / 1000
                            val hours = totalSeconds / 3600
                            val minutes = (totalSeconds % 3600) / 60
                            val seconds = totalSeconds % 60

                            val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                            binding.titleTime.text = "Contest Ends In"
                            binding.endTime.text = formattedTime
                            binding.titleTimeLive.text = "Contest Ends In"
                            binding.endTimeLive.text = formattedTime

                        }

                        override fun onFinish() {
                            binding.seeLiveUpdate.isVisible = false
                            setTabs()
                            contestEnd =  true
                            isSeeLive =  false
                            isLiveUpdateData = false

                            binding.titleTime.text = "Contest Finished"
                            binding.endTime.text = ""
                            binding.titleTimeLive.text = "Contest Finished"
                            binding.endTimeLive.text = ""
                            supportFragmentManager.let {
                                ContestEndDialog("Contest Completed!", "We're excited to announce that the contest has come to an end! \uD83C\uDF89 Thank you for your enthusiastic participation. Your contribution has made this event truly special.\n" +
                                        "\n" +
                                        "Stay tuned for the results, and we appreciate your continued support. We hope you had a fantastic time, and we look forward to having you join us in future contests.",this@StepContestUpdateActivity).show(it, "MyCustomFragment")
                            }
                        }
                    }

                    countDownTimer!!.start()
                } else {
                    val convert12HourTo24Hour = DateFormat.convert12HourTo24Hour(formattedStartTime)

                    val remainingTimeMillis = calculateRemainingTime(convert12HourTo24Hour)
                    countDownTimer = object : CountDownTimer(remainingTimeMillis, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val totalSeconds = millisUntilFinished / 1000
                            val hours = totalSeconds / 3600
                            val minutes = (totalSeconds % 3600) / 60
                            val seconds = totalSeconds % 60

                            val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                            binding.titleTime.text = "Contest Starts In"
                            binding.endTime.text = formattedTime
                            binding.titleTimeLive.text = "Contest Starts In"
                            binding.endTimeLive.text = formattedTime
                        }

                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onFinish() {
                            conteststart=  true
                            countDownTimerForBanner()

                        }
                    }

                    countDownTimer!!.start()
                }
            } else {
                displayToast("Error parsing times")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            displayToast("Error comparing times")
        }
    }

    override fun contestEnds() {
        startActivity(Intent(this,CompletedContestActivity::class.java))
        finishAfterTransition()
    }


    private fun myRankObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.myRankDataResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            try {
                                if (response.data!!.responseCode == 200){
                                    binding.myRank.text = response.data.result.usersRank
                                }
                            } catch (e: Exception) {
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

}