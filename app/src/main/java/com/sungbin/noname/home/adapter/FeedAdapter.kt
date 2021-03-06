package com.sungbin.noname.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sungbin.noname.databinding.ItemBoardBinding
import com.sungbin.noname.databinding.ItemLoadingBinding
import com.sungbin.noname.home.data.Board
import com.sungbin.noname.home.data.ImageItems
import com.sungbin.noname.home.data.MemberDto
import com.sungbin.noname.home.viewmodel.SharedViewModel
import com.sungbin.noname.util.BindingConversions.setImageAdapter

class FeedAdapter(val viewModel: SharedViewModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val FEED_TYPE = 0
    private val LOADING_TYPE = 1

    var items = mutableListOf<Board>()

    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(v: View, data: Board)
    }
    fun setOnItemClickLister(listener: OnItemClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            FEED_TYPE ->{
                val binding = ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return FeedViewHolder(binding)
            }
            else ->{
                val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return LoadingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = items[position]
        if (holder is FeedViewHolder) {
            holder.bind(data, viewModel)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int =
        when(items[position].id){
            -1 -> LOADING_TYPE
            else -> FEED_TYPE
        }

    fun setList(boards: MutableList<Board>) {
        items.addAll(boards)
        items.add((Board("", listOf(),-1, memberDto = MemberDto(0,",","","")))) // progress bar ?????? ??????
    }

    fun deleteLoading(){
        items.removeAt(items.lastIndex) // ????????? ???????????? ????????????????????? ??????
    }

    inner class FeedViewHolder(binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        private val binding = binding
        fun bind(data: Board, viewModel: SharedViewModel) {
            binding.data = data
            binding.vm = viewModel
            binding.indicator = binding.dotsIndicator
            binding.executePendingBindings()

            binding.feedProfileImage.setOnClickListener { view ->
                listener.onItemClick(view, data)
            }
            binding.feedProfileNickname.setOnClickListener { view ->
                listener.onItemClick(view, data)
            }
            binding.feedHeart.setOnClickListener { view ->
                listener.onItemClick(view, data)
            }
            binding.feedFillHeart.setOnClickListener { view ->
                listener.onItemClick(view, data)
            }
            binding.feedComment.setOnClickListener { view ->
                listener.onItemClick(view, data)
            }
            binding.feedShare.setOnClickListener { view ->
                listener.onItemClick(view, data)
            }
            binding.feedLikeCount.setOnClickListener { view ->
                listener.onItemClick(view, data)
            }
            binding.feedLikeText.setOnClickListener { view ->
                listener.onItemClick(view, data)
            }
        }
    }
    inner class LoadingViewHolder(binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root)
}