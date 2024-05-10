package com.fithun.ui.bottomSheet

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.fithun.R
import com.fithun.api.responses.SizeValueData
import com.fithun.clickInterfaces.GetUnitDetails
import com.fithun.clickInterfaces.SizeListener
import com.fithun.ui.adapter.SizeAdaptor

class SelectSizeBottomSheet(var mContext: Context, var sizeDetails : List<SizeValueData>, var sizeListener: GetUnitDetails) : BottomSheetDialogFragment() ,
    SizeListener {
    lateinit var unitRv: RecyclerView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.select_unit_list_design, null)
        unitRv= view.findViewById(R.id.unit_rv)

        setUnitAdaptor()

        return view
    }

    private fun setUnitAdaptor() {
        unitRv.layoutManager = LinearLayoutManager(mContext)
        var adapter = SizeAdaptor(mContext, sizeDetails, this)
        unitRv.adapter = adapter
    }



    override fun sizeUnitListener(unit: String, size: String, price: Number, id: String) {
        sizeListener.getUnitDetails(unit,size,price,id)
        dismiss()
    }
}
