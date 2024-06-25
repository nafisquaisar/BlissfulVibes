package com.example.nafis.nf.blissfulvibes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.nafis.nf.blissfulvibes.databinding.ActivityPlayNextOperationBinding

class PlayNextOperation : AppCompatActivity() {
    private lateinit var binding: ActivityPlayNextOperationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPlayNextOperationBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.playlisttoolbar.setNavigationOnClickListener { onBackPressed() }
    }
}