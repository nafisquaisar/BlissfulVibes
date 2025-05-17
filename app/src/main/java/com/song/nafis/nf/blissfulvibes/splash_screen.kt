package com.song.nafis.nf.blissfulvibes

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.song.nafis.nf.blissfulvibes.Activity.DashBoard
import com.song.nafis.nf.blissfulvibes.Activity.FrontActivity
import com.song.nafis.nf.blissfulvibes.UI.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class splash_screen : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler(Looper.getMainLooper()).postDelayed({
            val isLogin = viewModel.isLogin()
            val intent = if (isLogin) {
                Intent(this, DashBoard::class.java)
            } else {
                Intent(this, FrontActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 2000)
    }
}
