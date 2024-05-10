package com.fithun.ui.activities.common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fithun.R
import com.fithun.databinding.ActivityPaymentModeBinding
import com.fithun.ui.hostFragment.FragmentHostActivity

class PaymentModeActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPaymentModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentModeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }

        binding.placeOrderClick.setOnClickListener {
            val intent = Intent(this, FragmentHostActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }
}