package com.fithun.ui.bottomTab

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.fithun.R
import com.fithun.api.responses.GraphData
import com.fithun.databinding.FragmentStepCounterTabBinding
import com.fithun.utils.AndroidExtension
import com.fithun.utils.ManageStack
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.viewModel.HomeFlowViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@AndroidEntryPoint
class StepCounterTabFragment : Fragment() {

    private var _binding: FragmentStepCounterTabBinding? =  null
    private val binding get() = _binding!!


    private val minRotation = -130f
    private val maxRotation = 130f
    private var finalRotationValue = -130f
    private var job: Job? = null
    private var weightJob: Job? = null
    private var minSpeed = 0f
    private var maxSpeed = 6f

    private var minWeight = 40f
    private var maxWeight = 100f



    var unit = ""

    companion object{
        @JvmStatic
        fun newInstance() = StepCounterTabFragment()
    }


    private val viewModel: HomeFlowViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStepCounterTabBinding.inflate(layoutInflater, container, false)


        onClick()
        viewModel.getGraphApi(token= SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.AccessToken).toString(), "week")


        binding.circleProgressBar.setProgress(10f)




        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getGraphApi(token= SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.AccessToken).toString(), "week")

            binding.dayll.setBackgroundResource(R.drawable.theme_border_background)
            binding.monthll.setBackgroundResource(R.drawable.theme_border_background)
            binding.yearll.setBackgroundResource(R.drawable.theme_border_background)
            binding.weekll.setBackgroundResource(R.drawable.theme_background)
            binding.txtDay.setTextColor(ContextCompat.getColor(requireContext(),R.color.theme_color))
            binding.txtMonth.setTextColor(ContextCompat.getColor(requireContext(),R.color.theme_color))
            binding.txtYear.setTextColor(ContextCompat.getColor(requireContext(),R.color.theme_color))
            binding.txtWeek.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            binding.swipeRefresh.isRefreshing = false
        }






        job = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                uiStepCounterRotationValue()

                val stepsCount = (SavedPrefManager.getFloatPreferences(requireContext(), SavedPrefManager.totalDistance) * 1.3).roundToInt()
                binding.todayCoin.text = String.format("%.2f", stepsCount.toDouble() / 1000)

                delay(1000)
            }
        }





        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        graphObserver()

    }



    private fun onClick(){
        binding.dayll.setOnClickListener {
            viewModel.getGraphApi(token= SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.AccessToken).toString(), "day")

            binding.dayll.setBackgroundResource(R.drawable.theme_background)
            binding.monthll.setBackgroundResource(R.drawable.theme_border_background)
            binding.yearll.setBackgroundResource(R.drawable.theme_border_background)
            binding.weekll.setBackgroundResource(R.drawable.theme_border_background)
            binding.txtDay.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            binding.txtMonth.setTextColor(ContextCompat.getColor(requireContext(),R.color.theme_color))
            binding.txtYear.setTextColor(ContextCompat.getColor(requireContext(),R.color.theme_color))
            binding.txtWeek.setTextColor(ContextCompat.getColor(requireContext(),R.color.theme_color))

        }

        binding.monthll.setOnClickListener {
            viewModel.getGraphApi(token= SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.AccessToken).toString(), "month")

            binding.dayll.setBackgroundResource(R.drawable.theme_border_background)
            binding.monthll.setBackgroundResource(R.drawable.theme_background)
            binding.yearll.setBackgroundResource(R.drawable.theme_border_background)
            binding.weekll.setBackgroundResource(R.drawable.theme_border_background)
            binding.txtDay.setTextColor(ContextCompat.getColor(requireContext(),R.color.theme_color))
            binding.txtMonth.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            binding.txtYear.setTextColor(ContextCompat.getColor(requireContext(),R.color.theme_color))
            binding.txtWeek.setTextColor(ContextCompat.getColor(requireContext(),R.color.theme_color))
        }
        binding.weekll.setOnClickListener {
            viewModel.getGraphApi(token= SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.AccessToken).toString(), "week")
            binding.dayll.setBackgroundResource(R.drawable.theme_border_background)
            binding.monthll.setBackgroundResource(R.drawable.theme_border_background)
            binding.yearll.setBackgroundResource(R.drawable.theme_border_background)
            binding.weekll.setBackgroundResource(R.drawable.theme_background)
            binding.txtDay.setTextColor(ContextCompat.getColor(requireContext(),R.color.theme_color))
            binding.txtMonth.setTextColor(ContextCompat.getColor(requireContext(),R.color.theme_color))
            binding.txtYear.setTextColor(ContextCompat.getColor(requireContext(),R.color.theme_color))
            binding.txtWeek.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        }

        binding.yearll.setOnClickListener {
            viewModel.getGraphApi(token= SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.AccessToken).toString(), "year")
            binding.dayll.setBackgroundResource(R.drawable.theme_border_background)
            binding.monthll.setBackgroundResource(R.drawable.theme_border_background)
            binding.yearll.setBackgroundResource(R.drawable.theme_background)
            binding.weekll.setBackgroundResource(R.drawable.theme_border_background)
            binding.txtDay.setTextColor(ContextCompat.getColor(requireContext(),R.color.theme_color))
            binding.txtMonth.setTextColor(ContextCompat.getColor(requireContext(),R.color.theme_color))
            binding.txtYear.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            binding.txtWeek.setTextColor(ContextCompat.getColor(requireContext(),R.color.theme_color))
        }
    }



    private fun graphObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getGraphApiResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(requireContext())
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode== 200) {

                                    with(response.data.result){

                                        binding.stepsCount.text = totalSteps.toString()
                                        binding.txtStepCount.text = totalSteps.toString()
                                        binding.txtSteps.text = totalSteps.toString()

                                        binding.totalKm.text = "${convertDistance(totalDistanceWalked.toDouble())} $unit"
                                        binding.txtDistance.text = "${convertDistance(totalDistanceWalked.toDouble())} $unit"
                                        binding.txtWalked.text =  "${convertDistance(totalDistanceWalked.toDouble())} $unit"

                                        binding.burnKcal.text=  "$caloriesCount Kcal"
                                        binding.reduceWeight.text=  "Weight Reduce $reduceWeight"
                                        binding.weight.text=  "${userDetails.additionalDetails.weight} Kg"


                                        weightJob = CoroutineScope(Dispatchers.Main).launch {
                                            while (isActive) {
                                                if (userDetails.additionalDetails.weight!!.isNotEmpty()){
                                                    uiWeightRotationValue(userDetails.additionalDetails.weight)
                                                }

                                                delay(1000)
                                            }
                                        }



                                        binding.txtAverageSpeed.text = "$averageSpeed m/s"
                                        binding.txtSpeed.text = "$averageSpeed m/s"


                                        if (graphData.isNotEmpty()){
                                            lineGraph(graphData)
                                            binding.NoDataFound.visibility = View.GONE
                                            binding.lineChart.visibility = View.VISIBLE
                                        }else{
                                            binding.NoDataFound.visibility = View.VISIBLE
                                            binding.lineChart.visibility = View.GONE
                                        }

                                    }



                                }else{
                                    binding.txtStepCount.text = "0"
                                    binding.txtDistance.text = "0 Km"
                                    binding.txtAverageSpeed.text = "0 m/s"
                                    binding.tabMode.visibility = View.GONE
                                    binding.lineChart.visibility = View.GONE

                                    binding.txtSteps.text = "0"
                                    binding.txtWalked.text = "0 Km"
                                    binding.txtSpeed.text = "0 m/s"
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

    private fun uiWeightRotationValue(weight: String?) {

       if(isNumber(weight!!)){

           val finalValue = if (weight >= "99"){
               "92"
           }else{
               weight
           }


           this.finalRotationValue = calculateWeightRotationValue(finalValue.toFloat())
           rotateWeightMeter(this.finalRotationValue)
       }

    }


    private fun isNumber(input: String): Boolean {
        return input.matches(Regex("\\d+"))
    }
    private fun uiStepCounterRotationValue() {

        val currentSpeed =  SavedPrefManager.getFloatPreferences(requireContext(),SavedPrefManager.Speed)
        val kmPerHourSpeed = currentSpeed * 3.6



        this.finalRotationValue = calculateRotationValue(kmPerHourSpeed.toFloat())



        binding.kmPerHour.text = formatDynamicValue(kmPerHourSpeed.toString(), 2)
        if(kmPerHourSpeed >= 0.3) {
            binding.speedUnit.text = "Km/h"
        } else {
            binding.speedUnit.text = "m/s"
        }



        // Animate the rotation
        rotateMeter(this.finalRotationValue)
    }


    private fun formatDynamicValue(value: String, decimalPlaces: Int): String {
        val parts = value.split(".")

        return if (parts.size == 2 && parts[1].length > decimalPlaces) {
            val truncatedDecimal = parts[1].substring(0, decimalPlaces)
            "${parts[0]}.${truncatedDecimal}"
        } else {
            value
        }
    }

    private fun calculateRotationValue(speed: Float): Float {
        val range = maxRotation - minRotation
        val adjustedSpeed = (speed - minSpeed) / maxSpeed // Normalize the speed to [0, 1]
        return (adjustedSpeed * range) + minRotation
    }


    private fun calculateWeightRotationValue(weight: Float): Float {
        val range = maxRotation - minRotation
        val adjustedWeight = (weight - minWeight) / (maxWeight - minWeight) // Normalize the weight to [0, 1]
        return (adjustedWeight * range) + minRotation
    }

    private fun rotateMeter(finalRotationValue: Float){
        val currentRotation = binding.rotationHandImage.rotation
        val rotationDifference = abs(finalRotationValue - currentRotation)
        val fullRotationRange = 130 - (-130)
        val halfRotationRange = fullRotationRange / 2

        val animationDuration = (rotationDifference / halfRotationRange) * 1000L // Adjust the duration as needed

        val animator = ValueAnimator.ofFloat(currentRotation, finalRotationValue)
        animator.duration = animationDuration.toLong()
        animator.addUpdateListener { animation ->
            val newRotation = animation.animatedValue as Float
            val upperOffsetYExtender = -27.0 // Adjust this value to move the pivot closer or further from the top
            binding.rotationHandImage.pivotX = binding.rotationHandImage.width / 2f
            binding.rotationHandImage.pivotY = (binding.rotationHandImage.height + upperOffsetYExtender).toFloat()
            binding.rotationHandImage.rotation = newRotation
        }
        animator.start()
    }

    private fun rotateWeightMeter(finalRotationValue: Float){
        val currentRotation = binding.rotationWeightImage.rotation
        val rotationDifference = abs(finalRotationValue - currentRotation)
        val fullRotationRange = 130 - (-130)
        val halfRotationRange = fullRotationRange / 2

        val animationDuration = (rotationDifference / halfRotationRange) * 1000L // Adjust the duration as needed

        val animator = ValueAnimator.ofFloat(currentRotation, finalRotationValue)
        animator.duration = animationDuration.toLong()
        animator.addUpdateListener { animation ->
            val newRotation = animation.animatedValue as Float
            val upperOffsetYExtender = -27.0 // Adjust this value to move the pivot closer or further from the top
            binding.rotationWeightImage.pivotX = binding.rotationWeightImage.width / 2f
            binding.rotationWeightImage.pivotY = (binding.rotationWeightImage.height + upperOffsetYExtender).toFloat()
            binding.rotationWeightImage.rotation = newRotation
        }
        animator.start()
    }

    private fun lineGraph(list :ArrayList<GraphData>) {

        val arrayList :ArrayList<Entry> = arrayListOf()

        for (i in 0 until list.size-1) {
            arrayList.add(Entry(i.toFloat(), list[i].stepCount))
        }
        val dataSet = LineDataSet(arrayList, "Steps")

        dataSet.color = Color.parseColor("#FF4081")
        dataSet.lineWidth = 2f
        dataSet.setDrawCircles(true)
        dataSet.setDrawFilled(false)
        dataSet.fillColor = Color.parseColor("#FF4081")
        dataSet.fillAlpha = 30
        dataSet.setCircleColor(Color.parseColor("#FF4081"))
        dataSet.circleRadius = 5f
        dataSet.circleHoleRadius = 3f

        val lineData = LineData(dataSet)

        binding.lineChart.data = lineData
        binding.lineChart.setDrawGridBackground(false)
        binding.lineChart.setDrawBorders(false)
        binding.lineChart.description = Description().apply {
            text = ""
        }
        binding.lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.lineChart.xAxis.setDrawGridLines(false)
        binding.lineChart.xAxis.textColor = Color.WHITE


        binding.lineChart.animateX(1000, Easing.EaseInOutQuad)
        binding.lineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e != null) {
                    e.y
                    dataSet.setDrawValues(true)
                    binding.lineChart.invalidate()
                }
            }

            override fun onNothingSelected() {
                dataSet.setDrawValues(false)
                binding.lineChart.invalidate()
            }
        })
    }



    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        weightJob?.cancel()
    }


    private fun convertDistance(value: Double): String {
        return if (value >= 1.0) {
            unit = "Km"
            "%.1f".format(value)
        } else {
            unit = "m"
            "${(value * 1000).toInt()}"
        }
    }



}