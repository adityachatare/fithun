package com.fithun.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fithun.databinding.FragmentLeaderboardLiveBinding


class LeaderboardLiveFragment : Fragment() {

    private var _binding: FragmentLeaderboardLiveBinding? =  null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentLeaderboardLiveBinding.inflate(layoutInflater, container, false)
        setLeaderboardContestAdapter()
        return  binding.root
    }


    private fun setLeaderboardContestAdapter() {
       /* binding.LeaderboardRecycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter = LeaderboardContestLiveAdapter(requireContext(),isFrom = "")
        binding.LeaderboardRecycler.adapter = adapter*/


    }

}