package com.example.nafis.nf.blissfulvibes.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.nafis.nf.blissfulvibes.MainActivity
import com.example.nafis.nf.blissfulvibes.PlayList
import com.example.nafis.nf.blissfulvibes.PlaylistMusicListActivity
import com.example.nafis.nf.blissfulvibes.PlaymusicList
import com.example.nafis.nf.blissfulvibes.R
import com.example.nafis.nf.blissfulvibes.data.MusicDetail
import com.example.nafis.nf.blissfulvibes.data.formateDuration
import com.example.nafis.nf.blissfulvibes.databinding.MusicLayoutBinding

class MusicAdapter(private val context: Context,private var list: ArrayList<MusicDetail>,private val playlistDetail:Boolean=false,
    private val selection:Boolean=false): RecyclerView.Adapter<MusicViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        return MusicViewHolder(
            MusicLayoutBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.title.text = list[position].musicTitle
        holder.album.text = list[position].musicAlbum
        holder.durationMs.text = formateDuration(list[position].duration)
        Glide.with(context)
            .load(list[position].imgUri)
            .apply(RequestOptions().placeholder(R.mipmap.music_icon).centerCrop())
            .into(holder.img)

        when {
            playlistDetail -> {
                holder.root.setOnClickListener {
                    sendIntent(ref = "PlaylistMusicList", position)
                }
            }

            selection -> {
                    holder.root.setOnClickListener {
                        if(addmusic(list[position])){
                            holder.root.setBackgroundColor(ContextCompat.getColor(context,R.color.icon_color))
                        }else{
                            holder.root.setBackgroundColor(ContextCompat.getColor(context,R.color.white))
                        }
                    }
            }

            else -> {
                holder.root.setOnClickListener {
                    when {
                        MainActivity.search -> sendIntent(ref = "MusicAdapterSearch", position)
                        list[position].musicId == PlaymusicList.nowPlayingId -> {
                            sendIntent(ref = "NowPlaying", PlaymusicList.songPosition)
                        }

                        else -> sendIntent("MusicAdapter", position)
                    }
                }
            }
        }
    }

    fun updateMusicList(searchList: ArrayList<MusicDetail>) {
        list = ArrayList()
        list.addAll(searchList)
        notifyDataSetChanged()
    }

    fun sendIntent(ref: String, pos: Int) {
        val intent = Intent(context, PlaymusicList::class.java)
        intent.putExtra("index", pos)
        intent.putExtra("class", ref)
        ContextCompat.startActivity(context, intent, null)
    }

    fun addmusic(song: MusicDetail): Boolean {
        PlayList.musicPlaylist.ref[PlaylistMusicListActivity.currentPlaylistPos].playlist.forEachIndexed { index, musicDetail ->
            if (song.musicId == musicDetail.musicId) {
              PlayList.musicPlaylist.ref[PlaylistMusicListActivity.currentPlaylistPos].playlist.removeAt(index)
                return false
            }
        }
        PlayList.musicPlaylist.ref[PlaylistMusicListActivity.currentPlaylistPos].playlist.add(song)

        return true
    }

    fun refreshList(){
        list= ArrayList()
        list=PlayList.musicPlaylist.ref[PlaylistMusicListActivity.currentPlaylistPos].playlist
        notifyDataSetChanged()
    }
}