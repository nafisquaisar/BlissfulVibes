package com.example.nafis.nf.blissfulvibes.Service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import com.example.nafis.nf.blissfulvibes.BroadReciver.NotificationReciver
import com.example.nafis.nf.blissfulvibes.MainActivity
import com.example.nafis.nf.blissfulvibes.PlaymusicList
import com.example.nafis.nf.blissfulvibes.R
import com.example.nafis.nf.blissfulvibes.application.ApplicationClass
import com.example.nafis.nf.blissfulvibes.data.formateDuration
import com.example.nafis.nf.blissfulvibes.data.getImgPath

class MusicService: Service() {
    private var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var runnable: Runnable

    override fun onBind(intent: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext, "BlissFul Music")
        return myBinder
    }

    inner class MyBinder: Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
    }

    fun showNotification(playPauseBtn: Int) {
        val intent = Intent(baseContext, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val prevIntent = Intent(baseContext, NotificationReciver::class.java).setAction(ApplicationClass.PREVIOUS)
        val prevPendingIntent = PendingIntent.getBroadcast(baseContext, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val playIntent = Intent(baseContext, NotificationReciver::class.java).setAction(ApplicationClass.PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(baseContext, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val nextIntent = Intent(baseContext, NotificationReciver::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(baseContext, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val exitIntent = Intent(baseContext, NotificationReciver::class.java).setAction(ApplicationClass.EXIT)
        val exitPendingIntent = PendingIntent.getBroadcast(baseContext, 0, exitIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val imgArt = getImgPath(PlaymusicList.musicPlayList[PlaymusicList.songPosition].musicPath)
        val img = if (imgArt != null) {
            BitmapFactory.decodeByteArray(imgArt, 0, imgArt.size)
        } else {
            BitmapFactory.decodeResource(resources, R.drawable.notification_music_img)
        }

        val notification = NotificationCompat.Builder(this, ApplicationClass.CHANNEL_ID)
            .setContentIntent(contentIntent)
            .setContentTitle(PlaymusicList.musicPlayList[PlaymusicList.songPosition].musicTitle)
            .setContentText(PlaymusicList.musicPlayList[PlaymusicList.songPosition].musicArtist)
            .setSmallIcon(R.mipmap.music_icon)
            .setLargeIcon(img)
            .setStyle(MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.previous_notification, "Previous", prevPendingIntent)
            .addAction(playPauseBtn, "Play", playPendingIntent)
            .addAction(R.drawable.nextplay_notification, "Next", nextPendingIntent)
            .addAction(R.drawable.baseline_exit_to_app_24, "Exit", exitPendingIntent)
            .build()

        startForeground(13, notification)
    }

    fun createMediaFun() {
        try {
            if (PlaymusicList.musicService?.mediaPlayer == null) PlaymusicList.musicService?.mediaPlayer = MediaPlayer()
            PlaymusicList.musicService?.mediaPlayer?.reset()
            PlaymusicList.musicService?.mediaPlayer?.setDataSource(PlaymusicList.musicPlayList[PlaymusicList.songPosition].musicPath)
            PlaymusicList.musicService?.mediaPlayer?.prepare()
            PlaymusicList.binding.playMusicplaypausebtn.setImageResource(R.drawable.pause_button)
            PlaymusicList.musicService!!.showNotification(R.drawable.pause_notification)
            PlaymusicList.binding.musicTimeStart.text = formateDuration(mediaPlayer!!.currentPosition.toLong())
            PlaymusicList.binding.musicEndTime.text = formateDuration(mediaPlayer!!.duration.toLong())
            PlaymusicList.binding.playMusicSeek.progress = 0
            PlaymusicList.binding.playMusicSeek.max = PlaymusicList.musicService!!.mediaPlayer!!.duration
            PlaymusicList.nowPlayingId = PlaymusicList.musicPlayList[PlaymusicList.songPosition].musicId
        } catch (e: Exception) {
            e.printStackTrace()  // Handle the exception appropriately
        }
    }

    fun setupSeekbar() {
        runnable = Runnable {
            PlaymusicList.binding.musicTimeStart.text = formateDuration(mediaPlayer!!.currentPosition.toLong())
            PlaymusicList.binding.playMusicSeek.progress = mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
    }
}
