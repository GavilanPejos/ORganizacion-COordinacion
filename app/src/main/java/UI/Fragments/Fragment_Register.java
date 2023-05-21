package UI.Fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import java.util.Objects;
import UI.Activities.HUDLogin;

public class Fragment_Register extends Fragment {
    EditText ET_EMAIL, ET_PASS, ET_CONFIRM_PASS, ET_NAME;
    String email, pass, name;
    Button BTN_ACPT;
    CheckBox CB_TERM;
    TextView TV_LOG;
    final static String ubicacion = "register";
    final static String URL_LOCAL = "http://192.168.0.17:8080/orco_db";

    public Fragment_Register() {
        // Constructor vacío requerido
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View vroot = inflater.inflate(R.layout.fragment__register, container, false);

        // Enlazar vistas con variables
        ET_NAME = vroot.findViewById(R.id.FRET_Name);
        ET_EMAIL = vroot.findViewById(R.id.FRET_Email);
        ET_PASS = vroot.findViewById(R.id.FRET_Password);
        ET_CONFIRM_PASS = vroot.findViewById(R.id.FRET_Confirm_Password);
        CB_TERM = vroot.findViewById(R.id.FRCB_Terminos);
        BTN_ACPT = vroot.findViewById(R.id.FRBTN_Login);
        TV_LOG = vroot.findViewById(R.id.FRTV_INICIO);

        // Configurar click listener para el botón de aceptar
        BTN_ACPT.setOnClickListener(v -> {
            if (!validateFields()) {
                return;
            }

            // Realizar la solicitud de registro
            registerUser();

            // Navegar de vuelta a la pantalla de inicio de sesión
            navigateToLogin();
        });

        // Configurar click listener para el texto de inicio de sesión
        TV_LOG.setOnClickListener(v -> {
            // Navegar a la pantalla de inicio de sesión
            navigateToLogin();
        });

        return vroot;
    }

    // Método para realizar todas las comprobaciones de campos
    private boolean validateFields() {
        if (!CB_TERM.isChecked()) {
            // El checkbox de términos y condiciones no ha sido marcado
            Toast.makeText(getActivity().getApplicationContext(), "Debe aceptar los términos y condiciones", Toast.LENGTH_SHORT).show();
            return false;
        }

        name = ET_NAME.getText().toString().trim();
        email = ET_EMAIL.getText().toString().trim();
        pass = ET_PASS.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            // Uno o más campos están vacíos
            Toast.makeText(getActivity().getApplicationContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidEmail(email)) {
            // Contraseña inválida
            Toast.makeText(getActivity().getApplicationContext(), "La contraseña debe contener un '@' y al menos un '.'", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (pass.length() <= 5) {
            // Contraseña demasiado corta
            Toast.makeText(getActivity().getApplicationContext(), "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!pass.equals(ET_CONFIRM_PASS.getText().toString().trim())) {
            // Las contraseñas no coinciden
            Toast.makeText(getActivity().getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Método para realizar la solicitud de registro del usuario
    private void registerUser() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String urlregister = URL_LOCAL + "/register.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlregister,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String success) {
                        if (success.equals("success")) {
                            // Registro exitoso
                            Toast.makeText(getActivity().getApplicationContext(), "Registro realizado", Toast.LENGTH_SHORT).show();

                            // Reemplazar el fragmento actual con la pantalla de inicio de sesión
                            HUDLogin activity = (HUDLogin) getActivity();
                            assert activity != null;
                            activity.replaceFragment(ubicacion, "login");
                        } else {
                            System.err.println(success);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error en la solicitud de registro
                        Toast.makeText(getActivity().getApplicationContext(), "El registro falló", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Parámetros de la solicitud
                Map<String, String> paramV = new HashMap<>();
                paramV.put("name", name);
                paramV.put("email", email);
                paramV.put("pass", pass);
                return paramV;
            }
        };

        queue.add(stringRequest);
    }

    // Método para navegar a la pantalla de inicio de sesión
    private void navigateToLogin() {
        HUDLogin activity = (HUDLogin) getActivity();
        assert activity != null;
        activity.replaceFragment(ubicacion, "login");
    }

    // Método para verificar si el campo de contraseña contiene un "@" y al menos un "."
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}
