package com.example.umc01

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
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

//    // MainActivity 클래스에 MediaPlayer 멤버 변수 추가
//    private var mediaPlayer: MediaPlayer? = null
//
//    // 노래를 재생하는 함수 추가
//    private fun playSong(context: Context, songUri: Uri) {
//        // 기존에 재생 중인 노래가 있다면 정지하고 MediaPlayer를 해제합니다.
//        mediaPlayer?.release()
//
//        // MediaPlayer 초기화 및 노래 재생
//        mediaPlayer = MediaPlayer().apply {
//            setDataSource(context, songUri)
//            prepare()
//            start()
//        }
//    }
//
//    // 앨범 정보를 기반으로 노래 파일의 URI를 가져오는 함수
//    private fun getSongUri(album: Album): Uri {
//        // 앨범 정보를 기반으로 노래 파일의 파일명을 생성
//        val songFileName = "${album.title}_${album.singer}.mp3"
//
//        // 앱의 리소스로 제공된 raw 리소스의 URI를 생성
//        return Uri.parse("android.resource://" + packageName + "/raw/" + songFileName)
//    }

    fun updateMiniPlayer(album: Album) {
        // 미니플레이어 업데이트 코드를 여기에 추가
        binding.mainMiniplayerTitleTv.text = album.title
        binding.mainMiniplayerSingerTv.text = album.singer

//        // 노래를 재생하고 싶은 URI를 가져와서 노래를 재생합니다.
//        val songUri = getSongUri(album) // 노래의 URI를 가져오는 함수가 필요합니다.
//        playSong(context, songUri)
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
