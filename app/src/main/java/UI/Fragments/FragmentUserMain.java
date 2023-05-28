package UI.Fragments;

import static UI.Fragments.Fragment_Login.URL_LOCAL;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jmgavilan.organizacion_coordinacion.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import UI.data.Actividades;
import UI.data.AdapterActividades;


public class FragmentUserMain extends Fragment {

    String idGrupo;
    SharedPreferences sharedPreferencesActividades, sharedPreferencesGroup, sharedPreferences;
    String nombreEvento, nombreGrupo;
    TextView FUMnEv, FUMnGr;

    public FragmentUserMain() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vroot = inflater.inflate(R.layout.fragment_user_main, container, false);
        RecyclerView recyclerView = vroot.findViewById(R.id.FUM_recyclerView);
        FUMnEv = vroot.findViewById(R.id.FUM_NOMBREEVENTO);
        FUMnGr = vroot.findViewById(R.id.FUM_NOMBREGRUPO);


        sharedPreferences = getActivity().getSharedPreferences("ORCO", Context.MODE_PRIVATE);
        sharedPreferencesGroup = getActivity().getSharedPreferences("ORCOGroup", Context.MODE_PRIVATE);
        sharedPreferencesActividades = getActivity().getSharedPreferences("ORCOActividades", Context.MODE_PRIVATE);

        idGrupo = sharedPreferencesGroup.getString("idGrupo", idGrupo);

        recyclerView.setLayoutManager(new LinearLayoutManager(vroot.getContext()));
        recyclerView.setAdapter(new AdapterActividades(vroot.getContext().getApplicationContext(), new ArrayList<Actividades>()));

        loadActividades(); // Obtiene y actualiza las actividades de forma asíncrona

        return vroot;
    }

    private void loadActividades() {
        getActividades(); // Llama al método para obtener las actividades de forma asíncrona
        // El resto de la configuración del RecyclerView y la asignación del adaptador
        // se deben mover al callback de éxito de getActividades()
    }

    public void getActividades() {
        String urlgetloginGroup = URL_LOCAL + "/loginActividades.php";
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        List<Actividades> actividades = new ArrayList<Actividades>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlgetloginGroup,
                success -> {
                    try {
                        JSONObject jsonObject = new JSONObject(success);
                        String status = jsonObject.getString("status");

                        if (status.equals("success")) {

                            JSONArray activitiesArray = jsonObject.getJSONArray("activities");
                            for (int i = 0; i < activitiesArray.length(); i++) {
                                JSONObject ActividadesJSON = activitiesArray.getJSONObject(i);

                                Actividades actividad = saveOurActividades(ActividadesJSON);
                                actividades.add(actividad);
                            }

                            if (activitiesArray.length() > 0) {
                                // El RecyclerView se actualiza con las actividades aquí
                                RecyclerView recyclerView = getView().findViewById(R.id.FUM_recyclerView);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(new AdapterActividades(getContext().getApplicationContext(), actividades));
                            } else {
                                Toast.makeText(requireContext(), "No se encontraron actividades para el grupo proporcionado", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(requireContext(), "Grupo no encontrado", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                failure -> {
                    Toast.makeText(requireContext(), "failure", Toast.LENGTH_SHORT).show();
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                paramV.put("idGrupo", idGrupo);
                return paramV;
            }
        };

        queue.add(stringRequest);
    }

    public Actividades saveOurActividades(JSONObject jsonObject) throws JSONException {
        String nombreActividad = jsonObject.getString("nombreActividad");
        String descripcionActividad = jsonObject.getString("descripcionActividad");
        String fechaInicio = jsonObject.getString("fechaInicioActividad");
        String fechaFinal = jsonObject.getString("fechaFinActividad");
        String nameGrupo = jsonObject.getString("nameGrupo");
        String nameEvento = jsonObject.getString("nameEvento");
        String trabajadores = jsonObject.getString("usuarios");

        Actividades actividad = new Actividades();
        actividad.setNombreActividad(nombreActividad);
        actividad.setDescripcionActividad(descripcionActividad);
        actividad.setFechaInicio(fechaInicio);
        actividad.setFechaFinal(fechaFinal);
        actividad.setNameEvento(nameEvento);
        actividad.setNameGrupo(nameGrupo);
        actividad.setTrabajadores(trabajadores);
        FUMnEv.setText(actividad.getNameEvento());
        FUMnGr.setText(actividad.getNameGrupo());
        Log.d("OVERLORDchiquito", actividad.toString());

        return actividad;
    }
}
