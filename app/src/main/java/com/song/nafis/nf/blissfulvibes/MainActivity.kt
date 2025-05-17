package com.song.nafis.nf.blissfulvibes

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.song.nafis.nf.blissfulvibes.adapter.MusicAdapter
import com.song.nafis.nf.blissfulvibes.Model.MusicDetail
import com.song.nafis.nf.blissfulvibes.Model.MusicPlaylist
import com.song.nafis.nf.blissfulvibes.Model.stopApplication
import com.song.nafis.nf.blissfulvibes.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: Toolbar
    private lateinit var adapter: MusicAdapter

    companion object {
        lateinit var musiclist: ArrayList<MusicDetail>
        lateinit var musiclistSerach: ArrayList<MusicDetail>
        var search: Boolean = false
        var sortOrder:Int=0
        val sortingList = arrayOf(
            MediaStore.Audio.Media.DATE_ADDED + " DESC",
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.SIZE + " DESC",
            MediaStore.Audio.Media.DISPLAY_NAME
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        toolbar = binding.hometoolbar
        setSupportActionBar(toolbar)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.back_arrow)

        // Handle back button click
            toolbar.setNavigationOnClickListener {
                finish()
                overridePendingTransition(android.R.anim.anticipate_interpolator, android.R.anim.anticipate_overshoot_interpolator)
            }



        clickbtn()
//        drawer()
        if (requestRuntimePermission()) {
            adapterset()
            // retirve data from sharePrefrence for Favrourate list
            FavoriteMusic.musicListfb = ArrayList()
            val edit = getSharedPreferences("Favorite", MODE_PRIVATE)
            val jsonString = edit.getString("FavoriteSong", null)
            val typeToken = object : TypeToken<ArrayList<MusicDetail>>() {}.type
            if (jsonString != null) {
                val data: ArrayList<MusicDetail> =
                    GsonBuilder().create().fromJson(jsonString, typeToken)
                FavoriteMusic.musicListfb.addAll(data)

                // retirve data from sharePrefrence for Playlist
                PlayList.musicPlaylist = MusicPlaylist()
                val jsonStringPlayList = edit.getString("Playlistmusic", null)
                if (jsonStringPlayList != null) {
                    val dataPlaylist: MusicPlaylist = GsonBuilder().create()
                        .fromJson(jsonStringPlayList, MusicPlaylist::class.java)
                    PlayList.musicPlaylist = dataPlaylist

                }
            }
        }
    }

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
            sortingList[sortOrder],
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
                    val albumid = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
                    val dataIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)

                    if (idIndex != -1 && titleIndex != -1 && albumIndex != -1 && artistIndex != -1 &&
                        durationIndex != -1 && dataIndex != -1
                    ) {

                        val id = cursor.getString(idIndex)
                        val title = cursor.getString(titleIndex)
                        val album = cursor.getString(albumIndex)
                        val artist = cursor.getString(artistIndex)
                        val duration = cursor.getLong(durationIndex)
                        val data = cursor.getString(dataIndex)
                        val albumid = cursor.getLong(albumid).toString()
                        val uri = Uri.parse("content://media/external/audio/albumart")
                        val imgUri = Uri.withAppendedPath(uri, albumid).toString()
                        val musicDetail = MusicDetail(
                            musicId = id, musicTitle = title, musicAlbum = album,
                            musicArtist = artist, musicPath = data, duration = duration, imgUri = imgUri
                        )
                        templist.add(musicDetail)
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
        }

        return templist
    }

    private fun adapterset() {
        search = false
        val sortingEditor=getSharedPreferences("Sorting", MODE_PRIVATE)
        sortOrder=sortingEditor.getInt("sortOrder",0)
        musiclist = fetchMusicData()
        binding.apply {
            homeRecycler.setHasFixedSize(true)
            homeRecycler.setItemViewCacheSize(13)
            adapter = MusicAdapter(this@MainActivity, musiclist)
            homeRecycler.adapter = adapter
            totalsong.text = "Total Songs : " + adapter.itemCount.toString()
        }
    }

//    private fun drawer() {
//        val drawerLayout = binding.drawer
//        val drawerToggle = DuoDrawerToggle(
//            this, drawerLayout, toolbar,
//            R.string.navigation_drawer_open, // Assuming these are defined in your strings.xml
//            R.string.navigation_drawer_close
//        )
//
//        drawerLayout.setDrawerListener(drawerToggle)
//        drawerToggle.syncState()
//    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestRuntimePermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                android.Manifest.permission.READ_MEDIA_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO), 101
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                adapterset()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO), 101
                )
            }
        }
    }

    private fun clickbtn() {
        binding.apply {
            Homenextplaybtn.setOnClickListener {
                startActivity(Intent(this@MainActivity, PlayNextOperation::class.java))
            }
            homePlaylisttbtn.setOnClickListener {
                startActivity(Intent(this@MainActivity, PlayList::class.java))
            }
            homefavrouteBtn.setOnClickListener {
                startActivity(Intent(this@MainActivity, FavoriteMusic::class.java))
            }
            Homeshufflebtn.setOnClickListener {
                val intent = Intent(this@MainActivity, PlaymusicList::class.java)
                intent.putExtra("index", 0)
                intent.putExtra("class", "MainActivity")
                startActivity(intent)
            }
//            llBase.setOnClickListener {
//                startActivity(Intent(this@MainActivity, BaseSetting::class.java))
//            }
//            llSetting.setOnClickListener {
//                startActivity(Intent(this@MainActivity, AppSetting::class.java))
//            }
//            llShuffle.setOnClickListener {
//                startActivity(Intent(this@MainActivity, PlaymusicList::class.java))
//            }
//            llExit.setOnClickListener {
//                val builder = MaterialAlertDialogBuilder(this@MainActivity)
//                    .setTitle("Exit")
//                    .setMessage("Are you Sure to Exit")
//                    .setPositiveButton("Yes") { _, _ ->
//                        stopApplication()
//                    }
//                    .setNegativeButton("No") { dialog, _ ->
//                        dialog.dismiss()
//                    }
//
//                val alertDialog = builder.create()
//                alertDialog.show()
//                val color = ContextCompat.getColor(this@MainActivity, R.color.icon_color)
//                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color)
//                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color)
//            }
            NowPlaying.setOnClickListener{
                startActivity(Intent(this@MainActivity,PlaymusicList::class.java))
            }
            sortbutton.setOnClickListener {
                sortingFun()
            }
        }
    }

//    ====================== sorting function implement==============================
private fun sortingFun() {
    val menuList = arrayOf("Recently Added", "Song Title", "File Size", "File Name")
    val builder = AlertDialog.Builder(this)
    builder.setTitle("Sort By")
        .setSingleChoiceItems(menuList, sortOrder) { _, which ->
            sortOrder = which
        }
        .setPositiveButton("OK") { _, _ ->
            val editor = getSharedPreferences("Sorting", MODE_PRIVATE).edit()
            editor.putInt("sortOrder", sortOrder)
            editor.apply()
            // Update the music list based on the new sorting order
            musiclist = fetchMusicData()
            adapter.updateMusicList(musiclist)
            binding.totalsong.text = "Total Songs: ${adapter.itemCount}"
        }
        .setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

    val dialog = builder.create()
    dialog.show()
    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,R.color.icon_color))
    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.icon_color))
}


    override fun onDestroy() {
        super.onDestroy()
//        if (!PlaymusicList.isPlay && PlaymusicList.musicService != null) {
//            PlaymusicList.musicService!!.stopForeground(true)
//            PlaymusicList.musicService!!.mediaPlayer?.release()
//            PlaymusicList.musicService = null
//        }
//        exitProcess(1)
    }

    override fun onResume() {
        super.onResume()
       val edit=getSharedPreferences("Favorite", MODE_PRIVATE).edit()
        val jsonString=GsonBuilder().create().toJson(FavoriteMusic.musicListfb)
        edit.putString("FavoriteSong",jsonString)
        val jsonStringPlaylist=GsonBuilder().create().toJson(PlayList.musicPlaylist)
        edit.putString("Playlistmusic",jsonStringPlaylist)
        edit.apply()
        val sortingEditor=getSharedPreferences("Sorting", MODE_PRIVATE)
        val sortValue=sortingEditor.getInt("sortOrder",0)
        if((sortOrder!=sortValue)){
            sortOrder=sortValue
            musiclist=fetchMusicData()
            adapter.updateMusicList(musiclist)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.playlist_menu, menu)
        val searchView = menu?.findItem(R.id.search_playlist)?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("MainActivity", "onQueryTextSubmit: $query")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("MainActivity", "onQueryTextChange: $newText")
                Toast.makeText(this@MainActivity, newText.toString(), Toast.LENGTH_SHORT).show()
                musiclistSerach = ArrayList()
                if (newText != null) {
                    val userInput = newText.lowercase()
                    for (song in musiclist) {
                        if (song.musicTitle.lowercase().contains(userInput)) {
                            musiclistSerach.add(song)
                        }
                    }
                    search = true
                    adapter.updateMusicList(searchList = musiclistSerach)
                }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}
