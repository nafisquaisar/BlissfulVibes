package com.example.nafis.nf.blissfulvibes

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.provider.MediaStore.Audio.Playlists
import android.renderscript.ScriptGroup.Binding
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.nafis.nf.blissfulvibes.adapter.MusicAdapter
import com.example.nafis.nf.blissfulvibes.data.MusicDetail
import com.example.nafis.nf.blissfulvibes.databinding.ActivityMainBinding
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: Toolbar
    private lateinit var adapter: MusicAdapter

    companion object{
        lateinit var musiclist : ArrayList<MusicDetail>
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        toolbar = binding.hometoolbar
        clickbtn()
        drawer()
        if (requestRuntimePermission()){
        adapterset()
        }

    }

    // fetch data from file
    private fun fetchMusicData(): ArrayList<MusicDetail> {
        val templist = ArrayList<MusicDetail>()

        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID
        )
        val cursor = this.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            MediaStore.Audio.Media.DATE_ADDED + " DESC",
            null
        )

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                    val titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                    val albumIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
                    val artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                    val durationIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
                    val albumid= cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)

                    val dataIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)

                    if (idIndex != -1 && titleIndex != -1 && albumIndex != -1 && artistIndex != -1 &&
                        durationIndex != -1  && dataIndex != -1) {

                        val id = cursor.getString(idIndex)
                        val title = cursor.getString(titleIndex)
                        val album = cursor.getString(albumIndex)
                        val artist = cursor.getString(artistIndex)
                        val duration = cursor.getLong(durationIndex)
                        val data = cursor.getString(dataIndex)
                        val albumid = cursor.getLong(albumid).toString()
                        val uri= Uri.parse("content://media/external/audio/albumart")
                        val imgUri=Uri.withAppendedPath(uri,albumid).toString()
                        val musicDetail = MusicDetail(musicId =id , musicTitle =  title, musicAlbum =  album, musicArtist = artist, musicPath = data,duration=duration,imgUri=imgUri)
                        templist.add(musicDetail)
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
        }

        return templist
    }



    //    set adapter into the recyclerview
    private fun adapterset() {

        musiclist=fetchMusicData()
        binding.apply {
            homeRecycler.setHasFixedSize(true)
            homeRecycler.setItemViewCacheSize(13)
            adapter=MusicAdapter(this@MainActivity, musiclist)
            homeRecycler.adapter=adapter
            totalsong.text="Total Songs : "+adapter.itemCount.toString()
        }

    }

    //    drawer Function
    private fun drawer() {
        val drawerLayout = binding.drawer
        val drawerToggle = DuoDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, // Assuming these are defined in your strings.xml
            R.string.navigation_drawer_close
        )

        drawerLayout.setDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }

//    runtime permision start
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestRuntimePermission() :Boolean{
        if (ActivityCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO), 101)
            return false
        }
    return true
    }

//    manager permission start
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT)
                adapterset()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO),
                    101
                )
            }
        }
    }

//    intent navigate btn start
    private fun clickbtn() {
        binding.apply {
            Homenextplaybtn.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        PlayNextOperation::class.java
                    )
                )
            }
            homePlaylisttbtn.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        PlayList::class.java
                    )
                )
            }
            homefavrouteBtn.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        FavoriteMusic::class.java
                    )
                )
            }
            Homeshufflebtn.setOnClickListener {
                var intent=Intent(this@MainActivity, PlaymusicList::class.java)
                intent.putExtra("index",0)
                intent.putExtra("class","MainActivity")
                startActivity(intent)
            }
            llBase.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        BaseSetting::class.java
                    )
                )
            }
            llSetting.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        AppSetting::class.java
                    )
                )
            }
            llShuffle.setOnClickListener { startActivity(Intent(this@MainActivity, PlaymusicList::class.java))
            }
        }
    }
}