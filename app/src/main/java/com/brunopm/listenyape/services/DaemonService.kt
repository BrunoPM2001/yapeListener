package com.brunopm.listenyape.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.brunopm.listenyape.R

class DaemonService : Service() {

    companion object {
        var isRunning = false
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }
        return START_STICKY
    }

    private fun start() {
        if (isRunning) return

        isRunning = true
        val notification = NotificationCompat.Builder(this, "daemon_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Servicio activo")
            .setContentText("Esperando notificaciones de yape...")
            .build()

        startForeground(1, notification)

        //  Correr listener
        Intent(applicationContext, ListenerService::class.java).also {
            startService(it)
        }
        Log.d("DaemonService", "En ejecución")
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false

        Log.d("DaemonService", "Servicio detenido")
    }

    enum class Actions {
        START, STOP
    }
}
