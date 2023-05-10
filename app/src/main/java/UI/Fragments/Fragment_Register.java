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
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vroot = inflater.inflate(R.layout.fragment__register, container, false);

        ET_NAME = vroot.findViewById(R.id.FRET_Name);
        ET_EMAIL = vroot.findViewById(R.id.FRET_Email);
        ET_PASS = vroot.findViewById(R.id.FRET_Password);
        ET_CONFIRM_PASS = vroot.findViewById(R.id.FRET_Confirm_Password);
        CB_TERM = vroot.findViewById(R.id.FRCB_Terminos);


        BTN_ACPT = vroot.findViewById(R.id.FRBTN_Login);
        BTN_ACPT.setOnClickListener(v -> {

            if (ET_PASS.getText().toString().equals(ET_CONFIRM_PASS.getText().toString())) {
                name = String.valueOf(ET_NAME.getText());
                email = String.valueOf(ET_EMAIL.getText());
                pass = String.valueOf(ET_PASS.getText());

                RequestQueue queue = Volley.newRequestQueue(vroot.getContext());
                String urlregister = URL_LOCAL + "/register.php"; //Ctr+click te mete dentro
                System.out.println(urlregister);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlregister,
                        success -> {
                            if (success.equals("success")) {

                                Toast.makeText(getActivity().getApplicationContext(), "Registro realizado", Toast.LENGTH_SHORT).show();
                                HUDLogin activity = (HUDLogin) getActivity();
                                assert activity != null;
                                activity.replaceFragment(ubicacion, "login");
                            } else {
                                System.err.println(success);
                            }
                        },
                        error -> {
                            Toast.makeText(getActivity().getApplicationContext(), "El registro falló", Toast.LENGTH_SHORT).show();
                        }) {

                    protected Map<String, String> getParams() {
                        //inserta cosas en el script
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("name", name);
                        paramV.put("email", email);
                        paramV.put("pass", pass);
                        return paramV;
                    }
                };

                queue.add(stringRequest);
            } else {
                //Las contraseñas no coinciden
                Toast.makeText(getActivity().getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
            //Toast de que bien y tal
            HUDLogin activity = (HUDLogin) getActivity();
            assert activity != null;
            activity.replaceFragment(ubicacion, "login");
        });

        TV_LOG = vroot.findViewById(R.id.FRTV_INICIO);
        TV_LOG.setOnClickListener(v -> {
            HUDLogin activity = (HUDLogin) getActivity();
            assert activity != null;
            activity.replaceFragment(ubicacion, "login");

        });

        return vroot;
    }


}