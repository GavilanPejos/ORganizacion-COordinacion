package UI.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jmgavilan.organizacion_coordinacion.R;

import java.util.HashMap;
import java.util.Map;

import UI.Activities.HUDLogin;
import UI.Activities.HUDUserInterface;

public class FragmentSettings extends Fragment {

    private final static String URL_LOCAL = "http://192.168.0.17:8080/orco_db";
    private SharedPreferences sharedPreferences, sharedPreferencesGroup;
    private String email;
    private String name, nombreGrupo;
    private String apiKey;

    public FragmentSettings() {
        // Constructor vacío requerido
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        sharedPreferences = requireActivity().getSharedPreferences("ORCO", Context.MODE_PRIVATE);
        sharedPreferencesGroup = requireActivity().getSharedPreferences("ORCOGroup", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesGroup.edit();

        email = sharedPreferences.getString("email", "");
        apiKey = sharedPreferences.getString("apiKey", "");
        name = sharedPreferences.getString("name", "");

        TextView TVUsername = rootView.findViewById(R.id.FSET_UserName);
        TVUsername.setText(name);
        TextView TVEmail = rootView.findViewById(R.id.FSET_Email);
        TVEmail.setText(email);
        Button logoutButton = rootView.findViewById(R.id.FSBTN_Logout);
        logoutButton.setOnClickListener(v -> logoutUsuario());
        TextView TVNombreGrupo = rootView.findViewById(R.id.FSET_Grupo);
        TVNombreGrupo.setText(sharedPreferencesGroup.getString("nameGrupo", nombreGrupo));
        return rootView;
    }

    private void logoutUsuario() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String urlLogout = URL_LOCAL + "/logout.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlLogout,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            // Cierre de sesión exitoso
                            Toast.makeText(getActivity().getApplicationContext(), "Cierre de sesión exitoso", Toast.LENGTH_SHORT).show();
                            limpiarSharedPreferences();
                            redireccionarInicioSesion();
                        } else {
                            Log.d("Overlord", apiKey + name + email);
                            // Error en el cierre de sesión
                            Toast.makeText(getActivity().getApplicationContext(), "Error en el cierre de sesión", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error en la solicitud de cierre de sesión
                        Toast.makeText(getActivity().getApplicationContext(), "Error en la solicitud de cierre de sesión", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Parámetros de la solicitud
                Map<String, String> params = new HashMap<>();
                params.put("email", email); // Agrega el parámetro de correo electrónico necesario para cerrar la sesión
                params.put("apiKey", apiKey); // Agrega el parámetro de apiKey necesario para cerrar la sesión
                return params;
            }
        };

        // Agrega la solicitud a la cola de solicitudes
        queue.add(stringRequest);
    }

    private void limpiarSharedPreferences() {
        // Limpia los valores almacenados en SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void redireccionarInicioSesion() {

        // Redirige al usuario a la pantalla de inicio de sesión (HUDLogin)
        Intent intent = new Intent(getActivity(), HUDLogin.class);
        startActivity(intent);
        requireActivity().finish();
    }
}
