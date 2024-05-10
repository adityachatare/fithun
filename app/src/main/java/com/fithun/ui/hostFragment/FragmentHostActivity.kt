package com.fithun.ui.hostFragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.fithun.R
import com.fithun.api.Constants
import com.fithun.backgroundService.Actions
import com.fithun.backgroundService.ClearPreferences
import com.fithun.backgroundService.EndlessService
import com.fithun.backgroundService.ServiceState
import com.fithun.backgroundService.getServiceState
import com.fithun.clickInterfaces.CheckVersionStatus
import com.fithun.clickInterfaces.HomeFragmentClickListener
import com.fithun.clickInterfaces.LocationDenyInterface
import com.fithun.databinding.ActivityFragmentHostBinding
import com.fithun.permission.PermissionManager
import com.fithun.ui.activities.common.NotificationActivity
import com.fithun.ui.activities.common.SettingsActivity
import com.fithun.ui.activities.contest.CompletedContestActivity
import com.fithun.ui.activities.product.ProductCartActivity
import com.fithun.ui.activities.wallet.MyWalletActivity
import com.fithun.ui.bottomTab.FundTabFragment
import com.fithun.ui.bottomTab.HomeFragment
import com.fithun.ui.bottomTab.ProductTabFragment
import com.fithun.ui.bottomTab.StepContestTabFragment
import com.fithun.ui.bottomTab.StepCounterTabFragment
import com.fithun.utils.AndroidExtension
import com.fithun.utils.ManageStack
import com.fithun.utils.ManageStack.resetViewState
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.displayToast
import com.fithun.utils.setSafeOnClickListener
import com.fithun.viewModel.HomeFlowViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentHostActivity : AppCompatActivity(), LocationDenyInterface,CheckVersionStatus,
    HomeFragmentClickListener {

    lateinit var binding: ActivityFragmentHostBinding

    private val viewModel: HomeFlowViewModel by viewModels()
    var version = ""

    var walletAmount  = 0f
    private var exitTime: Long = 0

    private  var homeFragment: HomeFragment? = null
    private var stepContestTabFragment: StepContestTabFragment? = null
    private var productTabFragment: ProductTabFragment? = null
    private var stepCounterTabFragment: StepCounterTabFragment? = null
    private var fundTabFragment: FundTabFragment? = null
    var activeFragment: Fragment? = null







    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentHostBinding.inflate(layoutInflater)
        window.attributes.windowAnimations = R.style.Fade
        setContentView(binding.root)



        PermissionManager.checkAndRequestPermissions(this@FragmentHostActivity)

        homeFragment = HomeFragment.newInstance()
        stepContestTabFragment = StepContestTabFragment.newInstance()
        productTabFragment = ProductTabFragment.newInstance()
        stepCounterTabFragment = StepCounterTabFragment.newInstance()
        fundTabFragment = FundTabFragment.newInstance()
        activeFragment = homeFragment
        manageToolBar()


        supportFragmentManager.beginTransaction().apply {
            add(R.id.home_container, fundTabFragment!!).hide(fundTabFragment!!)
            add(R.id.home_container, stepCounterTabFragment!!).hide(stepCounterTabFragment!!)
            add(R.id.home_container, productTabFragment!!).hide(productTabFragment!!)
            add(R.id.home_container, stepContestTabFragment!!).hide(stepContestTabFragment!!)
            add(R.id.home_container, homeFragment!!).show(homeFragment!!)
        }.commit()



        this.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when(activeFragment){
                    stepContestTabFragment,productTabFragment,stepCounterTabFragment,fundTabFragment -> {
                        activeFragment?.let { it1 ->
                            supportFragmentManager.beginTransaction().hide(it1)
                                .show(homeFragment!!).commit()
                        }
                        activeFragment = homeFragment
                        manageToolBar()
                        homeFragment!!.onResume()
                    }


                    else -> {
                        if ((System.currentTimeMillis() - exitTime) > 2000) {
                            displayToast("Press again to exit")
                            exitTime = System.currentTimeMillis()
                        } else {
                            this@FragmentHostActivity.finishAfterTransition()
                        }
                    }
                }




            }
        })

        actionOnService(Actions.START)


        try {
            val pInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
            version = pInfo.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }


        viewModel.getLatestVersionApi()

        getApVersionFromApi()




        viewModel.getBannerDataApi()
        val saveData = ClearPreferences(applicationContext)
        saveData.setClearPreference()

        profileObserver()

        binding.HomeBottomTab.setOnClickListener {

            activeFragment?.let { it1 ->
                supportFragmentManager.beginTransaction().hide(it1)
                    .show(homeFragment!!).commit()
            }
            activeFragment = homeFragment
            manageToolBar()
            homeFragment!!.onResume()



        }

         binding.ContestBottomTab.setOnClickListener {

             activeFragment?.let { it1 ->
                 supportFragmentManager.beginTransaction().hide(it1)
                     .show(stepContestTabFragment!!).commit()
             }
             activeFragment = stepContestTabFragment
             manageToolBar()
             stepContestTabFragment!!.onResume()

        }

        binding.ProductsBottomTab.setOnClickListener {

            activeFragment?.let { it1 ->
                supportFragmentManager.beginTransaction().hide(it1)
                    .show(productTabFragment!!).commit()
            }
            activeFragment = productTabFragment
            manageToolBar()
            productTabFragment!!.onResume()

        }

        binding.CounterBottomTab.setOnClickListener {



            activeFragment?.let { it1 ->
                supportFragmentManager.beginTransaction().hide(it1)
                    .show(stepCounterTabFragment!!).commit()
            }
            activeFragment = stepCounterTabFragment
            manageToolBar()
            stepCounterTabFragment!!.onResume()

        }

        binding.FundBottomTab.setOnClickListener {

            activeFragment?.let { it1 ->
                supportFragmentManager.beginTransaction().hide(it1)
                    .show(fundTabFragment!!).commit()
            }
            activeFragment = fundTabFragment
            manageToolBar()
            fundTabFragment!!.onResume()


        }

        binding.walletClick.setSafeOnClickListener {
            val intent =  Intent(this, MyWalletActivity::class.java)
            intent.putExtra("wallet", walletAmount.toInt())
            startActivity(intent)
        }

        binding.cartClick.setSafeOnClickListener {
            startActivity(Intent(this, ProductCartActivity::class.java))
        }

        binding.profileClick.setSafeOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.notificationClick.setSafeOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        binding.medalClick.setSafeOnClickListener {
            startActivity(Intent(this, CompletedContestActivity::class.java))
        }

    }


    override fun onStart() {
        super.onStart()
        viewModel.userProfile(
            SavedPrefManager.getStringPreferences(
                this@FragmentHostActivity,
                SavedPrefManager.AccessToken
            ).toString()
        )
    }
    private fun profileObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.profileApiResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                        }

                        is Resource.Success -> {
                            try {
                                if (response.data?.responseCode == 200) {

                                    binding.username.text = response.data.result.name
                                    Glide.with(this@FragmentHostActivity).load(response.data.result.profilePic)
                                        .thumbnail(0.5f)
                                        .placeholder(R.drawable.placeholder)
                                        .into(binding.UserProfile)
                                    walletAmount = response.data.result.wallet
                                    if (response.data.result.kycDetails != null){
                                        SavedPrefManager.saveStringPreferences(this@FragmentHostActivity,SavedPrefManager.UPI_ID,response.data.result.kycDetails.upiId)
                                    }
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@FragmentHostActivity)
                            }
                        }

                        is Resource.Empty -> {

                        }

                    }
                }

            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        homeFragment = null
        stepContestTabFragment = null
        productTabFragment = null
        stepCounterTabFragment = null
        fundTabFragment = null
        activeFragment = null

        SavedPrefManager.deleteStepsCounts(this)
    }


    override fun openSettings(isForLocation: String) {
        if (isForLocation=="Location"){
            PermissionManager.requestBackgroundPermission(this@FragmentHostActivity)
        }else{
            val intent = Intent()
            intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            intent.data = Uri.fromParts("package", this.packageName, null)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)



        PermissionManager.handlePermissionResult(
            this,
            retryCallback = { deniedPermission ->

                when (deniedPermission) {
                    Manifest.permission.ACCESS_FINE_LOCATION -> {
                        AndroidExtension.locationAllowPermission(
                            "Please allow location permission for all time for step count",
                            this@FragmentHostActivity,
                            "Location",
                            this@FragmentHostActivity
                        )
                    }
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION -> {
                        AndroidExtension.locationAllowPermission(
                            "Please allow location permission for all time in background for step count",
                            this@FragmentHostActivity,
                            "Location",
                            this@FragmentHostActivity
                        )
                    }
                    Manifest.permission.CAMERA -> {
                        AndroidExtension.locationAllowPermission(
                            "Please allow permission for camera",
                            this@FragmentHostActivity,
                            "",
                            this@FragmentHostActivity
                        )
                    }
                    Manifest.permission.READ_EXTERNAL_STORAGE -> {
                        AndroidExtension.locationAllowPermission(
                            "Please allow permission for read camera and gallery",
                            this@FragmentHostActivity,
                            "",
                            this@FragmentHostActivity
                        )
                    }
                    Manifest.permission.POST_NOTIFICATIONS -> {
                        AndroidExtension.locationAllowPermission(
                            "Please allow permission for notifications",
                            this@FragmentHostActivity,
                            "",
                            this@FragmentHostActivity
                        )
                    }
                    Manifest.permission.USE_EXACT_ALARM  -> {
                        AndroidExtension.locationAllowPermission(
                            "Please allow permission ",
                            this@FragmentHostActivity,
                            "",
                            this@FragmentHostActivity
                        )
                    }
                }

            },
            requestCode = requestCode,
            permissions = permissions,
            grantResults = grantResults
        )



    }

    private fun actionOnService(action: Actions) {
        if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) return


        Intent(this, EndlessService::class.java).also {
            it.action = action.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(it)
                return
            }

            startService(it)
        }
    }


    private fun getApVersionFromApi() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getAppVersionResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            try {

                                if (response.data?.statusCode == 200) {
                                    try {
                                        if(version < response.data.result.newVersion){
                                            AndroidExtension.checkVersionStatus("Hey !! New update are available, please update with new version.", this@FragmentHostActivity,this@FragmentHostActivity)
                                        }
                                    }catch (e:Exception){
                                        e.printStackTrace()
                                    }

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

    override fun checkStatus() {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(Constants.APP_URL)
        startActivity(openURL)
    }

    override fun onResume() {
        super.onResume()
        if (ManageStack.isForRedeemForFund){
            activeFragment?.let { it1 ->
                supportFragmentManager.beginTransaction().hide(it1)
                    .show(fundTabFragment!!).commit()
            }
            activeFragment = fundTabFragment
            manageToolBar()
            fundTabFragment!!.onResume()
        }
        manageToolBar()

    }

    override fun onPause() {
        super.onPause()
        manageToolBar()

    }

    override fun productClickListener() {
        supportFragmentManager.beginTransaction().apply {
            activeFragment?.let { hide(it) }
            productTabFragment?.let { show(it) }
        }.commit()
        activeFragment = productTabFragment
        manageToolBar()

    }

    override fun contestClickListener() {
        supportFragmentManager.beginTransaction().apply {
            activeFragment?.let { hide(it) }
            stepContestTabFragment?.let { show(it) }
        }.commit()
        activeFragment = stepContestTabFragment
        manageToolBar()
    }


    private fun manageToolBar(){

        when(activeFragment){
            homeFragment -> {
                setBottomTab(tab=0)
                binding.cartClick.isVisible =  true
                binding.notificationClick.isVisible =  true
                binding.walletClick.isVisible =  true
                binding.medalClick.isVisible =  false


            }
            stepContestTabFragment -> {
                setBottomTab(tab=1)
                binding.cartClick.isVisible =  true
                binding.notificationClick.isVisible =  true
                binding.walletClick.isVisible =  false
                binding.medalClick.isVisible =  true

            }

            productTabFragment -> {
                setBottomTab(tab=2)
                binding.cartClick.isVisible =  true
                binding.notificationClick.isVisible =  true
                binding.walletClick.isVisible =  true
                binding.medalClick.isVisible =  false

            }

            stepCounterTabFragment -> {
                setBottomTab(tab=3)
                binding.cartClick.isVisible =  true
                binding.notificationClick.isVisible =  true
                binding.walletClick.isVisible =  true
                binding.medalClick.isVisible =  false

            }

            fundTabFragment -> {
                setBottomTab(tab=4)
                binding.cartClick.isVisible =  true
                binding.notificationClick.isVisible =  true
                binding.walletClick.isVisible =  true
                binding.medalClick.isVisible =  false

            }
        }

    }


    private fun setBottomTab(tab:Int){
        resetViewState(
            tab = tab,
            selectedHome = binding.SelectedHome,
            unSelectedHome = binding.UnSelectedHome,
            unselectedContest1 = binding.UnSelectedContest,
            selectedContest1 = binding.SelectedContest,
            unselectedFund1 = binding.UnSelectedFund,
            selectedFund1 = binding.SelectedFund,
            homeTV = binding.HomeTV,
            contestTV = binding.ContestTV,
            productsTV = binding.ProductsTV,
            counterTV = binding.CounterTV,
            fundTV = binding.FundTV,
            context = this,
            supportFragmentManager = supportFragmentManager,
            selectedCounter = binding.SelectedCounter,
            unSelectedCounter = binding.UnSelectedCounter,
        )
    }




}