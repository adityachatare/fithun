package com.fithun.utils

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.fithun.R
import com.fithun.ui.bottomTab.FundTabFragment
import com.fithun.ui.bottomTab.HomeFragment
import com.fithun.ui.bottomTab.ProductTabFragment
import com.fithun.ui.bottomTab.StepContestFragment
import com.fithun.ui.bottomTab.StepContestTabFragment
import com.fithun.ui.bottomTab.StepCounterTabFragment

object ManageStack {


    var isForRedeemForFund = false

     fun resetViewState(
         tab: Int,
         selectedHome: ImageView,
         unSelectedHome: ImageView,
         unselectedContest1: ImageView,
         selectedContest1: ImageView,
         unselectedFund1: ImageView,
         selectedFund1: ImageView,
         homeTV: TextView,
         contestTV: TextView,
         productsTV: TextView,
         counterTV: TextView,
         fundTV: TextView,
         context: Context,
         supportFragmentManager: FragmentManager,
         selectedCounter :ImageView,
         unSelectedCounter:ImageView,
     ) {

        when(tab){
            0->{
               selectedHome.isVisible = true
                unSelectedHome.isVisible = false

                unselectedContest1.isVisible = true
                selectedContest1.isVisible = false

                unSelectedCounter.isVisible = true
                selectedCounter.isVisible = false

                unselectedFund1.isVisible = true
                selectedFund1.isVisible = false


                homeTV.setTextColor(ContextCompat.getColor(context, R.color.check_color))
                contestTV.setTextColor(ContextCompat.getColor(context, R.color.viewColor))
                productsTV.setTextColor(ContextCompat.getColor(context, R.color.viewColor))
                counterTV.setTextColor(ContextCompat.getColor(context, R.color.viewColor))
                fundTV.setTextColor(ContextCompat.getColor(context, R.color.viewColor))





            }
            1->{
               selectedHome.isVisible = false
                unSelectedHome.isVisible = true

                unselectedContest1.isVisible = false
                selectedContest1.isVisible = true


                unSelectedCounter.isVisible = true
                selectedCounter.isVisible = false

                unselectedFund1.isVisible = true
                selectedFund1.isVisible = false


                homeTV.setTextColor(ContextCompat.getColor(context, R.color.viewColor))
                contestTV.setTextColor(ContextCompat.getColor(context, R.color.check_color))
                productsTV.setTextColor(ContextCompat.getColor(context, R.color.viewColor))
                counterTV.setTextColor(ContextCompat.getColor(context, R.color.viewColor))
                fundTV.setTextColor(ContextCompat.getColor(context, R.color.viewColor))



            }
            2->{
               selectedHome.isVisible = false
                unSelectedHome.isVisible = true

                unselectedContest1.isVisible = true
                selectedContest1.isVisible = false

                unSelectedCounter.isVisible = true
                selectedCounter.isVisible = false

                unselectedFund1.isVisible = true
                selectedFund1.isVisible = false


                homeTV.setTextColor(ContextCompat.getColor(context, R.color.viewColor))
                contestTV.setTextColor(ContextCompat.getColor(context, R.color.viewColor))
                productsTV.setTextColor(ContextCompat.getColor(context, R.color.check_color))
                counterTV.setTextColor(ContextCompat.getColor(context, R.color.viewColor))
                fundTV.setTextColor(ContextCompat.getColor(context, R.color.viewColor))

            }
            3->{
               selectedHome.isVisible = false
                unSelectedHome.isVisible = true

                unselectedContest1.isVisible = true
                selectedContest1.isVisible = false

                unSelectedCounter.isVisible = false
                selectedCounter.isVisible = true


                unselectedFund1.isVisible = true
                selectedFund1.isVisible = false


                homeTV.setTextColor(ContextCompat.getColor(context, R.color.viewColor))
                contestTV.setTextColor(ContextCompat.getColor(context, R.color.viewColor))
                productsTV.setTextColor(ContextCompat.getColor(context, R.color.viewColor))
                counterTV.setTextColor(ContextCompat.getColor(context, R.color.check_color))
                fundTV.setTextColor(ContextCompat.getColor(context, R.color.viewColor))

            }
            4->{
               selectedHome.isVisible = false
                unSelectedHome.isVisible = true

                unselectedContest1.isVisible = true
                selectedContest1.isVisible = false

                unSelectedCounter.isVisible = true
                selectedCounter.isVisible = false

                unselectedFund1.isVisible = false
                selectedFund1.isVisible = true


                homeTV.setTextColor(ContextCompat.getColor(context, R.color.viewColor))
                contestTV.setTextColor(ContextCompat.getColor(context, R.color.viewColor))
                productsTV.setTextColor(ContextCompat.getColor(context, R.color.viewColor))
                counterTV.setTextColor(ContextCompat.getColor(context, R.color.viewColor))
                fundTV.setTextColor(ContextCompat.getColor(context, R.color.check_color))

            }

            5 -> {
                supportFragmentManager.beginTransaction().replace(R.id.home_container, StepContestFragment(),"stepContestListFragment")
                    .addToBackStack("stepContestListFragment").commit()
            }

        }
    }


}