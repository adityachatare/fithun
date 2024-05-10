package com.fithun.ui.activities.wallet

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.fithun.R
import com.fithun.databinding.ActivityMyWalletBinding
import com.fithun.ui.adapter.wallet.MyWalletAdapter
import com.fithun.ui.bottomSheet.WalletDialog
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.viewModel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyWalletActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyWalletBinding

    private val viewModel: TransactionViewModel by viewModels()
    private lateinit var startForResult: ActivityResultLauncher<Intent>


    var walletAmount = 2f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade


        intent.getIntExtra("wallet",0).let { walletAmount = it.toFloat() }


        viewModel.transactionList(token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString(), page = 1, limit =11, transactionType = "WALLET", transactionStatus = "PAID",fromDate = "", toDate = "")

        binding.tablayout.removeAllTabs()
        binding.tablayout.addTab(binding.tablayout.newTab().setText("My Transaction"))
        binding.tablayout.addTab(binding.tablayout.newTab().setText("My Kyc"))
        binding.tablayout.tabGravity = TabLayout.GRAVITY_FILL
        setTabs()


        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }

        binding.AddedCoin.setOnClickListener {
            supportFragmentManager.let {
                WalletDialog("Added Coin!", "Display add Cash \n(Rule: it can be use for Join Contest Only)").show(it, "MyCustomFragment")
            }
        }

        binding.WinningCoin.setOnClickListener {
            supportFragmentManager.let {
                WalletDialog("Winning Coin!", "Display Contest Wining Prize.\n(Rule:Wining Prize can be use for Product Purchase & Redeem)").show(it, "MyCustomFragment")
            }
        }

        binding.rewardCoin.setOnClickListener {
            supportFragmentManager.let {
                WalletDialog("Reward Coin!", "Reward Cash is coins redeem from daily walk \n (Rule: 1.Reward Coin= No of Strides x 0.001 (Coin) \n" +
                        "2. Reward Coin will Expires in 30 days from the date of issue\n" +
                        "3.Use your reward to buy product &  product cost will be  ₹100 or 10% (Whichever is Higher) of Product cost\n" +
                        "4. Reward Coin can use as discount on Product price which is 10% of total product cost").show(it, "MyCustomFragment")
            }
        }


        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val addedCoin = data?.getBooleanExtra("isAddedCoin",false)
                if (addedCoin == true){
                    Toast.makeText(this, "Coin added successfully", Toast.LENGTH_SHORT).show()
                    viewModel.transactionList(token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString(), page = 1, limit =11, transactionType = "WALLET", transactionStatus = "PAID",fromDate = "", toDate = "")
                    setTabs()
                }
            }
        }


        binding.addCoin.setOnClickListener {
            val intent =  Intent(this, AddCoinActivity::class.java)
            startForResult.launch(intent)

        }
        transactionResponseObserver()

    }


    private fun setTabs() {
        val adapter = MyWalletAdapter(this,  binding.tablayout.tabCount)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tablayout, binding.viewPager) { tab, position ->

            when (position) {
                0 -> tab.text = "My Transaction"
                else -> tab.text = "My Kyc"

            }

        }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.viewPager.currentItem = position
                binding.scrollViewPost.scrollY = 0
            }
        })


    }


    private fun transactionResponseObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.transactionResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            try {
                                if (response.data?.responseCode== 200) {
                                    binding.rewardCoinEt.text =  "₩ ${response.data.result.wallet.totalRewardEarned}"
                                    binding.addedCoinEt.text =  "₩ ${response.data.result.wallet.totalDeposits}"
                                    binding.winningsCoinEt.text =  "₩ ${response.data.result.wallet.totalWinings.toInt()}"

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





}