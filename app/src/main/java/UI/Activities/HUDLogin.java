package UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.jmgavilan.organizacion_coordinacion.R;

import UI.Fragments.Fragment_ForgetPass;
import UI.Fragments.Fragment_Login;
import UI.Fragments.Fragment_Register;
import UI.data.BackgroundMusic;

public class HUDLogin extends AppCompatActivity {

    //Variable para saber si la actividad se creó
    public boolean isActivityCreated = false;
    //Transacción de fragmentos
    FragmentTransaction fragmentTransaction;
    //Fragmento actual
    Fragment fragment;
    //Administrador de fragmentos
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Establece el diseño para esta actividad
        setContentView(R.layout.activity_hudlogin);
        //Indica que la actividad se ha creado
        isActivityCreated=true;

        //Inicia el servicio de música en segundo plano
        Intent startIntent = new Intent(getApplicationContext(), BackgroundMusic.class);
        startService(startIntent);
        //Reproduce la música de fondo
      //  reproducir();
    }

    //Reproductor de música de fondo
    MediaPlayer mediaPlayer;
    private void reproducir() {
        if (mediaPlayer == null) {
            //Crea un objeto MediaPlayer con el archivo de música iwanttodie
            mediaPlayer = MediaPlayer.create(this, R.raw.iwanttodie);
            //Establece que la música no se repita
            mediaPlayer.setLooping(false);
            //Establece un Listener para detectar cuando la música ha terminado de reproducirse
            mediaPlayer.setOnCompletionListener(mp -> {
                //Libera el objeto MediaPlayer
                mediaPlayer.release();
                mediaPlayer = null;
            });
            //Inicia la reproducción de la música
            mediaPlayer.start();
        }
    }

    //Método para reemplazar el fragmento actual por otro
    public void replaceFragment(String procedencia,String destino){

        //Crea un nuevo fragmento basado en la variable destino
        switch (destino){
            case "login":
                fragment = new Fragment_Login();
                break;
            case "forget":
                fragment = new Fragment_ForgetPass();
                break;
            case "register":
                fragment = new Fragment_Register();
                break;
        }

        //Obtiene el administrador de fragmentos
        fragmentManager = getSupportFragmentManager();
        //Crea una nueva transacción de fragmentos
        fragmentTransaction = fragmentManager.beginTransaction();
        //Agrega la transacción actual a la pila de fragmentos
        fragmentTransaction.addToBackStack(null);
        //Reemplaza el fragmento actual con el nuevo fragmento
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        //Confirma la transacción
        fragmentTransaction.commit();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
