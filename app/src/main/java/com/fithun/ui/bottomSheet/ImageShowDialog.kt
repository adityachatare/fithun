package com.fithun.ui.bottomSheet

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2
import com.fithun.R
import com.fithun.ui.adapter.ImageSliderAdaptorZoom
import com.fithun.utils.setSafeOnClickListener
import me.relex.circleindicator.CircleIndicator3

class ImageShowDialog(private var imageList: List<String>, val imagePosition:Int) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.image_preview_layout, container, false)
        val multiImage = view.findViewById<ViewPager2>(R.id.multi_image)
        val indicator3 = view.findViewById<CircleIndicator3>(R.id.indicator)
        val cross = view.findViewById<ImageView>(R.id.cross)
        val imageAdaptor = ImageSliderAdaptorZoom(imageList, requireContext())
        multiImage.adapter = imageAdaptor
        if (imageList.size > 1) {
            indicator3.setViewPager(multiImage)
        }

        cross.setSafeOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        val layoutParams: WindowManager.LayoutParams = dialog!!.window!!.attributes
        layoutParams.dimAmount = 0.7f
        dialog!!.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog!!.window?.setGravity(Gravity.CENTER)
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog!!.window?.attributes = layoutParams
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialog!!.window?.attributes?.windowAnimations = android.R.style.Animation_Dialog

    }


}