package com.brunopm.listenyape

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.brunopm.listenyape.services.DaemonService
import com.brunopm.listenyape.ui.theme.ListenYapeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        enableEdgeToEdge()
        setContent {
            ListenYapeTheme {
                Column (
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { iniciarServicioConPermiso() }) {
                        Text(text = "Iniciar servicio")
                    }
                    Button(onClick = { pausarServicio() }) {
                        Text(text = "Pausar servicio")
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        verificarPermisoNotificaciones()
    }

    // Verificar y solicitar el permiso de notificaciones
    private fun verificarPermisoNotificaciones() {
        if (NotificationManagerCompat.getEnabledListenerPackages(this).contains(packageName)) {
            Log.d("ListenerService", "Permiso concedido")
        } else {
            Log.d("ListenerService", "Permiso no concedido")
            mostrarDialogoPermiso()
        }
    }

    // Mostrar diálogo solo si el permiso no está concedido
    private fun mostrarDialogoPermiso() {
        AlertDialog.Builder(this)
            .setTitle("Permiso necesario")
            .setMessage("Para que la aplicación funcione correctamente, necesitamos acceso a las notificaciones. Activa el permiso en la siguiente pantalla.")
            .setPositiveButton("Permitir") { _, _ ->
                val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                startActivity(intent)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // Iniciar el servicio de daemon solo si el permiso está activo
    private fun iniciarServicioConPermiso() {
        if (NotificationManagerCompat.getEnabledListenerPackages(this).contains(packageName)) {
            Log.d("ListenerService", "Iniciando servicio...")
            Intent(this, DaemonService::class.java).also {
                it.action = DaemonService.Actions.START.toString()
                startService(it)
            }
        } else {
            mostrarDialogoPermiso()
        }
    }

    // Detener el servicio de daemon
    private fun pausarServicio() {
        Log.d("ListenerService", "Pausando servicio...")
        Intent(this, DaemonService::class.java).also {
            it.action = DaemonService.Actions.STOP.toString()
            startService(it)
        }
    }
}