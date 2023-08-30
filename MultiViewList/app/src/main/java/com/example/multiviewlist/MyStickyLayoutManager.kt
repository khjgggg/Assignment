package com.example.multiviewlist

import android.content.Context
import com.brandongogetap.stickyheaders.StickyLayoutManager
import com.example.recyclerviewtest.MyAdapter

class MyStickyLayoutManager(context: Context, headerHandler: MyAdapter)
    : StickyLayoutManager(context, headerHandler) {

    override fun scrollToPosition(position: Int) {
        super.scrollToPositionWithOffset(position, 0)
    }
}