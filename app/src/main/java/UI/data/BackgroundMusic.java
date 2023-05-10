package UI.data;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.jmgavilan.organizacion_coordinacion.R;


public class BackgroundMusic extends Service {
    // MediaPlayer para reproducir la música
    private MediaPlayer mediaPlayer;
    // Variable para guardar la posición de la música cuando se detiene
    private int resumePosition;
    // BroadcastReceiver para detectar cuando la pantalla se enciende
    private BroadcastReceiver screenOnReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Este método se utiliza para comunicar el servicio con otras aplicaciones
        // En este caso, no se necesita, así que devuelve null
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Crea el MediaPlayer y asigna el archivo de música
        mediaPlayer = MediaPlayer.create(this, R.raw.iwanttodie);
        // Crea el BroadcastReceiver para detectar cuando la pantalla se enciende
        screenOnReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Comprueba si la pantalla se enciende
                // Comprueba si el intent recibido es un Intent de tipo ACTION_SCREEN_OFF
                if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    // Detiene la reproducción de la música
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                } else if( Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                        // Reanuda la reproducción de la música y avanza hasta la posición guardada
                        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                            mediaPlayer.seekTo(resumePosition);
                        }
                }
            }
        };
        // Registra el BroadcastReceiver para detectar cuando la pantalla se apaga
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenOnReceiver, filter);
        // Registra el BroadcastReceiver para detectar cuando la pantalla se enciende
        registerReceiver(screenOnReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Comprueba si el MediaPlayer está inicializado y no está reproduciendo la música
        if (mediaPlayer != null) {
            if (!mediaPlayer.isPlaying()) {
                // Inicia la reproducción de la música y avanza hasta la posición guardada
                mediaPlayer.start();
                mediaPlayer.seekTo(resumePosition);
            }
        }
        // Indica que el servicio debe seguir corriendo hasta que se detenga explícitamente
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Comprueba si el MediaPlayer está inicializado y está reproduciendo la música
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            // Guarda la posición actual de la música
            resumePosition = mediaPlayer.getCurrentPosition();
            // Detiene la reproducción de la música
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        // Desregistra el BroadcastReceiver para detectar cuando la pantalla se enciende
        unregisterReceiver(screenOnReceiver);
        super.onDestroy();
    }
}




