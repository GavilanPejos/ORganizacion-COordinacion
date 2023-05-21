package UI.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jmgavilan.organizacion_coordinacion.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class FragmentChat extends Fragment {

    private RecyclerView FCRV_Lista;
    private String email, name;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://organizacion-coordinacion-default-rtdb.europe-west1.firebasedatabase.app\n" +
            "\n");

    public FragmentChat() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vroot = inflater.inflate(R.layout.fragment_chat, container, false);
        final CircleImageView userImage = vroot.findViewById(R.id.FCIV_imgPerfil);
        FCRV_Lista = vroot.findViewById(R.id.FCRV_ListaUsuarios);


        FCRV_Lista.setHasFixedSize(true);
        FCRV_Lista.setLayoutManager(new LinearLayoutManager(vroot.getContext()));

        ProgressDialog progressDialog = new ProgressDialog(vroot.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Cargandito...");
        progressDialog.show();

        //pilla foto
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String imgUsuarioURL = snapshot.child("users").child(email).child("imgUsuario").getValue(String.class);

                Picasso.get().load(imgUsuarioURL).into(userImage);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });

        return vroot;
    }
}