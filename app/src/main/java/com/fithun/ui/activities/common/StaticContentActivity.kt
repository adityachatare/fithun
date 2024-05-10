package com.fithun.ui.activities.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebSettings
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fithun.R
import com.fithun.databinding.ActivityStaticContentBinding
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.displayToast
import com.fithun.viewModel.StaticViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class StaticContentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStaticContentBinding
    private val viewModel: StaticViewModel by viewModels()

    var isFor = ""
    var type = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaticContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        intent.getStringExtra("isFor")?.let { isFor = it }


        binding.heading.text =  when (isFor) {
            "privacyPolicy" -> {
                 "Privacy Policy"

            }
            "Help" -> {
              "Help & Support"
            }
            "About" -> {
               "About Us"
            }
            "Community GuideLines" -> {
                "Community GuideLines"
            }
            "Legality" -> {
                "Legality"
            }
            else -> {
                "Terms & Conditions"
            }
        }

        callApis();



        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }


        viewStaticDataObserver()




    }


    private fun callApis() {
        when (isFor) {
            "privacyPolicy" -> {
                "Privacy Policy"
                viewModel.viewStaticData(resources.getString(R.string.PRIVACY_POLICY))

            }
            "Help" -> {
                "Help & Support"
                viewModel.viewStaticData(resources.getString(R.string.HELP_SUPPORT))
            }
            "About" -> {
                "About Us"
                viewModel.viewStaticData(resources.getString(R.string.ABOUT_US))
            }
            "Community GuideLines" -> {
                "Community GuideLines"
               viewModel.viewStaticData(resources.getString(R.string.COMUNITY_GUIDLINES))
            }
            "Legality" -> {
                "Legality"
                viewModel.viewStaticData(resources.getString(R.string.LEGALITY_SCREENS))
            }
            else -> {
                "Terms & Conditions"
                viewModel.viewStaticData(resources.getString(R.string.TERMS_AND_CONDITIONS))
            }
        }
    }

    private fun viewStaticDataObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.viewProfileApiResponseData.collect { response ->
                    when(response){
                        is Resource.Loading ->{
                            Progress.start(this@StaticContentActivity)
                        }
                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200){


                                    val html = response.data.result!!.description.trimIndent()
                                    val formattedHtml = convertToHyperlinks(html)
                                    val webSettings: WebSettings = binding.staticContent.settings
                                    webSettings.javaScriptEnabled = true

                                    binding.staticContent.loadDataWithBaseURL(null, formattedHtml, "text/html", "UTF-8", null)


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

    private fun convertToHyperlinks(text: String): String {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        val urlRegex = Regex("https?://\\S+")

        val emailMatcher = emailRegex.findAll(text)
        val urlMatcher = urlRegex.findAll(text)

        val replacedText = StringBuffer(text)

        // Replace email addresses with hyperlinks
        for (match in emailMatcher) {
            val email = match.value
            val hyperlink = "<a href=\"mailto:$email\">$email</a>"
            replacedText.replace(match.range.first, match.range.last + 1, hyperlink)
        }

        // Replace URLs with hyperlinks
        for (match in urlMatcher) {
            val url = match.value
            val hyperlink = "<a href=\"$url\">$url</a>"
            replacedText.replace(match.range.first, match.range.last + 1, hyperlink)
        }

        return replacedText.toString()
    }




}