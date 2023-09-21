package com.example.kakaoimagesearch_btype

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageData(
    val documents: MutableList<Document>?,
    val meta: Meta?
):Parcelable {
    @Parcelize
    data class Document(
        val collection: String?,
        val datetime: String?,
        val display_sitename: String?,
        val doc_url: String?,
        val height: Int?,
        val image_url: String?,
        val thumbnail_url: String?,
        val width: Int?
    ):Parcelable
    @Parcelize
    data class Meta(
        val is_end: Boolean?,
        val pageable_count: Int?,
        val total_count: Int?
    ):Parcelable
}

@Parcelize
data class VideoData(
    val documents: MutableList<Document>?,
    val meta: Meta?
):Parcelable {
    @Parcelize
    data class Document(
        val title:	String?,	//동영상 제목
        val url: String?,	//동영상 링크
        val datetime: String?,	//동영상 등록일, ISO 8601 [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]
        val play_time: Int?,	//동영상 재생시간, 초 단위
        val thumbnail: String?,	//동영상 미리보기 URL
        val author:	String?	//동영상 업로더
    ):Parcelable
    @Parcelize
    data class Meta(
        val is_end: Boolean?,
        val pageable_count: Int?,
        val total_count: Int?
    ):Parcelable
}