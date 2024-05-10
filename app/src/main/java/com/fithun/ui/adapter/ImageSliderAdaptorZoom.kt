package com.fithun.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.chrisbanes.photoview.PhotoView
import com.fithun.R


class ImageSliderAdaptorZoom(
    private var imageList: List<String>,
    var context: Context,
) : RecyclerView.Adapter<ImageSliderAdaptorZoom.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val view: View? = LayoutInflater.from(parent.context).inflate(R.layout.slider_image_zoom, null)
        view?.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return MyViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            Glide.with(context).load(imageList[position]).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.progressBar.isVisible = true
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.progressBar.isVisible = false
                        return false
                    }

                }).into(holder.image)

        }catch (e:Exception){
            e.printStackTrace()
        }


    }

    override fun getItemCount(): Int {
        return imageList.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: PhotoView = itemView.findViewById(R.id.img)
        var progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

    }
}