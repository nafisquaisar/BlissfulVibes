package com.example.nafis.nf.blissfulvibes.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.nafis.nf.blissfulvibes.databinding.FavItemLayoutBinding

class FavoriteViewHolder(
    var binding: FavItemLayoutBinding
):RecyclerView.ViewHolder(binding.root) {
        val musictitle=binding.songName
        val musicImg=binding.songimg
      val root=binding.root
}
