package com.example.nafis.nf.blissfulvibes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.nafis.nf.blissfulvibes.databinding.ActivityPlayListBinding
import com.example.nafis.nf.blissfulvibes.databinding.ActivityPlayNextOperationBinding

class PlayList : AppCompatActivity() {
    private lateinit var binding: ActivityPlayListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPlayListBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.playlisttoolbar.setNavigationOnClickListener { onBackPressed() }
    }
}