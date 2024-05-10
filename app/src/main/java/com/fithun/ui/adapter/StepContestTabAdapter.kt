package com.fithun.ui.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.fithun.R
import com.fithun.api.responses.ExpectedPricePool
import com.fithun.api.responses.ViewContestListDocs
import com.fithun.clickInterfaces.JoinContest
import com.fithun.databinding.UpcomingContestLayoutBinding
import com.fithun.ui.activities.contest.StepContestUpdateActivity
import com.fithun.utils.DateFormat
import com.fithun.utils.load
import com.fithun.utils.setTextViewValue
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import kotlin.math.log

class StepContestTabAdapter(
    var context: Context,
    private val contestDataList: ArrayList<ViewContestListDocs>,
    val click:JoinContest
) : RecyclerView.Adapter<StepContestTabAdapter.ViewHolder>() {
    private val inputFormat = "HH:mm"
    private var inputParser = SimpleDateFormat(inputFormat, Locale.US)

    var countDownTimer: CountDownTimer? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UpcomingContestLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
             val binding =  holder.binding
            contestDataList[position].apply {
                binding.contestImage.load(contestImage)
                binding.contestTitle.setTextViewValue(heading)
                binding.contestSubTitle.setTextViewValue(subHeading)

                var list : java.util.ArrayList<ExpectedPricePool> = arrayListOf()

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


                binding.poolPrice.setTextViewValue("₩$setValue")
                binding.entryPrice.setTextViewValue("₩$entryFee")
                binding.spot.setTextViewValue("$maximumSpot")

                binding.startTime.setTextViewValue(DateFormat.convert24To12(startDate))

                binding.endTime.setTextViewValue(DateFormat.convert24To12(endDate))



                val (formattedDate, formattedTime) = DateFormat.dateForCountdown(startDate)


                val convert12HourTo24Hour = DateFormat.convert12HourTo24Hour(formattedTime)
                val startTimeForCompare = DateFormat.convert12HourTo24HourWithTime(convert12HourTo24Hour)


                val currentDate = LocalDate.now()
                val formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val compareCurrentDate =  currentDate.format(formatterDate)

                val formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss")
                val currentTime = LocalTime.now()
                val compareCurrentTime = currentTime.format(formatterTime)
                val formatter = DateTimeFormatter.ofPattern("hh:mm a")
                val finalFormattedTime = currentTime.format(formatter)




                val dateDifference = getDateDifference(compareCurrentDate,formattedDate)


                if (status.uppercase() == "CANCELED"){

                    binding.joinNowClick.isEnabled = false
                    binding.joinNowClickButton.text = "Canceled"
                    binding.countDownTimer.text = "Canceled"

                }else if (isExpire){

                    binding.joinNowClick.isEnabled = false
                    binding.joinNowClickButton.text = "Expired"
                    binding.countDownTimer.text = "Expired"

                }else{

                    binding.joinNowClick.isEnabled = true
                    if (joined != null){
                        binding.joinNowClick.setBackgroundResource(R.drawable.green_background)
                        binding.joinNowClickButton.text = "View"
                    }else{
                        binding.joinNowClick.setBackgroundResource(R.drawable.red_background)
                        binding.joinNowClickButton.text = "Join"
                    }


                    if (compareCurrentDate == formattedDate){
                        if (joined != null){
                            if (joined.id.isNotEmpty()){
                                val remainingTimeMillis = DateFormat.calculateRemainingTime(convert12HourTo24Hour)
                                countDownTimer = object : CountDownTimer(remainingTimeMillis, 1000) {
                                    override fun onTick(millisUntilFinished: Long) {
                                        val totalSeconds = millisUntilFinished / 1000
                                        val hours = totalSeconds / 3600
                                        val minutes = (totalSeconds % 3600) / 60
                                        val seconds = totalSeconds % 60

                                        val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                                        holder.binding.countDownTimer.text = formattedTime
                                        println(formattedTime)
                                    }

                                    override fun onFinish() {
                                        holder.binding.countDownTimer.text = "Running!"
                                    }
                                }

                                countDownTimer!!.start()
                            }

                        }else{
                            binding.countDownTimer.text = "Today"
                        }

                    }else if (dateDifference.days == 1){
                        binding.countDownTimer.text = "Tomorrow"
                    }else {
                        binding.countDownTimer.text = DateFormat.contestDateFormat(formattedDate)
                    }
                }








                holder.binding.joinNowClick.setOnClickListener {
                    if (joined == null || joined.id.isEmpty()){
                        click.joinNow(id = id, endDate, startDate,heading,binding.firstPrice.text.toString())
                        return@setOnClickListener
                    }
                    val intent = Intent(context, StepContestUpdateActivity::class.java)
                    intent.putExtra("id", id)
                    intent.putExtra("endDate", endDate)
                    intent.putExtra("startDate", startDate)
                    intent.putExtra("heading", heading)
                    context.startActivity(intent)

                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }




    override fun getItemCount(): Int {
        return contestDataList.size
    }

    inner class ViewHolder( val binding: UpcomingContestLayoutBinding) : RecyclerView.ViewHolder(binding.root)








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







}