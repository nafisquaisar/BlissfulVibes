package com.example.nafis.nf.blissfulvibes

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nafis.nf.blissfulvibes.adapter.FavoriteAdapter
import com.example.nafis.nf.blissfulvibes.data.MusicDetail
import com.example.nafis.nf.blissfulvibes.data.checkPlaylist
import com.example.nafis.nf.blissfulvibes.databinding.ActivityFavoriteMusicBinding

class FavoriteMusic : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteMusicBinding
    private lateinit var fabAdapter: FavoriteAdapter
    companion object{
         var musicListfb:ArrayList<MusicDetail> =ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.playlisttoolbar)
        binding.playlisttoolbar.setNavigationOnClickListener { onBackPressed() }
        musicListfb= checkPlaylist(musicListfb)
        setAdapter()
        if(musicListfb.size<1)binding.favoriteShufflebtn.visibility=View.INVISIBLE
        binding.favoriteShufflebtn.setOnClickListener {
            val intent = Intent(this@FavoriteMusic, PlaymusicList::class.java)
            intent.putExtra("index", 0)
            intent.putExtra("class", "FavoriteShuffle")
            startActivity(intent)
        }

    }

    private fun setAdapter() {
        binding.apply {
            FavoriteRecyclerView.setHasFixedSize(true)
            FavoriteRecyclerView.setItemViewCacheSize(13)
            FavoriteRecyclerView.layoutManager=GridLayoutManager(this@FavoriteMusic,4)
            fabAdapter=FavoriteAdapter(this@FavoriteMusic, musicListfb)
            FavoriteRecyclerView.adapter = fabAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.playlist_menu, menu)
        val searchView = menu?.findItem(R.id.search_playlist)?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Toast.makeText(this@FavoriteMusic, newText.toString(), Toast.LENGTH_SHORT).show()
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}
