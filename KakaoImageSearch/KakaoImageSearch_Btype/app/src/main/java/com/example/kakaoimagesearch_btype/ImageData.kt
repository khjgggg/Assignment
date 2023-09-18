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