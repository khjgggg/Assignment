package com.example.recyclerviewtest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.brandongogetap.stickyheaders.exposed.StickyHeaderHandler
import com.example.multiviewlist.MixItem
import com.example.multiviewlist.databinding.ItemRecyclerviewBinding
import com.example.multiviewlist.databinding.ItemTitleBinding

class MyAdapter(val dataList: MutableList<MixItem>) :StickyHeaderHandler, RecyclerView.Adapter<ViewHolder>(){

    interface ItemClick{
        fun onClick(view: View, position: Int )
    }
    companion object {
        private const val VIEW_TYPE_TITLE = 1
        private const val VIEW_TYPE_MYITEM = 2

    }
    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return when(viewType){
            VIEW_TYPE_TITLE -> {
                val binding =
                    ItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TitleViewHolder(binding)
            }

            else -> {
                val binding =
                    ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MyItemViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when (val item = dataList[position]) {
            is MixItem.MyTitle -> {
                (holder as TitleViewHolder).title.text = "${item.age} 살"
            }
            is MixItem.MyItem -> {
                (holder as MyItemViewHolder).name.text = item.aName
                holder.age.text = item.aAge
                holder.iconImageView.setImageResource(item.aIcon)

//                val size = dataList.size

                holder.itemView.setOnClickListener {
                    itemClick?.onClick(it, position)
                }
            }
        }

    }
    override fun getAdapterData():MutableList<MixItem>{
        return dataList
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    override fun getItemViewType(position: Int): Int {
        return when(dataList[position]){
            is MixItem.MyTitle -> VIEW_TYPE_TITLE
            is MixItem.MyItem -> VIEW_TYPE_MYITEM
        }
    }
    inner class MyItemViewHolder(val binding: ItemRecyclerviewBinding) : ViewHolder(binding.root) {
        val iconImageView = binding.iconItem
        val name = binding.textItem1
        val age = binding.textItem2
    }
    inner class TitleViewHolder(val binding: ItemTitleBinding) : ViewHolder(binding.root) {
        val title = binding.tvAgetitle
    }

}
