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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jmgavilan.organizacion_coordinacion.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import UI.Activities.HUDLogin;
import UI.Activities.HUDUserInterface;

public class Fragment_Login extends Fragment {
//Declaración de atributos
    EditText ET_EMAIL, ET_PASS;
    String name, email, pass, apiKey;
    Button BTN_LOG;
    TextView TV_FORGET, TV_NEWUSER;
    final static String URL_LOCAL = "http://192.168.0.17:8080/orco_db";

    final static String ubicacion = "login";
    SharedPreferences sharedPreferences;


    public Fragment_Login() {
        // Constructor vacío requerido por Fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout del fragment y asignar las vistas a los atributos
        View vroot = inflater.inflate(R.layout.fragment__login, container, false);
        bindViews(vroot);

        // Verificar si ya se ha iniciado sesión anteriormente
        checkIfLoggedIn();

        // Configurar el botón de inicio de sesión para enviar una solicitud HTTP al servidor
        configureLoginButton(BTN_LOG,vroot);

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
        sharedPreferences = getActivity().getSharedPreferences("ORCO", Context.MODE_PRIVATE);
    }

    // Método para verificar si ya se ha iniciado sesión anteriormente y redirigir al usuario a la pantalla principal si es así
    private void checkIfLoggedIn() {
        if (sharedPreferences.getString("logged", "false").equals("false")) { //CAMBIAR A EQUALS TRUE TRAS METER EL LOGOUT
            Intent intent = new Intent(getActivity().getApplicationContext(), HUDUserInterface.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    // Método para configurar el botón de inicio de sesión para que envíe una solicitud HTTP al servidor y maneje la respuesta
    private void configureLoginButton(Button btnLogin, View vroot) {
        btnLogin.setOnClickListener(v -> {
            //Comprobar ET y mandar a el HUD principal
            email = String.valueOf(ET_EMAIL.getText());
            pass = String.valueOf(ET_PASS.getText());

            RequestQueue queue = Volley.newRequestQueue(vroot.getContext());
            String urlregister = URL_LOCAL + "/login.php"; //Ctr+click te mete dentro

            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlregister,
                    success -> {
                        try {
                            JSONObject jsonObject = new JSONObject(success);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equals("success")) {
                                name = jsonObject.getString("name");
                                email = jsonObject.getString("email");
                                apiKey = jsonObject.getString("apiKey");
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("logged", "true");
                                editor.putString("name", name);
                                editor.putString("email", email);
                                editor.putString("apiKey", apiKey);
                                editor.apply();
                                Intent intent = new Intent(getActivity().getApplicationContext(), HUDUserInterface.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    failure -> {
                        Toast.makeText(getActivity().getApplicationContext(), "failure", Toast.LENGTH_SHORT).show();
                    }) {

                protected Map<String, String> getParams() {
                    //inserta cosas en el script
                    Map<String, String> paramV = new HashMap<>();
                    paramV.put("email", email);
                    paramV.put("pass", pass);
                    return paramV;
                }
            };

            queue.add(stringRequest);

            //muverse
        });
    }

    private void configureNewUserAndForgetPasswordTexts(View vroot) {
        TV_FORGET = vroot.findViewById(R.id.FLTV_ForgetPass);
        TV_NEWUSER = vroot.findViewById(R.id.FLTV_NewUser);

        TV_NEWUSER.setOnClickListener(v -> {
            HUDLogin activity = (HUDLogin) getActivity();
            assert activity != null;
            activity.replaceFragment(ubicacion, "register");
        });

        TV_FORGET.setOnClickListener(v -> {
            HUDLogin activity = (HUDLogin) getActivity();
            assert activity != null;
            activity.replaceFragment(ubicacion, "forget");
        });
    }
}



    /*

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vroot = inflater.inflate(R.layout.fragment__login, container, false);

        ET_EMAIL = vroot.findViewById(R.id.FLET_Email);
        ET_PASS = vroot.findViewById(R.id.FLET_Password);
        BTN_LOG = vroot.findViewById(R.id.FLBTN_Login);
        TV_FORGET = vroot.findViewById(R.id.FLTV_ForgetPass);
        TV_NEWUSER = vroot.findViewById(R.id.FLTV_NewUser);
        sharedPreferences= getActivity().getSharedPreferences("ORCO", Context.MODE_PRIVATE);

        if(sharedPreferences.getString("logged","false").equals("false")){ //CAMBIAR A EQUALS TRUE TRAS METER EL LOGOUT
            Intent intent = new Intent(getActivity().getApplicationContext(),HUDUserInterface.class);
            startActivity(intent);
            getActivity().finish();
        }

        BTN_LOG.setOnClickListener(v -> {
//Comprobar ET y mandar a el HUD principal
            email = String.valueOf(ET_EMAIL.getText());
            pass = String.valueOf(ET_PASS.getText());

            RequestQueue queue = Volley.newRequestQueue(vroot.getContext());
            String urlregister = URL_LOCAL + "/login.php"; //Ctr+click te mete dentro

            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlregister,
                    success -> {
                        try {
                            JSONObject jsonObject = new JSONObject(success);
                            String status = jsonObject.getString("status");
                            String message =jsonObject.getString("message");
                            if(status.equals("success")){
                                name = jsonObject.getString("name");
                                email = jsonObject.getString("email");
                                apiKey = jsonObject.getString("apiKey");
                                SharedPreferences.Editor editor =sharedPreferences.edit();
                                editor.putString("logged","true");
                                editor.putString("name",name);
                                editor.putString("email",email);
                                editor.putString("apiKey",apiKey);
                                editor.apply();
                                Intent intent = new Intent(getActivity().getApplicationContext(),HUDUserInterface.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    failure -> {
                        Toast.makeText(getActivity().getApplicationContext(), "failure", Toast.LENGTH_SHORT).show();
                    }) {

                protected Map<String, String> getParams() {
                    //inserta cosas en el script
                    Map<String, String> paramV = new HashMap<>();
                    paramV.put("email", email);
                    paramV.put("pass", pass);
                    return paramV;
                }
            };

            queue.add(stringRequest);

//muverse

        });
        TV_NEWUSER.setOnClickListener(v -> {
            HUDLogin activity = (HUDLogin) getActivity();
            assert activity != null;
            activity.replaceFragment(ubicacion, "register");

        });
        TV_FORGET.setOnClickListener(v -> {
            HUDLogin activity = (HUDLogin) getActivity();
            assert activity != null;
            activity.replaceFragment(ubicacion, "forget");
        });
        return vroot;
    }
}*/