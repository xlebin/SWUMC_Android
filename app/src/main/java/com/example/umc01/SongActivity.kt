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

    //전역변수
    lateinit var binding: ActivitySongBinding
    lateinit var song : Song
    lateinit var timer : Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("SongActivity", "onCreate: Starting SongActivity")

        initSong()
        setPlayer(song)
        setPlayerStatus(song.isPlaying)  // Ensure initial player status is set correctly

//        if(intent.hasExtra("title") && intent.hasExtra("singer")&&intent.hasExtra("albumImage")&&intent.hasExtra("lyrics01")&&intent.hasExtra("lyrics02")){
//            binding.songMusicTitleTv.text = intent.getStringExtra("title")
//            binding.songSingerNameTv.text = intent.getStringExtra("singer")
//            binding.songMusicLyrics01Iv.text = intent.getStringExtra("lyrics01")
//            binding.songMusicLyrics02Iv.text = intent.getStringExtra("lyrics02")
//        }


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
            Log.d("SongActivity", "Pause button clicked")
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            Log.d("SongActivity", "Play button clicked")
            setPlayerStatus(false)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
    }

    private fun initSong(){
        if(intent.hasExtra("title") && intent.hasExtra("singer")) {
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("Playtime", 60),
                intent.getBooleanExtra("isPlaying", false)
            )
            Log.d("SongActivity", "Song initialized: ${song.title} by ${song.singer}")
        } else {
            Log.e("SongActivity", "Required song data not found in intent")
        }
        startTimer()
    }

    private fun setPlayer(song: Song){
        binding.songMusicTitleTv.text = intent.getStringExtra("title")!!
        binding.songSingerNameTv.text = intent.getStringExtra("singer")!!
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)

        // playTime이 0이 아닐 때만 진행률을 설정
        if (song.playTime > 0) {
            binding.songProgressSb.progress = (song.second * 1000 / song.playTime)
        } else {
            binding.songProgressSb.progress = 0
        }
        Log.d("SongActivity", "Player set with song data")
        setPlayerStatus(song.isPlaying)
    }

    fun setPlayerStatus(isPlaying : Boolean){

        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        Log.d("SongActivity", "Player status set to isPlaying: $isPlaying")

        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility=View.VISIBLE
        }
        else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility=View.GONE
        }
    }

    private fun startTimer() {
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
        Log.d("SongActivity", "Timer started with playTime: ${song.playTime} and isPlaying: ${song.isPlaying}")
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = false) : Thread() {
        private var second : Int = 0
        private var mills : Float = 0F

        override fun run() {
            super.run()

            try {
                while(true) {
                    if(second >= playTime) {
                        break
                    }

                    if(second >= 30){
                        break
                    }

                    while (!isPlaying) {
                        sleep(200) // 0.2초 대기
                    }

                    if(isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            // binding.songProgressSb.progress = ((mills/playTime*1000) * 100).toInt()
                            binding.songProgressSb.progress = ((mills/playTime) * 100).toInt()
                        }

                        if(mills % 1000 == 0F) { // 1초
                            runOnUiThread {
                                binding.songStartTimeTv.text = String.format("%02d:%02d", second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("SongActivity", "Thread Terminates! ${e.message}")
            }
        }
    }

}



