package com.example.umc01

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.umc01.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    companion object {
        const val STRING_INTENT_KEY = "my_string_key"
    }

    private lateinit var binding: ActivityMainBinding

    private var song:Song = Song()
    private var gson: Gson = Gson()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Log.d("MainActivity", "Current theme before setting content view: " + resources.getResourceEntryName(R.style.SplashTheme))
        //setTheme(R.style.Theme_UMC01)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // HomeFragment를 기본 프래그먼트로 설정
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commit()
        }

        // ActivityResultLauncher 초기화
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val returnString = result.data?.getStringExtra(STRING_INTENT_KEY)
                Toast.makeText(this, returnString ?: "No data", Toast.LENGTH_SHORT).show()
            }
        }

        val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString(),0,60,false, "music_lilac")


        binding.mainPlayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java).apply {
                putExtra("title", song.title)
                putExtra("singer", song.singer)
//                putExtra("albumImage", R.drawable.img_album_exp3)
//                putExtra("lyrics01", "늘 뻔한 recipe")
//                putExtra("lyrics02", "착한 아이처럼")

                //타이머 구현 위한 데이터 전달
                putExtra("second", song.second)
                putExtra("playTime", song.playTime)
                putExtra("isPlaying", song.isPlaying)
                putExtra("music", song.music)
            }
            resultLauncher.launch(intent)
        }

        initBottomNavigation()


    }

    private fun initBottomNavigation() {
        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> replaceFragment(HomeFragment())
                R.id.lookFragment -> replaceFragment(LookFragment())
                R.id.searchFragment -> replaceFragment(SearchFragment())
                R.id.lockerFragment -> replaceFragment(LockerFragment())
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, fragment)
            .commitAllowingStateLoss()
        return true
    }

    private fun setMiniPlayer(song: Song){
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainMiniplayerProgressSb.progress = (song.second * 100000)/song.playTime
    }
    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("songData", null)

        song = if(songJson == null){
            Song("라일락", "아이유(IU)", 0, 60, false, "music_lilac")
        }else{
                gson.fromJson(songJson, Song::class.java)
        }

        setMiniPlayer(song)
    }
}
