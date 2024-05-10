package com.fithun.ui.activities.common

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.fithun.R
import com.fithun.api.responses.Docs
import com.fithun.api.responses.SubCategory
import com.fithun.clickInterfaces.HowToPlayCategory
import com.fithun.databinding.ActivityFaqBinding
import com.fithun.ui.adapter.FaqAdapter
import com.fithun.uiModalClass.FaqResult
import com.fithun.utils.AndroidExtension
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.viewModel.StaticViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FaqActivity : AppCompatActivity(), HowToPlayCategory {

    private lateinit var binding: ActivityFaqBinding
    private val viewModel: StaticViewModel by viewModels()
    private var isFrom = ""

    lateinit var FAQsAdapter: FaqAdapter
    lateinit var result: ArrayList<FaqResult>

    var loaderFlag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        intent.getStringExtra("isFrom")?.let { isFrom = it }

        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }




        binding.heading.text =  when (isFrom) {
            "Faqs" -> {
                "Faqs"

            }
            else -> {

                "How To Play"
            }
        }
        when (isFrom) {
            "Faqs" -> {
                "Faqs"
                viewModel.faqListApi("")
            }
            else -> {

                "How To Play"
                viewModel.howPlayList( page=1, limit=10, search="")


            }
        }

        faqObserver()
        howToPlayList()







    }

    private fun faqObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.faqListApiResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(this@FaqActivity)
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200) {
                                    faqRecycler(response.data.result?.docs)

                                }


                            } catch (e: Exception) {
                                
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@FaqActivity)
                            }


                        }


                        is Resource.Empty -> {


                        }


                    }


                }

            }
        }





    }

    private fun howToPlayList() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.howPlayListApiResponseData.collect { response ->
                    when(response){

                        is Resource.Loading ->{
                            Progress.start(this@FaqActivity)
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200){



                                    val resultHow = response.data.result
                                    faqRecycler(resultHow?.docs)
                                    // Set the category name in the adapter
//                                    val categoryName = resultHow?.docs.firstOrNull()?.categoryData?.name ?: ""
//                                    adapter.setCategoryName(categoryName)


                                }

                            }catch (e:Exception){
                                
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error ->{
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@FaqActivity)
                            }

                        }


                        is Resource.Empty ->{

                        }


                    }


                }

            }
        }

    }






    fun faqRecycler(data: ArrayList<Docs>?) {
        binding.FAQsRecycler.layoutManager = LinearLayoutManager(this)
        FAQsAdapter = FaqAdapter(this, data, isFrom = isFrom, this)
        binding.FAQsRecycler.adapter = FAQsAdapter



    }

    override fun getHowToPlayCategory(subCategoryData: ArrayList<SubCategory>) {
        val intent = Intent(this, HowToPlayActivity::class.java)
        intent.putExtra("subCategoryData", subCategoryData)
        startActivity(intent)    }
}



