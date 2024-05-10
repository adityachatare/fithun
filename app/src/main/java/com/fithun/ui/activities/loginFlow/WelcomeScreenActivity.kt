package com.fithun.ui.activities.loginFlow

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fithun.R
import com.fithun.api.Constants
import com.fithun.api.responses.WinnerPriceData
import com.fithun.clickInterfaces.CheckVersionStatus
import com.fithun.databinding.ActivityWelcomeScreenBinding
import com.fithun.utils.AndroidExtension
import com.fithun.utils.AndroidExtension.initLoader
import com.fithun.utils.Resource
import com.fithun.viewModel.HomeFlowViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WelcomeScreenActivity : AppCompatActivity(),CheckVersionStatus {

    private lateinit var binding: ActivityWelcomeScreenBinding

    private val viewModel:HomeFlowViewModel by viewModels()

    var version = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade



        binding.loginClick.setOnClickListener {
            startActivity(Intent(this@WelcomeScreenActivity, LoginActivity::class.java))
            finishAfterTransition()
        }

        binding.signUpClick.setOnClickListener {
            val intent =  Intent(this, SignUpActivity::class.java)
            intent.putExtra("isFor", "Welcome")
            startActivity(intent)
            finishAfterTransition()
        }

        try {
            val pInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
            version = pInfo.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        viewModel.getLatestVersionApi()

        getApVersionFromApi()

    }


    override fun onResume() {
        super.onResume()

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
                                            AndroidExtension.checkVersionStatus("Hey !! New update are available, please update with new version.", this@WelcomeScreenActivity,this@WelcomeScreenActivity)
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


}