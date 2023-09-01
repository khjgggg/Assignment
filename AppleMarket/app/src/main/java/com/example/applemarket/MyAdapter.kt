package com.example.applemarket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.applemarket.databinding.ItemRecyclerviewBinding
import java.text.DecimalFormat

class MyAdapter(val dataList: MutableList<ItemGoods>) : RecyclerView.Adapter<MyAdapter.Holder>() {

    interface ItemClick {
        fun onClick(position: Int)
        fun onLongClick(position: Int)
    }

    var itemClick: ItemClick? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.Holder {
        val binding =
            ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: MyAdapter.Holder, position: Int) {

        holder.itemView.setOnClickListener {
            itemClick?.onClick(position)
        }
        holder.itemView.setOnLongClickListener {
            itemClick?.onLongClick(position)
            true
        }
        holder.icItemImg.setImageResource(dataList[position].aIcon)
        holder.name.text = dataList[position].aName
        holder.address.text = dataList[position].aAddress
        holder.price.text = addCommaIncludeWon(dataList[position].aPrice)
        holder.tvChatNum.text = dataList[position].aComment
        holder.imgLike.isSelected = dataList[position].isFavor
        holder.tvLikeNum.text = aLikeCnt(dataList[position])
    }

    private fun aLikeCnt(item: ItemGoods): String {
        return if (item.isFavor) {
            (item.alike + 1).toString()
        } else item.alike.toString()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class Holder(val binding: ItemRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val icItemImg = binding.iconItem
        val name = binding.tvName
        val address = binding.tvAddress
        val price = binding.tvPrice
        val tvChatNum = binding.tvChatCount
        val imgLike = binding.imgLike
        val tvLikeNum = binding.tvLikeCount
    }
}
