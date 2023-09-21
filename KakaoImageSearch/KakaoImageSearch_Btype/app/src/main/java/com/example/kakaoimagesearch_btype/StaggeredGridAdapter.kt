package com.example.kakaoimagesearch_btype

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaoimagesearch_btype.databinding.LayoutPhotoItemBinding
import com.example.kakaoimagesearch_btype.databinding.LayoutVideoItemBinding

class StaggeredGridAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ItemClick {
        fun onClick(position: Int)
        fun onLongClick(position: Int)
        fun onClickAddFolder(doc: KakaoCommonData)
    }

    var itemClick: ItemClick? = null
    private val items = mutableListOf<KakaoCommonData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.IMAGE -> {
                ImageItemViewHolder(
                    LayoutPhotoItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                VideoItemViewHolder(
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
            is ImageItemViewHolder -> {
                holder.bind(item as ImageData.Document)
                holder.itemView.setOnClickListener {
                    itemClick?.onClick(position)
                }
                holder.itemView.setOnLongClickListener {
                    itemClick?.onLongClick(position)
                    true
                }
                holder.binding.ivAddFolder.setOnClickListener {
                    itemClick?.onClickAddFolder(item)
                }
            }

            else -> {
                val videoItemViewHolder = (holder as StaggeredGridAdapter.VideoItemViewHolder)
                videoItemViewHolder.bind(item as VideoData.Document)
                videoItemViewHolder.itemView.setOnClickListener {
                    itemClick?.onClick(position)
                }
                videoItemViewHolder.itemView.setOnLongClickListener {
                    itemClick?.onLongClick(position)
                    true
                }
                videoItemViewHolder.binding.ivAddFolder.setOnClickListener {
                    itemClick?.onClickAddFolder(item)
                }
            }
        }

    }

    fun addItems(resData: List<KakaoCommonData>) {
        items.clear()
        items.addAll(resData)
        notifyDataSetChanged()
    }

    inner class ImageItemViewHolder(val binding: LayoutPhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ImageData.Document) {
            binding.item = item
        }
    }

    inner class VideoItemViewHolder(val binding: LayoutVideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VideoData.Document) {
            binding.item = item
        }
    }
}
object ViewType {
    val IMAGE = 1
    val VIDEO = 2
}