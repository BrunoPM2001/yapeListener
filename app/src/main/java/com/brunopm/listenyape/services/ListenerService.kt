package com.brunopm.listenyape.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class ListenerService : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("ListenerService", "Servicio de lectura de notificaciones conectado")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        if (!DaemonService.isRunning) {
            Log.d("ListenerService", "Daemon detenido, ignorando notificación")
            return
        }

        //  Notification data and regex
        val packageName = sbn.packageName
        val notificationTitle = sbn.notification.extras.getCharSequence("android.title")?.toString()
        val notificationText = sbn.notification.extras.getCharSequence("android.text")?.toString()
        val regex = Regex("""Yape!\s(.+?)\ste envió un pago por S/\s([\d.]+)""")

        if (packageName == "com.bcp.innovacxion.yapeapp" && notificationTitle == "Confirmación de Pago") {
            val matchResult = regex.find(notificationText.toString())
            if (matchResult != null) {
                val (nombre, monto) = matchResult.destructured
                Log.d("ListenerService", "Nombre: $nombre")
                Log.d("ListenerService", "Monto: S/ $monto")

                //  TODO - Enviar data a un backend - Hacer algo con ella
            } else {
                Log.d("ListenerService", "No se pudo extraer la información")
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
    }
}