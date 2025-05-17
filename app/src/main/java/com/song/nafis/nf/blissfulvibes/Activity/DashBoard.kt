package com.song.nafis.nf.blissfulvibes.Activity

import com.song.nafis.nf.blissfulvibes.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.song.nafis.nf.blissfulvibes.AppSetting
import com.song.nafis.nf.blissfulvibes.BaseSetting
import com.song.nafis.nf.blissfulvibes.Fragment.HomeFragment
import com.song.nafis.nf.blissfulvibes.Fragment.MusicSearchFragment
import com.song.nafis.nf.blissfulvibes.Fragment.PlayListFragment
import com.song.nafis.nf.blissfulvibes.Fragment.ProfileFragment

import com.song.nafis.nf.blissfulvibes.PlaymusicList
import com.song.nafis.nf.blissfulvibes.UI.AuthViewModel
import com.song.nafis.nf.blissfulvibes.databinding.ActivityDashBoardBinding
import dagger.hilt.android.AndroidEntryPoint
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle


@AndroidEntryPoint
class DashBoard : AppCompatActivity() {
    lateinit var binding: ActivityDashBoardBinding
    private lateinit var toolbar: Toolbar
    private lateinit var drawerToggle: DuoDrawerToggle

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDashBoardBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        toolbar = binding.hometoolbar
        setSupportActionBar(toolbar)

        clickbtn()
        drawer()
        setprofile()
        setBottomNavigation(savedInstanceState)

    }

    private fun setBottomNavigation(savedInstanceState: Bundle?) {
        binding.BottomNavigationBar.setOnItemSelectedListener { item ->
            val id = item.itemId
            val preferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
            val editor = preferences.edit()

            when (id) {
                R.id.nav_home -> {
                    loadfragment(HomeFragment(), 0)
                    editor.putString("LAST_FRAGMENT", "Home")
                }
                R.id.nav_music_search -> {
                    loadfragment(MusicSearchFragment(), 1)
                    editor.putString("LAST_FRAGMENT", "search")
                }
//                R.id.nav_Shuffle -> {
//                    startActivity(Intent(this@DashBoard, PlaymusicList::class.java))
//                }
                R.id.nav_mymusic -> {
                    loadfragment(PlayListFragment(), 1)
                    editor.putString("LAST_FRAGMENT", "playlist")
                }
                R.id.nav_profile -> {
                    loadfragment(ProfileFragment(), 1)
                    editor.putString("LAST_FRAGMENT", "Profile")
                }
            }
            editor.apply()
            true
        }

        val preferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val lastFragment = preferences.getString("LAST_FRAGMENT", "Home")

        if (savedInstanceState == null) {
            // If the app is opened for the first time, load Home
            binding.BottomNavigationBar.selectedItemId = R.id.nav_home
        } else {
            // If returning from another activity, restore the last fragment
            when (lastFragment) {
                "Home" -> binding.BottomNavigationBar.selectedItemId = R.id.nav_home
                "search" -> binding.BottomNavigationBar.selectedItemId = R.id.nav_music_search
                "playlist" -> binding.BottomNavigationBar.selectedItemId = R.id.nav_mymusic
                "Profile" -> binding.BottomNavigationBar.selectedItemId = R.id.nav_profile
            }
        }

    }

    private fun setprofile() {
        var shimmerLayout=binding.proShimmer.root


        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
        binding.profileHeader.visibility = View.GONE



        viewModel.fetchUserData { user ->
            if (user != null) {
                binding.proName.text = user.name.ifEmpty { "Blissful Vibes" }
                binding.proEmail.text=user.email.ifEmpty{""}

                if (user.imgUrl.isNotEmpty()) {
                    Glide.with(this)
                        .load(user.imgUrl)
                        .placeholder(R.drawable.profileicon) // Optional placeholder while loading
                        .error(R.drawable.profileicon)       // Optional fallback on error
                        .into(binding.profileImg)

                } else {
                    binding.profileImg.setImageResource(R.drawable.profileicon)
                }
                // After data loads
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
                binding.profileHeader.visibility = View.VISIBLE
            } else {
                binding.proName.text = "Blissful Vibes"
                binding.profileImg.setImageResource(R.drawable.profileicon)
                binding.proEmail.text=""
                // After data loads
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
                binding.profileHeader.visibility = View.VISIBLE
            }
        }

    }

    // 2. Handle activity resume properly
    override fun onResume() {
        super.onResume()
        if (!isFinishing && !isDestroyed) {
            setprofile()
        }
    }


    private fun clickbtn() {
        binding.apply {

            llBase.setOnClickListener {
                startActivity(Intent(this@DashBoard, BaseSetting::class.java))
            }
            llSetting.setOnClickListener {
                startActivity(Intent(this@DashBoard, AppSetting::class.java))
            }
            llShuffle.setOnClickListener {
                startActivity(Intent(this@DashBoard, PlaymusicList::class.java))
            }
            llExit.setOnClickListener {
                val builder = MaterialAlertDialogBuilder(this@DashBoard)
                    .setTitle("LogOut")
                    .setMessage("Are you Sure to LogOut")
                    .setPositiveButton("Yes") { _, _ ->
                        viewModel.logout()
                        val intent = Intent(this@DashBoard, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }

                val alertDialog = builder.create()
                alertDialog.show()
                val color = ContextCompat.getColor(this@DashBoard, R.color.icon_color)
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color)
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color)
            }


        }
    }

    private fun drawer() {

        val drawerLayout = binding.drawer
        drawerToggle = object : DuoDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerOpened(drawerView: android.view.View) {
                super.onDrawerOpened(drawerView)
                // Change to back arrow and tint it white
                toolbar.navigationIcon = ContextCompat.getDrawable(this@DashBoard, R.drawable.back_arrow)?.mutate()
                toolbar.navigationIcon?.setTint(ContextCompat.getColor(this@DashBoard, R.color.white))
            }

            override fun onDrawerClosed(drawerView: android.view.View) {
                super.onDrawerClosed(drawerView)
                // Change to hamburger icon and tint it white
                toolbar.navigationIcon = ContextCompat.getDrawable(this@DashBoard, R.drawable.drawer_icon)?.mutate()
                toolbar.navigationIcon?.setTint(ContextCompat.getColor(this@DashBoard, R.color.white))
            }
        }

        drawerLayout.setDrawerListener(drawerToggle)
        drawerToggle.syncState()
        // ✅ Set white color to the initial navigation icon here
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.drawer_icon)?.mutate()
        toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.white))

    }




    //  ***************** Fragment Load Function *****************************
    fun loadfragment(fragment: Fragment, flag:Int){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (flag == 0) {
            fragmentTransaction.add(R.id.container, fragment)
        } else {
            fragmentTransaction.replace(R.id.container, fragment)
        }
        fragmentTransaction.commit()
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
                Toast.makeText(this@DashBoard, newText.toString(), Toast.LENGTH_SHORT).show()
//                musiclistSerach = ArrayList()
//                if (newText != null) {
//                    val userInput = newText.lowercase()
//                    for (song in musiclist) {
//                        if (song.musicTitle.lowercase().contains(userInput)) {
//                            musiclistSerach.add(song)
//                        }
//                    }
//                    search = true
//                }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }


}