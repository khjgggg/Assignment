package com.example.kakaoimagesearch_btype

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaoimagesearch_btype.databinding.LayoutPhotoItemBinding
import com.example.kakaoimagesearch_btype.databinding.LayoutVideoItemBinding

class StaggeredMyArchiveGridAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ItemClick {
        fun onClick(position: Int)
        fun onLongClick(doc: KakaoCommonData)

    }

    var itemClick: ItemClick? = null
    private val items = mutableListOf<KakaoCommonData>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.IMAGE -> {
                StaggeredMyArchiveGridItemViewHolder(
                    LayoutPhotoItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                StaggeredMyArchiveGridVideoItemViewHolder(
                    LayoutVideoItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ImageData.Document -> ViewType.IMAGE
            else -> {
                ViewType.VIDEO
            }
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is StaggeredMyArchiveGridItemViewHolder -> {
                holder.bind(item as ImageData.Document)
                holder.binding.ivAddFolder.isSelected = true
                holder.itemView.setOnClickListener {
                    itemClick?.onClick(position)
                }
                holder.itemView.setOnLongClickListener {
                    itemClick?.onLongClick(item)
                    true
                }

            }
            else -> {
                val videoItemViewHolder = (holder as StaggeredMyArchiveGridVideoItemViewHolder)
                videoItemViewHolder.bind(item as VideoData.Document)
                holder.binding.ivAddFolder.isSelected = true
                videoItemViewHolder.itemView.setOnClickListener {
                    itemClick?.onClick(position)
                }
                videoItemViewHolder.itemView.setOnLongClickListener {
                    itemClick?.onLongClick(item)
                    true
                }

            }
        }
    }

    fun addItems(resData: MutableList<KakaoCommonData>) {
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

    inner class StaggeredMyArchiveGridVideoItemViewHolder(val binding: LayoutVideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VideoData.Document) {
            binding.item = item
        }
    }
}
