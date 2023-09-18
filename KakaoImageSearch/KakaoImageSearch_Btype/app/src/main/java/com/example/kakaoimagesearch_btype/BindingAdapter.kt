package com.example.kakaoimagesearch_btype

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat

@BindingAdapter("imgRes")
fun imgRes(imageView: ImageView, url:String) {
    // Glide
    Glide.with(imageView.context)
        .load(url)
        .into(imageView)
}

@BindingAdapter("setDisplayText")
fun setDisplayText(textView: TextView, text: String) {
    textView.text = "[image] $text"
}

@BindingAdapter("setTextDatetime")
fun setTextDatetime(textView: TextView, text: String) {
    textView.text = dateformat(text)
}

fun dateformat(inputDateString: String): String {
    // 입력 문자열을 Date 객체로 파싱합니다.
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    val date = inputFormat.parse(inputDateString)

    // 출력 형식을 정의하고 원하는 형식으로 포맷합니다.
    val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    // 포맷팅된 문자열을 얻습니다.
    return outputFormat.format(date)
}
