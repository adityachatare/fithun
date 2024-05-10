package com.fithun.ui.activities.common

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fithun.R
import com.fithun.api.responses.SubCategory
import com.fithun.databinding.ActivityHowToPlayBinding
import com.fithun.ui.adapter.HowToPlayAdapter
import com.fithun.uiModalClass.FaqResult
import com.fithun.viewModel.StaticViewModel

class HowToPlayActivity : AppCompatActivity() {


    var newTest :String = ""




    private lateinit var binding: ActivityHowToPlayBinding
    private val viewModel: StaticViewModel by viewModels()
    private var categoryId = ""
    var subCategoryData : ArrayList<SubCategory>  =ArrayList()
    lateinit var howToPlayAdapter: HowToPlayAdapter
    lateinit var result: ArrayList<FaqResult>

    var loaderFlag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHowToPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        intent.getSerializableExtra("subCategoryData")?.let { subCategoryData =
            it as ArrayList<SubCategory>
        }

        binding.backButton.setOnClickListener {

            finishAfterTransition()
        }

        binding.heading.text = "How To Play"

        if(subCategoryData.size > 0) {
            howToPlayAdapter(subCategoryData)
        }
    }

    fun howToPlayAdapter(subCategoryData : ArrayList<SubCategory>) {
        binding.HowToPlayRecycler.layoutManager = LinearLayoutManager(this)
        howToPlayAdapter = HowToPlayAdapter(this, subCategoryData)
        binding.HowToPlayRecycler.adapter = howToPlayAdapter



    }
}