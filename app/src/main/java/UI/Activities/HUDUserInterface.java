package UI.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jmgavilan.organizacion_coordinacion.R;

import UI.Fragments.FragmentChat;
import UI.Fragments.FragmentGPS;
import UI.Fragments.FragmentSettings;
import UI.Fragments.FragmentUserMain;

public class HUDUserInterface extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;

    // Fragmentos iniciando
    FragmentUserMain fragmentUserMain = new FragmentUserMain();
    FragmentChat fragmentChat = new FragmentChat();
    FragmentGPS fragmentGPS = new FragmentGPS();
    FragmentSettings fragmentSettings = new FragmentSettings();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hud_ui);

        // Inicializar Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.HUBN_MENU);

        getSupportFragmentManager().beginTransaction().replace(R.id.HUFL_CONTAINER, fragmentUserMain).commit();

        // Obtener el SharedPreferences "UserDataXML"
        sharedPreferences = getSharedPreferences("UserDataXML", Context.MODE_PRIVATE);

        // Autenticar al usuario
        String email = sharedPreferences.getString("email", "");
        String contraseña = sharedPreferences.getString("password", "");
      //  autenticarUsuario(email, contraseña);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.MENUBTN_CUENTA) {
                    cargarFragmento(fragmentUserMain);
                } else if (menuItemId == R.id.MENUBTN_CHAT) {
                    cargarFragmento(fragmentChat);
                } else if (menuItemId == R.id.MENUBTN_GPS) {
                    cargarFragmento(fragmentGPS);
                } else if (menuItemId == R.id.MENUBTN_INFO) {
                    cargarFragmento(fragmentSettings);
                }

                return false;
            }
        });
    }

    private void cargarFragmento(Fragment fragmento) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.HUFL_CONTAINER, fragmento);
        transaction.commit();
    }

    private void autenticarUsuario(String email, String contraseña) {
        firebaseAuth.signInWithEmailAndPassword(email, contraseña)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // El inicio de sesión en Firebase fue exitoso
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        System.out.println("LOGIN EXITOSO");
                        // Realizar acciones adicionales después de iniciar sesión correctamente
                    } else {
                        // El inicio de sesión en Firebase falló
                        // Manejar el fallo del inicio de sesión
                    }
                });
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
