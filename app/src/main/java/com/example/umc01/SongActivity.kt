package com.example.umc01

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.umc01.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra("title") && intent.hasExtra("singer")&&intent.hasExtra("albumImage")&&intent.hasExtra("lyrics01")&&intent.hasExtra("lyrics02")){
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
            binding.songMusicLyrics01Iv.text = intent.getStringExtra("lyrics01")
            binding.songMusicLyrics02Iv.text = intent.getStringExtra("lyrics02")
        }


        // 인텐트에서 이미지 리소스 ID 추출
        val imageResId = intent.getIntExtra("albumImage", 0) // 기본값을 0으로 설정

        // 이미지 리소스 ID 검증 및 이미지 뷰 업데이트
        if (imageResId != 0) {
            binding.songAlbumIv.setImageResource(imageResId)
        } else {
            Log.e("SongActivity", "유효하지 않은 이미지 리소스 ID")
        }


        binding.songDownIb.setOnClickListener {

            // 인텐트에서 음악 제목 가져오기
            val songTitle = intent.getStringExtra("title") ?: "Unknown" // 기본값 설정

            // Intent 객체 생성 및 설정
            val returnIntent = Intent().apply {
                putExtra(MainActivity.STRING_INTENT_KEY, songTitle)
            }
            // 결과 설정 및 현재 액티비티 종료
            setResult(Activity.RESULT_OK, returnIntent)
            if (!isFinishing) finish()
        }
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(true)
        }

    }

    fun setPlayerStatus(isPlaying : Boolean){
        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility=View.GONE
        }
        else {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility=View.VISIBLE
        }
    }
}



