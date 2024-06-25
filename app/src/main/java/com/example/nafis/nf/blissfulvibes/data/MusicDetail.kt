package com.example.nafis.nf.blissfulvibes.data

import java.util.concurrent.TimeUnit

data class MusicDetail(
    val musicId:String="",
    val musicTitle:String="",
    val musicAlbum:String="",
    val musicArtist:String="",
    val musicPath:String="",
    val duration:Long=0,
    val imgUri:String=""
)

fun formateDuration(duration: Long):String{
    val minutes=TimeUnit.MINUTES.convert(duration,TimeUnit.MILLISECONDS)
    val second=(TimeUnit.SECONDS.convert(duration,TimeUnit.MILLISECONDS)-
            minutes*TimeUnit.SECONDS.convert(1,TimeUnit.MINUTES))
    return String.format("%02d:%02d",minutes,second)
}