package com.fithun.ui.activities.product

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fithun.R
import com.fithun.api.responses.StateCityResult
import com.fithun.clickInterfaces.StateCityCommonClick
import com.fithun.databinding.ActivityAddAddressBinding
import com.fithun.ui.adapter.OpenDialog
import com.fithun.utils.AndroidExtension.alertBox
import com.fithun.utils.AndroidExtension.alertBoxFinishActivity
import com.fithun.utils.DialogUtils
import com.fithun.utils.FormValidations
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.setSafeOnClickListener
import com.fithun.utils.setTextValue
import com.fithun.utils.setTextViewValue
import com.fithun.viewModel.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddAddressActivity : AppCompatActivity(), StateCityCommonClick {
    private lateinit var binding :ActivityAddAddressBinding

    private val viewModel : AddressViewModel by viewModels()
    var stateCityResult:List<StateCityResult> = listOf()

    var isFrom = ""
    var token = ""
    var addressId = ""
    var stateCodeRequest = ""
    var flagStateOrCity = ""
    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OpenDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        token = SavedPrefManager.getStringPreferences(this,SavedPrefManager.AccessToken).toString()
        intent.getStringExtra("isFor")?.let { isFrom = it }
        intent.getStringExtra("id")?.let { addressId = it }

        binding.title.text = if (isFrom == "Add"){
             "Add Address"
        }else{
            "Update Address"
        }

        binding.buttonText.text = binding.title.text

        binding.etFirstName.addTextChangedListener(textWatcherValidation)
        binding.etLastName.addTextChangedListener(textWatcherValidation)
        binding.etHouse.addTextChangedListener(textWatcherValidation)
        binding.etArea.addTextChangedListener(textWatcherValidation)
        binding.etCountry.addTextChangedListener(textWatcherValidation)
        binding.etState.addTextChangedListener(textWatcherValidation)
        binding.etCity.addTextChangedListener(textWatcherValidation)
        binding.etZip.addTextChangedListener(textWatcherValidation)

        binding.etCountry.text = "India"

        if (isFrom != "Add"){

            viewModel.viewAddressApi(token = token, id = addressId)
        }

        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }

        binding.AddressClickDisabled.setOnClickListener {
            validate(currentField= "")
        }

        binding.etState.setSafeOnClickListener {
            flagStateOrCity ="State"
            openPopUp()
        }

        binding.etCity.setSafeOnClickListener {
            flagStateOrCity ="City"
            openPopUp()
        }

        binding.addAddressClick.setOnClickListener {
            validate(currentField= "")

            if (binding.etFirstName.text.isNotEmpty() &&  binding.etLastName.text.isNotEmpty() &&
                binding.etHouse.text.isNotEmpty() &&  binding.etArea.text.isNotEmpty() &&
                binding.etCountry.text.isNotEmpty() &&  binding.etState.text.isNotEmpty()&&
                binding.etCity.text.isNotEmpty() &&  binding.etZip.text.isNotEmpty()){

                if (isFrom == "Add"){
                    viewModel.addAddress(token=token,firstName= binding.etFirstName.text.toString(),
                        lastName=binding.etLastName.text.toString(), houseNumber=binding.etHouse.text.toString()
                        , area=binding.etArea.text.toString(), city=binding.etCity.text.toString(), state=binding.etState.text.toString(),
                        country=binding.etCountry.text.toString(), zipCode=binding.etZip.text.toString(),stateCode = stateCodeRequest)
                }else{
                    viewModel.updateAddressApi(token=token,firstName= binding.etFirstName.text.toString(),
                        lastName=binding.etLastName.text.toString(), houseNumber=binding.etHouse.text.toString()
                        , area=binding.etArea.text.toString(), city=binding.etCity.text.toString(), state=binding.etState.text.toString(),
                        country=binding.etCountry.text.toString(), zipCode=binding.etZip.text.toString(), id=addressId,stateCode=stateCodeRequest)
                }

            }
        }

        addAddressObserver()
        updateAddressObserver()
        stateCityObserver()
        viewAddressObserver()
    }

    private fun openPopUp() {
        try {
            val binding = LayoutInflater.from(this).inflate(R.layout.state_city_popup, null)
            dialog = DialogUtils().createDialog(this, binding.rootView, 0)!!
            recyclerView = binding.findViewById(R.id.popup_recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)

            val dialogTitle = binding.findViewById<TextView>(R.id.popupTitle)
            val dialogBackButton = binding.findViewById<ImageView>(R.id.BackButton)
            dialogBackButton.setOnClickListener { dialog.dismiss() }

            dialogTitle.text  =  flagStateOrCity
            val searchEditText = binding.findViewById<EditText>(R.id.search_bar_edittext_popuplist)
            searchEditText.addTextChangedListener(textWatchers)

            when (flagStateOrCity) {
                "State" -> {
                    viewModel.stateCityApi("")
                }

                else ->{
                    viewModel.stateCityApi(stateCodeRequest)
                }

            }

            dialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private val textWatcherValidation = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val currentField = if (s === binding.etFirstName.text) "First Name" else if (s === binding.etLastName.text) "Last Name"
            else if (s === binding.etHouse.text) "House Number" else if (s === binding.etArea.text) "Area"
            else if (s === binding.etCountry.text) "Country" else if (s === binding.etState.text) "State"
            else if (s === binding.etCity.text) "City" else if (s === binding.etZip.text) "ZipCode" else  ""
            validate(currentField)

            if (binding.etFirstName.text.isNotEmpty() &&  binding.etLastName.text.isNotEmpty() &&
                binding.etHouse.text.isNotEmpty() &&  binding.etArea.text.isNotEmpty() &&
                binding.etCountry.text.isNotEmpty() &&  binding.etState.text.isNotEmpty()&&
                binding.etCity.text.isNotEmpty() &&  binding.etZip.text.isNotEmpty()){

                binding.AddressClickDisabled.isVisible = false
                binding.addAddressClick.isVisible = true


            }else{
                binding.AddressClickDisabled.isVisible = true
                binding.addAddressClick.isVisible = false
            }
        }
    }


    private fun validate(currentField:String){
        FormValidations.addAddress(binding.etFirstName,binding.firstNameLL,binding.tvFirstName,
            binding.etLastName,binding.lastNameLL,binding.tvLastName,
            binding.etHouse,binding.HouseNumberLL,binding.tvHouseNumber,
            binding.etArea,binding.AreaLL,binding.tvArea,
            binding.etCountry,binding.CountryLL,binding.tvCountry,
            binding.etState,binding.StateLL,binding.tvState,
            binding.etCity,binding.CityLL,binding.tvCity,
            binding.etZip,binding.ZipLL,binding.tvZip,currentField
        )
    }

    private fun addAddressObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.addressResponseData.collect { response ->
                    when(response){

                        is Resource.Loading ->{
                            Progress.start(this@AddAddressActivity)
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.statusCode == 200){
                                    alertBoxFinishActivity(response.data.responseMessage,this@AddAddressActivity,this@AddAddressActivity)
                                }


                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error ->{
                            Progress.stop()
                            response.message?.let {
                                alertBox(it,this@AddAddressActivity)
                            }
                        }

                        is Resource.Empty ->{

                        }

                    }

                }

            }
        }
    }
    private fun updateAddressObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.updateAddressResponseData.collect { response ->
                    when(response){

                        is Resource.Loading ->{
                            Progress.start(this@AddAddressActivity)
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.statusCode == 200){
                                    alertBoxFinishActivity(response.data.responseMessage,this@AddAddressActivity,this@AddAddressActivity)
                                }


                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error ->{
                            Progress.stop()
                            response.message?.let {
                                alertBox(it,this@AddAddressActivity)
                            }

                        }

                        is Resource.Empty ->{

                        }

                    }

                }

            }
        }
    }
    private fun viewAddressObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.viewResponseAddressData.collect { response ->
                    when(response){

                        is Resource.Loading ->{
                            Progress.start(this@AddAddressActivity)
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200){
                                    response.data.result.apply {
                                        binding.etFirstName.setTextValue(firstName)
                                        binding.etLastName.setTextValue(lastName)
                                        binding.etHouse.setTextValue(houseNumber)
                                        binding.etArea.setTextValue(area)
                                        binding.etState.setTextViewValue(state)
                                        binding.etCity.setTextViewValue(city)
                                        binding.etZip.setTextValue(zipCode)
                                        stateCodeRequest = state
                                    }

                                }

                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error ->{
                            Progress.stop()
                            response.message?.let {
                                alertBox(it,this@AddAddressActivity)
                            }
                        }

                        is Resource.Empty ->{

                        }

                    }

                }

            }
        }
    }
    private fun stateCityObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getStateCityResponseData.collect { response ->
                    when(response){

                        is Resource.Loading ->{
                            Progress.start(this@AddAddressActivity)
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()

                                if (response.data?.responseCode == 200){
                                    stateCityResult =  emptyList()
                                    stateCityResult = response.data.result
                                    setAdapterPopUp()
                                }

                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error ->{
                            Progress.stop()
                            response.message?.let {
                                alertBox(it,this@AddAddressActivity)
                            }
                        }

                        is Resource.Empty ->{

                        }
                    }
                }
            }
        }
    }

    private fun setAdapterPopUp() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = OpenDialog(this, stateCityResult, flagStateOrCity, click= this)
        recyclerView.adapter = adapter
    }

    override fun getCityState(data: String, flag: String, code: String) {
        when(flag){

            "State" -> {
                binding.etState.text = data
                stateCodeRequest = code
                binding.etCity.text = ""
                binding.tvState.isVisible = false
                binding.StateLL.setBackgroundResource(R.drawable.white_border_background)
                dialog.dismiss()
            }

            "City" -> {
                binding.etCity.text = data
                binding.tvCity.isVisible = false
                binding.CityLL.setBackgroundResource(R.drawable.white_border_background)
                dialog.dismiss()
            }
        }
    }


    private val textWatchers = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            filterData(s.toString())

        }
    }

    private fun filterData(searchText: String) {
        val filteredList = stateCityResult.filter { item ->
            try {
                item.name.contains(searchText, ignoreCase = true)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
        adapter.filterList(filteredList as ArrayList<StateCityResult>)
    }


}