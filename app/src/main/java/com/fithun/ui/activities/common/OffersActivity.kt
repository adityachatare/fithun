package com.fithun.ui.activities.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fithun.R
import com.fithun.databinding.ActivityOffersBinding
import com.fithun.ui.adapter.BannerAdapter
import com.fithun.ui.adapter.OfferRewardsAdapter
import com.fithun.ui.adapter.OfferWinAdapter
import com.fithun.uiModalClass.BannerImages

class OffersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOffersBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOffersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        setBannerImageAdaptor()
        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }


    }
    private fun setBannerImageAdaptor() {


        val bannerAdapter = BannerAdapter(this, BannerImages.data)
        binding.storeViewpager.adapter = bannerAdapter
        binding.indicator.setViewPager(binding.storeViewpager)



        val offerRewardsAdapter = OfferRewardsAdapter(this)
        binding.yourRewards.adapter = offerRewardsAdapter

        val offerWinAdapter = OfferWinAdapter(this)
        binding.yourWin.adapter = offerWinAdapter




    }

}