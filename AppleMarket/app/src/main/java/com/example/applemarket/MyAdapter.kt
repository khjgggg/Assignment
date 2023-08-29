package com.example.applemarket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.applemarket.databinding.ItemRecyclerviewBinding
import java.text.DecimalFormat

class MyAdapter (val dataList: MutableList<ItemGoods>) : RecyclerView.Adapter<MyAdapter.Holder>() {

    interface ItemClick {
        fun onClick(position: Int)
    }

    var itemClick: ItemClick? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.Holder {
        val binding = ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: MyAdapter.Holder, position: Int) {
// 천단위 콤마처리(2)
// val addCommaIncludeWon = DecimalFormat("#.###")

        holder.itemView.setOnClickListener {
            itemClick?.onClick(position)

            //아이템 클릭 이벤트 발생
            //인터페이스를 사용해서 메인으로 해당 이벤트의 정보를 넘겨주고 싶어.
            //그럼 받는 쪽(메인액티비티)에서 필요한 정보를 여기서 넘겨줌
        }
        val size = dataList.size
        holder.icItemImg.setImageResource(dataList[position].aIcon)
        holder.name.text = dataList[position].aName
        holder.address.text = dataList[position].aAddress
        holder.price.text = addCommaIncludeWon(dataList[position].aPrice)
        holder.tvChatNum.text =dataList[position].aComment
        holder.tvLikeNum.text = dataList[position].alike
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class Holder(val binding: ItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root){
        val icItemImg = binding.iconItem
        val name = binding.tvName
        val address = binding.tvAddress
        val price = binding.tvPrice
        val tvChatNum = binding.tvChatCount
        val tvLikeNum = binding.tvLikeCount


    }
}
