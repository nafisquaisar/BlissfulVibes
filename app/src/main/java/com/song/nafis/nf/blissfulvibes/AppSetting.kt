package com.song.nafis.nf.blissfulvibes

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.song.nafis.nf.blissfulvibes.databinding.ActivityAppSettingBinding

class AppSetting : AppCompatActivity() {
    private lateinit var binding: ActivityAppSettingBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        // Load the theme based on the saved preference before setting the content view
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
//        if (sharedPreferences.getBoolean("dark_mode", false)) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        }

        binding = ActivityAppSettingBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        // Set up toolbar navigation
        binding.settingtoolbar.setNavigationOnClickListener { onBackPressed() }

//        // Initialize the theme switch
//        val themeSwitch = findViewById<SwitchMaterial>(R.id.switch_theme)
//        themeSwitch.isChecked = sharedPreferences.getBoolean("dark_mode", false)
//
//        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            }
//            // Save the new theme preference
//            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply()
//        }
    }
}
