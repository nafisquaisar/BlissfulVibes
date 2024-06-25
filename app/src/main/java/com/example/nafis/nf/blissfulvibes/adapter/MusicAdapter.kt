package com.example.nafis.nf.blissfulvibes.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.nafis.nf.blissfulvibes.PlayList
import com.example.nafis.nf.blissfulvibes.PlaymusicList
import com.example.nafis.nf.blissfulvibes.R
import com.example.nafis.nf.blissfulvibes.data.MusicDetail
import com.example.nafis.nf.blissfulvibes.data.formateDuration
import com.example.nafis.nf.blissfulvibes.databinding.MusicLayoutBinding

class MusicAdapter(private val context: Context,private val list: ArrayList<MusicDetail>): RecyclerView.Adapter<MusicViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        return MusicViewHolder(MusicLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
         return list.size
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
           holder.title.text=list[position].musicTitle
           holder.album.text=list[position].musicAlbum
           holder.durationMs.text= formateDuration(list[position].duration)
           Glide.with(context)
               .load(list[position].imgUri)
               .apply(  RequestOptions().placeholder(R.mipmap.music_icon).centerCrop() )
               .into(holder.img)

          holder.root.setOnClickListener {
              val intent=Intent(context,PlaymusicList::class.java)
              intent.putExtra("index",position)
              intent.putExtra("class","MusicAdapter")
              ContextCompat.startActivity(context,intent,null)
          }
    }
}