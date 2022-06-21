package com.ltbth.foregroundservice

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity() {
    private var isPlay = false
    private lateinit var buttonStart: ImageView
    private lateinit var circleImageView: CircleImageView
    companion object {
        const val SONG = "object_song"
        private const val TITLE = "Sweet but psycho"
        private const val SINGER = "Bad Romance - Mashup || Ava Max X Lady Gaga"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStart = findViewById(R.id.btn_start_service)
        circleImageView = findViewById(R.id.song_circle_image)
        val song = Song(TITLE, SINGER, R.drawable.me, R.raw.sweet_but_psycho)
        buttonStart.setOnClickListener {
            val intent = Intent(this, PlayMusicService::class.java)
            if (isPlay) {
                isPlay = false
                buttonStart.setImageResource(R.drawable.ic_play)
                stopRotateImage()
                stopService(intent)
            } else {
                isPlay = true
                buttonStart.setImageResource(R.drawable.ic_pause)
                startRotateImage()
                val bundle = Bundle()
                bundle.putSerializable(SONG, song)
                intent.putExtras(bundle)
                startService(intent)
            }
        }
    }

    private fun startRotateImage() {
        val runnable = object: Runnable {
            override fun run() {
                circleImageView.animate()
                    .rotationBy(360F).withEndAction(this)
                    .setDuration(10000)
                    .setInterpolator(LinearInterpolator())
                    .start()
            }
        }
        circleImageView.animate()
            .rotationBy(360F).withEndAction(runnable)
            .setDuration(10000)
            .setInterpolator(LinearInterpolator())
            .start()
    }

    private fun stopRotateImage() {
        circleImageView.animate().cancel()
    }


    override fun onDestroy() {
        super.onDestroy()
//        val intent = Intent(this, PlayMusicService::class.java)
//        stopService(intent)
    }
}