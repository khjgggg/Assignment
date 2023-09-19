package com.example.kakaoimagesearch_btype

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaoimagesearch_btype.databinding.LayoutPhotoItemBinding

class StaggeredMyArchiveGridAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ItemClick {
        fun onClick(position: Int)
        fun onLongClick(position: Int)

        fun onClickAddFolder(doc: ImageData.Document)
    }

    var itemClick: ItemClick? = null
    private val items = mutableListOf<ImageData.Document>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StaggeredMyArchiveGridItemViewHolder(
            LayoutPhotoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        val gridItemViewHolder = (holder as StaggeredMyArchiveGridItemViewHolder)
        gridItemViewHolder.bind(item)
        gridItemViewHolder.itemView.setOnClickListener {
            itemClick?.onClick(position)
        }
        gridItemViewHolder.itemView.setOnLongClickListener {
            itemClick?.onLongClick(position)
            true
        }
        gridItemViewHolder.binding.ivAddFolder.setOnClickListener {
            itemClick?.onClickAddFolder(item)
        }
        gridItemViewHolder.binding.ivFavorite.setOnClickListener {
        }
    }

    fun addItems(resData: MutableList<ImageData.Document>) {
        items.clear()
        items.addAll(resData)
        notifyDataSetChanged()
    }

    inner class StaggeredMyArchiveGridItemViewHolder(val binding: LayoutPhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ImageData.Document) {
            binding.item = item
        }
    }
}
