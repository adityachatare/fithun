package com.fithun.ui.activities.loginFlow

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fithun.R
import com.fithun.ui.hostFragment.FragmentHostActivity
import com.fithun.utils.SavedPrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity(){



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)




        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)

            if(SavedPrefManager.getBooleanPreferences(this@SplashActivity,SavedPrefManager.isLogin)){
                startActivity(Intent(this@SplashActivity, FragmentHostActivity::class.java))
                finishAfterTransition()
            }
            else{
                startActivity(Intent(this@SplashActivity, WelcomeScreenActivity::class.java))
                finishAfterTransition()
            }


        }



    }



}