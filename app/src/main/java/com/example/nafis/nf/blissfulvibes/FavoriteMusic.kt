package com.example.nafis.nf.blissfulvibes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.nafis.nf.blissfulvibes.databinding.ActivityFavoriteMusicBinding
import com.example.nafis.nf.blissfulvibes.databinding.ActivityPlayListBinding

class FavoriteMusic : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteMusicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFavoriteMusicBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.playlisttoolbar.setNavigationOnClickListener { onBackPressed() }
    }
}