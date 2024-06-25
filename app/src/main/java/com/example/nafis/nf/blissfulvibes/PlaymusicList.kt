package com.example.nafis.nf.blissfulvibes

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.nafis.nf.blissfulvibes.data.MusicDetail
import com.example.nafis.nf.blissfulvibes.databinding.ActivityPlaymusicListBinding

class PlaymusicList : AppCompatActivity() {
    private lateinit var binding: ActivityPlaymusicListBinding

    companion object{
        lateinit var musicPlayList: ArrayList<MusicDetail>
        var songpostion:Int=0
        var mediaPlayer:MediaPlayer?=null
        var isPlay:Boolean=false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPlaymusicListBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        initialiseLayout()
        binding.playMusicplaypausebtn.setOnClickListener {
            if (isPlay)pauseMusic()
            else playMusic()
        }

        binding.playMusicNextbtn.setOnClickListener {prenext(true) }
        binding.playMusicPreviousbtn.setOnClickListener {prenext(false)}

    }

    private fun pauseMusic() {
        binding.playMusicplaypausebtn.setImageResource(R.drawable.playbtn)
        isPlay=false
        mediaPlayer!!.pause()
    }

    private fun playMusic() {
        binding.playMusicplaypausebtn.setImageResource(R.drawable.pause_button)
       isPlay=true
       mediaPlayer!!.start()
    }

    private fun initialiseLayout() {
        songpostion=intent.getIntExtra("index",0)
        when(intent.getStringExtra("class")){
            "MusicAdapter"->{
                musicPlayList= ArrayList()
                musicPlayList.addAll(MainActivity.musiclist)
               createMediafun()
                setLayout()
            }
            "MainActivity"->{
                musicPlayList= ArrayList()
                musicPlayList.addAll(MainActivity.musiclist)
                musicPlayList.shuffle()
                createMediafun()
                setLayout()
            }
        }
    }

    private fun createMediafun() {
        if(mediaPlayer==null) mediaPlayer= MediaPlayer()
        mediaPlayer!!.reset()
        mediaPlayer!!.setDataSource(musicPlayList[songpostion].musicPath)
        mediaPlayer!!.prepare()
        mediaPlayer!!.start()
        isPlay=true
        binding.playMusicplaypausebtn.setImageResource(R.drawable.pause_button)
    }

    private fun setLayout(){
        Glide.with(this)
            .load(musicPlayList[songpostion].imgUri)
            .apply(  RequestOptions().placeholder(R.mipmap.music_icon).centerCrop() )
            .into(binding.playmusicImg)
        binding.playMusicTitle.text= musicPlayList[songpostion].musicTitle
    }

    // ============previous or next play function ===========================
    private fun prenext(increment:Boolean){
        if(increment){
                   setSongPosition(true)
                   setLayout()
                  createMediafun()
        }else{
            setSongPosition(false)
            setLayout()
            createMediafun()
        }
    }

    // ===============set song position check last position and first pos =========================
    private fun setSongPosition(increment: Boolean){
        if(increment){
            if(songpostion== musicPlayList.size-1)
                songpostion=0
            else
                ++songpostion
        }else{
            if(songpostion==0)
                songpostion= musicPlayList.size-1
            else
                --songpostion
        }
    }
}