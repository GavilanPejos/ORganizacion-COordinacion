package UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jmgavilan.organizacion_coordinacion.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import UI.Fragments.FragmentChat;
import UI.Fragments.FragmentGPS;
import UI.Fragments.FragmentSettings;
import UI.Fragments.FragmentUserMain;

public class HUDUserInterface extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FirebaseAuth firebaseAuth;
    String idGrupo;
    SharedPreferences sharedPreferences, sharedPreferencesGroup;
    final static String URL_LOCAL = "http://192.168.0.17:8080/orco_db";

    // Fragmentos iniciando
    FragmentUserMain fragmentUserMain = new FragmentUserMain();
    FragmentChat fragmentChat = new FragmentChat();
    FragmentGPS fragmentGPS = new FragmentGPS();
    FragmentSettings fragmentSettings = new FragmentSettings();

   public  String nombreGrupo, email, name, actividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hud_ui);

        bottomNavigationView = findViewById(R.id.HUBN_MENU);

        getSupportFragmentManager().beginTransaction().replace(R.id.HUFL_CONTAINER, fragmentUserMain).commit();
        getGrupo();
        // Obtener el SharedPreferences "UserDataXML"
        sharedPreferences = getSharedPreferences("ORCO", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("name", name);
        email = sharedPreferences.getString("email", email);

        sharedPreferencesGroup = getSharedPreferences("ORCOGroup", Context.MODE_PRIVATE);

        bottomNavigationView.setOnItemSelectedListener(item -> {
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
        });
    }

    private void cargarFragmento(Fragment fragmento) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.HUFL_CONTAINER, fragmento);
        transaction.commit();
    }

    private void getGrupo() {
        String urlgetloginGroup = URL_LOCAL + "/loginGroup.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlgetloginGroup,
                success -> {
                    try {
                        JSONObject jsonObject = new JSONObject(success);
                        String status = jsonObject.getString("status");

                        if (status.equals("success")) {
                            saveGroupCredentials(jsonObject);
                        } else {

                            Toast.makeText(this, "Grupo no encontrado", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                failure -> {
                    Toast.makeText(this, "failure", Toast.LENGTH_SHORT).show();
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                paramV.put("email", email);
                return paramV;
            }
        };

        queue.add(stringRequest);
    }

    private void saveGroupCredentials(JSONObject jsonObject) throws JSONException {
        idGrupo=jsonObject.getString("idGrupo");
        nombreGrupo = jsonObject.getString("nameGrupo");
        SharedPreferences.Editor editor = sharedPreferencesGroup.edit();
        editor.putString("nameGrupo", nombreGrupo);
        editor.putString("idGrupo",idGrupo);
        editor.apply();
    }

}