package com.example.nafis.nf.blissfulvibes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.nafis.nf.blissfulvibes.databinding.ActivityAppSettingBinding

class AppSetting : AppCompatActivity() {
    private lateinit var binding: ActivityAppSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAppSettingBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.settingtoolbar.setNavigationOnClickListener { onBackPressed() }

    }
}