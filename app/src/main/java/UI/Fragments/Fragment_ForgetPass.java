package UI.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jmgavilan.organizacion_coordinacion.R;

import UI.Activities.HUDLogin;

public class Fragment_ForgetPass extends Fragment {

    EditText ET_EMAIL;
    Button BTN_ACPT;
    TextView TV_INICIO;

    final static String ubicacion="forget";

    public Fragment_ForgetPass() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vroot = inflater.inflate(R.layout.fragment__forget_pass, container, false);


        ET_EMAIL = vroot.findViewById(R.id.FFPET_Email);
        BTN_ACPT = vroot.findViewById(R.id.FFPBTN_Login);
        TV_INICIO = vroot.findViewById(R.id.FFPTV_INICIO);
        TV_INICIO.setOnClickListener(v -> {
            HUDLogin activity = (HUDLogin) getActivity();
            assert activity != null;
            activity.replaceFragment(ubicacion,"login");


        });
        return  vroot;
    }
}