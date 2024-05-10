package com.fithun.ui.bottomSheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.fithun.R
import com.fithun.api.responses.BrandListResult
import com.fithun.clickInterfaces.ProductFilterInterface
import com.fithun.utils.setSafeOnClickListener

class ProductFilter(val click: ProductFilterInterface, private val brandListData: List<BrandListResult>): BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.product_filter, container, false)

        val cancelClick =  view.findViewById<LinearLayout>(R.id.CancelClick)!!
        val applyClick =  view.findViewById<RelativeLayout>(R.id.applyClick)!!
        val llBrands =  view.findViewById<RelativeLayout>(R.id.llBrands)!!
        val brandsName =  view.findViewById<TextView>(R.id.brandsName)!!
        val priceSpinner =  view.findViewById<Spinner>(R.id.priceSpinner)!!




        cancelClick.setOnClickListener {
            brandListData.forEach { it.isSelected = false }

            dismiss()
        }
        applyClick.setOnClickListener {
            click.applyFilter(priceSpinner.selectedItem.toString())
            dismiss()
        }


        llBrands.setSafeOnClickListener {
            click.openBrands(brandsName)

        }

        return view
    }






    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.product_filter, null)
        dialog.setContentView(contentView)





        contentView.post {
            val contentHeight = contentView.height
            val peekHeightFraction = 0.75
            val peekHeight = (contentHeight * peekHeightFraction).toInt()

            val params = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            if (behavior != null && behavior is BottomSheetBehavior) {
                behavior.peekHeight = peekHeight
            }
        }
    }


    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

}