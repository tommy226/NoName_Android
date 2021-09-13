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
import com.sungbin.noname.util.BindingConversions.setImageAdapter

class FeedAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val FEED_TYPE = 0
    private val LOADING_TYPE = 1

    var items = mutableListOf<Board>()

    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(v: View, data: Board, binding: ItemBoardBinding)
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
            holder.bind(data)
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
        items.add((Board("", listOf(),-1, memberDto = MemberDto(0,",","","")))) // progress bar 넣을 자리
    }

    fun deleteLoading(){
        items.removeAt(items.lastIndex) // 로딩이 완료되면 프로그레스바를 지움
    }

    inner class FeedViewHolder(binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        private val binding = binding
        fun bind(data: Board) {
            binding.data = data
            binding.indicator = binding.dotsIndicator
            binding.executePendingBindings()

            binding.feedProfileImage.setOnClickListener { view ->
                listener.onItemClick(view, data, binding)
            }
            binding.feedProfileNickname.setOnClickListener { view ->
                listener.onItemClick(view, data, binding)
            }
            binding.feedHeart.setOnClickListener { view ->
                listener.onItemClick(view, data, binding)
            }
            binding.feedFillHeart.setOnClickListener { view ->
                listener.onItemClick(view, data, binding)
            }
            binding.feedComment.setOnClickListener { view ->
                listener.onItemClick(view, data, binding)
            }
            binding.feedShare.setOnClickListener { view ->
                listener.onItemClick(view, data, binding)
            }
        }
    }
    inner class LoadingViewHolder(binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root)
}