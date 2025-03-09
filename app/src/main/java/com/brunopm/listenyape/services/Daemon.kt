package com.brunopm.listenyape.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class Daemon : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.d("Daemon service", "Servicio creado!")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("Daemon service", "Servicio iniciado")

        //  Describir la acción a ejecutar
        Thread {
            while (true) {
                Log.d("Daemon service","Servicio ejecutándose")
                Thread.sleep(5000)
            }
        }.start()

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Daemon service", "Servicio destruido!")
    }
}
