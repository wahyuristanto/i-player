package com.i.player.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.i.player.R
import com.i.player.databinding.ItemSongsBinding
import com.i.player.models.SongModel

class MainScreenAdapter: RecyclerView.Adapter<MainScreenAdapter.ViewHolder>() {
    private val data = ArrayList<SongModel>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun getData(): ArrayList<SongModel> {
        return data
    }
    fun setData(item: ArrayList<SongModel>?) {
        data.clear()
        item?.let { data.addAll(it) }
        notifyDataSetChanged()
    }

    fun updateData(item: SongModel, position: Int){
        this.data.removeAt(position)
        this.data.add(0, item)
        notifyDataSetChanged()
    }

    fun addData(item: SongModel){
        this.data.add(item)
        notifyDataSetChanged()
    }

    fun setOnClickItemCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: SongModel)
    }

    inner class ViewHolder(private val binding: ItemSongsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SongModel) {
            with(binding) {
                Glide.with(root)
                    .load(item.artworkUrl100)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(coverImage)
                txtSubTitle.text = item.trackName
                txtTitle.text = item.artistName
                itemView.setOnClickListener{ onItemClickCallback?.onItemClicked(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSongsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}