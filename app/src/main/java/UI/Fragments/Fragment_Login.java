package UI.Fragments;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jmgavilan.organizacion_coordinacion.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import UI.Activities.HUDLogin;
import UI.Activities.HUDUserInterface;

public class Fragment_Login extends Fragment {
    // Declaración de atributos
    EditText ET_EMAIL, ET_PASS;
    String name, email, pass, apiKey;
    Button BTN_LOG;
    TextView TV_FORGET, TV_NEWUSER;
    final static String URL_LOCAL = "http://192.168.0.17:8080/orco_db";
    final static String ubicacion = "login";
    SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;

    public Fragment_Login() {
        // Constructor vacío requerido por Fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout del fragment y asignar las vistas a los atributos
        View vroot = inflater.inflate(R.layout.fragment__login, container, false);
        bindViews(vroot);
        // Inicializa Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Verificar si ya se ha iniciado sesión anteriormente
        //checkIfLoggedIn();

        // Configurar el botón de inicio de sesión para enviar una solicitud HTTP al servidor
        configureLoginButton(BTN_LOG, vroot);

        // Configurar los textos de "Nuevo usuario" y "Olvidé mi contraseña" para que cambien a la pantalla correspondiente cuando se les hace clic
        configureNewUserAndForgetPasswordTexts(vroot);

        return vroot;
    }

    // Método para asignar las vistas a los atributos correspondientes
    private void bindViews(View vroot) {
        ET_EMAIL = vroot.findViewById(R.id.FLET_Email);
        ET_PASS = vroot.findViewById(R.id.FLET_Password);
        BTN_LOG = vroot.findViewById(R.id.FLBTN_Login);
        TV_FORGET = vroot.findViewById(R.id.FLTV_ForgetPass);
        TV_NEWUSER = vroot.findViewById(R.id.FLTV_NewUser);
        sharedPreferences = requireActivity().getSharedPreferences("ORCO", Context.MODE_PRIVATE);
    }

    // Método para verificar si ya se ha iniciado sesión anteriormente y redirigir al usuario a la pantalla principal si es así
    private void checkIfLoggedIn() {
        if (sharedPreferences.getString("logged", "false").equals("false")) { //CAMBIAR A EQUALS TRUE TRAS METER EL LOGOUT
            Intent intent = new Intent(requireContext(), HUDUserInterface.class);
            startActivity(intent);
            requireActivity().finish();
        }
    }

    // Método para configurar el botón de inicio de sesión para que envíe una solicitud HTTP al servidor y maneje la respuesta
    private void configureLoginButton(Button btnLogin, View vroot) {
        btnLogin.setOnClickListener(v -> {
            // Comprobar campos de entrada
            if (!validateFields()) {
                return;
            }

            // Obtener los valores de los campos de entrada
            email = String.valueOf(ET_EMAIL.getText());
            pass = String.valueOf(ET_PASS.getText());

            RequestQueue queue = Volley.newRequestQueue(vroot.getContext());
            String urlregister = URL_LOCAL + "/login.php"; // Ctr+click te mete dentro

            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlregister,
                    success -> {
                        try {
                            JSONObject jsonObject = new JSONObject(success);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equals("success")) {
                                saveUserCredentials(jsonObject);
                            } else {
                                Toast.makeText(requireContext(), "Los datos introducidos no son correctos", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    failure -> {
                        Toast.makeText(requireContext(), "failure", Toast.LENGTH_SHORT).show();
                    }) {

                protected Map<String, String> getParams() {
                    // Insertar valores en el script
                    Map<String, String> paramV = new HashMap<>();
                    paramV.put("email", email);
                    paramV.put("pass", pass);
                    return paramV;
                }
            };

            queue.add(stringRequest);
        });
    }

    // Método para validar los campos de entrada
    private boolean validateFields() {
        String emailInput = ET_EMAIL.getText().toString().trim();
        String passInput = ET_PASS.getText().toString().trim();

        if (emailInput.isEmpty()) {
            ET_EMAIL.setError("Ingrese su correo electrónico");
            ET_EMAIL.requestFocus();
            return false;
        }

        if (passInput.isEmpty()) {
            ET_PASS.setError("Ingrese su contraseña");
            ET_PASS.requestFocus();
            return false;
        }

        return true;
    }

    // Método para guardar las credenciales del usuario en SharedPreferences y realizar el registro en googleAuthentication
    private void saveUserCredentials(JSONObject jsonObject) throws JSONException {

        email = jsonObject.getString("email");
        pass=jsonObject.getString("pass");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("logged", "true");
        editor.putString("email", email);
        editor.putString("pass", pass);

        System.out.println(email+pass+"555555555555555555555555555555555555555555555555");
        editor.apply();

        // Realizar registro en googleAuthentication
        boolean registrationSuccess = registerInGoogleAuthentication(email, pass);

        if (registrationSuccess) {
            // Registro exitoso, navegar a la siguiente actividad
            navigateToNextActivity();
        } else {
            // Registro fallido debido a que el usuario ya está registrado, continuar con el flujo
            continueWithFlow();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void reload() {
        // Obtiene la referencia a la actividad actual
        Activity currentActivity = requireActivity();

        // Verifica si la actividad actual no es nula
        if (currentActivity != null) {
            // Obtiene el intent de la actividad actual
            Intent intent = currentActivity.getIntent();

            // Finaliza y vuelve a iniciar la actividad actual
            currentActivity.finish();
            currentActivity.startActivity(intent);
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // El usuario está autenticado correctamente, realizar acciones en la interfaz de usuario
            // por ejemplo, navegar a la siguiente actividad o mostrar información del usuario
            navigateToNextActivity();
        } else {
            // El usuario no está autenticado, realizar acciones en la interfaz de usuario
            // por ejemplo, mostrar un mensaje de error o limpiar los campos de entrada
            Toast.makeText(requireContext(), "Error de autenticación", Toast.LENGTH_SHORT).show();
            ET_EMAIL.setText("");
            ET_PASS.setText("");
        }
    }

    // Método para realizar el registro en googleAuthentication
    private boolean registerInGoogleAuthentication(String email, String pass) {
        try {
            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(requireActivity(), task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(requireContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    });
        }catch (Exception e){

            Log.e(TAG, "Error al registrar en Google Authentication", e);

        }
        return true;
    }

    // Método para navegar a la siguiente actividad
    private void navigateToNextActivity() {
        Intent intent = new Intent(requireActivity(), HUDUserInterface.class);
        startActivity(intent);
        requireActivity().finish();
    }

    // Método para continuar con el flujo normal
    private void continueWithFlow() {
        Intent intent = new Intent(requireActivity(), HUDUserInterface.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void configureNewUserAndForgetPasswordTexts(View vroot) {
        TV_FORGET = vroot.findViewById(R.id.FLTV_ForgetPass);
        TV_NEWUSER = vroot.findViewById(R.id.FLTV_NewUser);

        TV_NEWUSER.setOnClickListener(v -> {
            HUDLogin activity = (HUDLogin) requireActivity();
            activity.replaceFragment(ubicacion, "register");
        });

        TV_FORGET.setOnClickListener(v -> {
            HUDLogin activity = (HUDLogin) requireActivity();
            activity.replaceFragment(ubicacion, "forget");
        });
    }
}
