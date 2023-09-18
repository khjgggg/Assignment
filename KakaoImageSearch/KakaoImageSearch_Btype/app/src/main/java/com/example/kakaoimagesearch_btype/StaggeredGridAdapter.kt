package com.example.kakaoimagesearch_btype

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaoimagesearch_btype.databinding.ItemRecyclerviewBinding

class StaggeredGridAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ItemClick {
        fun onClick(position: Int)
        fun onLongClick(position: Int)
    }

    var itemClick: ItemClick? = null
    private val items = mutableListOf<ImageData.Document>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StaggeredGridItemViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        (holder as StaggeredGridItemViewHolder).bind(item)
        holder.itemView.setOnClickListener {
            itemClick?.onClick(position)
        }
        holder.itemView.setOnLongClickListener {
            itemClick?.onLongClick(position)
            true
        }
    }

    fun addItems(resData: MutableList<ImageData.Document>) {
        items.clear()
        items.addAll(resData)
        notifyDataSetChanged()
    }

    inner class StaggeredGridItemViewHolder(private val binding: ItemRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ImageData.Document) {
            binding.item = item
        }
    }
}