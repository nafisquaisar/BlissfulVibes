package com.example.nafis.nf.blissfulvibes.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.nafis.nf.blissfulvibes.databinding.PlaylistViewBinding

class PlaylistViewHolder (var binding: PlaylistViewBinding) :RecyclerView.ViewHolder(binding.root){
        var plName=binding.playlistName
        var plimg=binding.plalistImg
        var root=binding.root
        var morebtn=binding.morebtn
        var totalsong=binding.playlistTotalsong
}
