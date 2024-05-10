package com.fithun.ui.activities.common

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.fithun.R
import com.fithun.api.Constants
import com.fithun.api.responses.FriendAction
import com.fithun.api.responses.Steps
import com.fithun.databinding.ActivityInviteFriendsBinding
import com.fithun.ui.adapter.BannerAdapter
import com.fithun.ui.adapter.HowItWorkTrackAdapter
import com.fithun.ui.adapter.InviteUserAdapter
import com.fithun.ui.bottomSheet.InviteFriends
import com.fithun.uiModalClass.BannerImages
import com.fithun.utils.AndroidExtension
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.setSafeOnClickListener
import com.fithun.viewModel.HowToWorkViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class InviteFriendsActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityInviteFriendsBinding
    private var player: SimpleExoPlayer? = null

    private val sliderHandler: Handler = Handler()
    private val viewModel: HowToWorkViewModel by viewModels()


    private var referCode = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInviteFriendsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        viewModel.getReferCodeApi(token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString())
        viewModel.howToWorkApi(token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString())
        howToWorkDetails()
        getReferCodeObserver()

        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }

        binding.shareViaClick.setSafeOnClickListener {
            val bottomSheetFragment = InviteFriends(referCode)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        setBannerImageAdaptor()

        val sliderRunnable = Runnable {
            lifecycleScope.launch {
                val currentItem = binding.storeViewpager.currentItem
                val lastItem = binding.storeViewpager.adapter?.itemCount?.minus(1) ?: 0


                if (currentItem == lastItem) {
                    binding.storeViewpager.currentItem = 0
                } else {
                    binding.storeViewpager.currentItem = currentItem + 1
                }
            }
        }

        val callback: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 2000)
            }
        }
        binding.storeViewpager.registerOnPageChangeCallback(callback)







        binding.InviteClick.setOnClickListener {
//            val message = "Check out this link to get application : ${Constants.APP_URL}  and use this referral code : to get joining bonus."
            val message = "Hey there! Check out this amazing app that helps you to track your daily steps and stay active: Fit-hun.\n" +
                    "        \n" +
                    "Want to join on the journey towards a healthier lifestyle? Use my referral code $referCode when you sign up!\n" +
                    "        \n" +
                    "You can download the app from the App Store here: ${Constants.APP_URL}"
            shareOnWhatsApp(message)
        }
    }

    private fun howToWorkDetails() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.howToWorkDetailsResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                                Progress.start(this@InviteFriendsActivity)

                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode== 200) {
                                    setPlayer(response.data.result.mediaUrl)
                                    setupRecycler(response.data.result.steps, response.data.result.friendAction)

                                    binding.Header.isVisible = response.data.result.friendAction.isNotEmpty()
                                    binding.WinningsRecycler.isVisible = response.data.result.friendAction.isNotEmpty()
                                    binding.totalEarn.isVisible = response.data.result.friendAction.isNotEmpty()


                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@InviteFriendsActivity)
                            }
                        }

                        is Resource.Empty -> {
                        }
                    }
                }
            }
        }
    }
    private fun getReferCodeObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getReferCodeResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                        }

                        is Resource.Success -> {
                            try {
                                if (response.data?.responseCode == 200) {
                                    referCode = response.data.result.referCode
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

    // Function to generate WhatsApp shareable link
    private fun generateWhatsAppLink(message: String, url: String): String {
        return "https://wa.me/?text=${Uri.encode("$message $url")}"
    }


    // Function to open WhatsApp and share the link
    private fun shareOnWhatsApp(link: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, link)
        sendIntent.type = "text/plain"
        sendIntent.`package` = "com.whatsapp" // Specify WhatsApp package name

        try {
            startActivity(sendIntent)
        } catch (e: ActivityNotFoundException) {
            // WhatsApp is not installed, handle the error here
            Toast.makeText(this, "WhatsApp is not installed on your device.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setBannerImageAdaptor() {
        val bannerAdapter = BannerAdapter(this, BannerImages.data)
        binding.storeViewpager.adapter = bannerAdapter
        binding.indicator.setViewPager(binding.storeViewpager)
    }

    private fun setupRecycler(list : ArrayList<Steps>, inviteList : ArrayList<FriendAction>) {
        binding.viewHowItWorks.layoutManager = LinearLayoutManager(this)
        val adapter = HowItWorkTrackAdapter(context = this, itemList = list)
        binding.viewHowItWorks.adapter = adapter



        binding.WinningsRecycler.layoutManager = LinearLayoutManager(this)
        val adapterWinningsRecycler = InviteUserAdapter(context = this, inviteList)
        binding.WinningsRecycler.adapter = adapterWinningsRecycler
    }

    private fun setPlayer(mediaUrl:String){

        player = SimpleExoPlayer.Builder(this).build()
        player!!.addListener(object : Player.Listener {

            override fun onLoadingChanged(isLoading: Boolean) {}
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if(playbackState == Player.STATE_BUFFERING){
                    binding.imgHowToWork.visibility = View.VISIBLE
                    binding.player.visibility = View.GONE
                }else if(playbackState == Player.STATE_ENDED){
                    player!!.seekTo(0);
                    player!!.playWhenReady = true
                }else if(playbackState == Player.STATE_IDLE){
                    binding.imgHowToWork.visibility = View.VISIBLE
                    binding.player.visibility = View.GONE
                } else{
                    binding.imgHowToWork.visibility = View.GONE
                    binding.player.visibility = View.VISIBLE
                }
            }
        })

        binding.player.player = player
        val mediaItem: MediaItem =
            MediaItem.fromUri(mediaUrl)
        player!!.addMediaItem(mediaItem)
        player!!.prepare()
        binding.player.hideController()
        player!!.playWhenReady = true

    }

    override fun onStop() {
        super.onStop()
        binding.player!!.player = null
        player!!.release()
        player = null
    }

}