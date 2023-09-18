package com.example.kakaoimagesearch_btype

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imgRes")
fun imgRes(imageView: ImageView, url:String) {
    // Glide
    Glide.with(imageView.context)
        .load(url)
        .into(imageView)
}
