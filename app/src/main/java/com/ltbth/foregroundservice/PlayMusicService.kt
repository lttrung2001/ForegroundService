package com.ltbth.foregroundservice

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat

class PlayMusicService : Service() {
    private lateinit var mediaPlayer: MediaPlayer
    companion object {
        const val CHANNEL_ID = "Play song channel"
        const val CHANNEL_NAME = "Play song channel"
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val bundle = intent!!.extras
        val song = bundle!!.getSerializable(MainActivity.SONG) as Song
        sendNotification(song)
        mediaPlayer = MediaPlayer.create(applicationContext,song.resource)
        mediaPlayer.start()
        return START_NOT_STICKY
    }

    private fun sendNotification(song: Song) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val remoteViews = RemoteViews(packageName,R.layout.custom_notification)
        val bitmap = BitmapFactory.decodeResource(resources,song.image)
        remoteViews.setTextViewText(R.id.song_title, song.title)
        remoteViews.setTextViewText(R.id.song_single, song.single)
        remoteViews.setImageViewBitmap(R.id.song_img, bitmap)

        val notification = NotificationCompat.Builder(this,CHANNEL_ID)
            .setSmallIcon(song.image)
            .setContentIntent(pendingIntent)
            .setCustomContentView(remoteViews)
            .setSound(null)
            .build()
        startForeground(1,notification)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_NAME
            val descriptionText = CHANNEL_NAME
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                setSound(null,null)
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}