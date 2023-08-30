package com.example.multiviewlist

import com.brandongogetap.stickyheaders.exposed.StickyHeader

sealed class MixItem {
    data class MyItem(val aIcon: Int, val aName: String, val aAge: String) : MixItem()
    data class MyTitle(val age: String) : MixItem(), StickyHeader

}

val dataList = mutableListOf(
    MixItem.MyTitle("1"),
    MixItem.MyItem(R.drawable.cat1, "Luna", "1"),
    MixItem.MyItem(R.drawable.cat2, "Mari", "1"),
    MixItem.MyTitle("20"),
    MixItem.MyItem(R.drawable.img_8700, "khj", "20"),
    MixItem.MyItem(R.drawable.img_8703, "khj", "20"),
    MixItem.MyItem(R.drawable.img_8704, "khj", "20"),
    MixItem.MyItem(R.drawable.img_8705, "khj", "20"),
    MixItem.MyItem(R.drawable.img_8706, "khj", "20"),
    MixItem.MyItem(R.drawable.img_8707, "khj", "20"),
    MixItem.MyItem(R.drawable.img_8711, "khj", "20"),
    MixItem.MyTitle("30"),
    MixItem.MyItem(R.drawable.img_1, "khj", "30"),
    MixItem.MyItem(R.drawable.img_2, "khj", "30"),
    MixItem.MyItem(R.drawable.img_3, "khj", "30"),
    MixItem.MyItem(R.drawable.img_4, "khj", "30"),
    MixItem.MyItem(R.drawable.img_5, "khj", "30")
)