package com.example.nafis.nf.blissfulvibes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nafis.nf.blissfulvibes.adapter.MusicAdapter
import com.example.nafis.nf.blissfulvibes.databinding.ActivityAddSongPlaylistBinding

class add_song_playlist : AppCompatActivity() {
    private lateinit var binding: ActivityAddSongPlaylistBinding
    private lateinit var adapter:MusicAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddSongPlaylistBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setSupportActionBar(binding.playlisttoolbar)
        binding.playlisttoolbar.setNavigationOnClickListener { onBackPressed() }

        setAdapter()
        searchmusic()
        binding.doneaddsong.setOnClickListener { finish() }

    }

    private fun setAdapter() {
        binding.apply {
            PlaylistRecylerView.setHasFixedSize(true)
            PlaylistRecylerView.setItemViewCacheSize(13)
            PlaylistRecylerView.layoutManager=LinearLayoutManager(this@add_song_playlist)
            adapter = MusicAdapter(this@add_song_playlist, MainActivity.musiclist, selection = true)
            PlaylistRecylerView.adapter = adapter
        }
    }

    private fun searchmusic() {
        binding.searchPlaylistMusic.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("MainActivity", "onQueryTextSubmit: $query")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("MainActivity", "onQueryTextChange: $newText")
                Toast.makeText(this@add_song_playlist, newText.toString(), Toast.LENGTH_SHORT).show()
                MainActivity.musiclistSerach = ArrayList()
                if (newText != null) {
                    val userInput = newText.lowercase()
                    for (song in MainActivity.musiclist) {
                        if (song.musicTitle.lowercase().contains(userInput)) {
                            MainActivity.musiclistSerach.add(song)
                        }
                    }
                    MainActivity.search = true
                    adapter.updateMusicList(searchList = MainActivity.musiclistSerach)
                }
                return true
            }
        })

    }
}