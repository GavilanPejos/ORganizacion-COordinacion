package UI.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jmgavilan.organizacion_coordinacion.R;

import java.util.ArrayList;
import java.util.List;

import UI.Activities.ActivityChat;
import UI.data.AdapterFragmentChat;
import UI.data.Message;
import UI.data.User;

public class FragmentChat extends Fragment {
    String idgrupo;
    private RecyclerView recyclerView;
    private AdapterFragmentChat adapterFragmentChat;
    private List<User> userList;
    private List<Message> messageList;

    public FragmentChat() {
        // Constructor público vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.FCRV_ListaUsuarios);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();
        messageList = new ArrayList<>();

        // Supongamos que tienes el groupId del usuario actual almacenado en una variable llamada "groupId"
        String email = obtenerEmailUsuarioActual();
        insertarUsuarioActual(email);
        // Filtra la lista de usuarios según el groupId
        obtenerGroupIdUsuario(email);

        adapterFragmentChat = new AdapterFragmentChat(userList, messageList);
        adapterFragmentChat.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(adapterFragmentChat);

        return view;
    }

    private void insertarUsuarioActual(String email) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        Query query = usersRef.orderByChild("email").equalTo(email);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String userId = snapshot.child("userId").getValue(String.class);
                        String groupId = snapshot.child("groupId").getValue(String.class);
                        String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                        String name = snapshot.child("name").getValue(String.class);

                        // Crea un objeto User y establece los valores obtenidos
                        User user = new User();
                        user.setId(userId);
                        user.setEmail(email);
                        user.setIdGroup(groupId);
                        user.setImageUrl(imageUrl);
                        user.setUsername(name);
                        idgrupo = user.getIdGroup();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error en caso de que la consulta sea cancelada
            }
        });
    }

    private String obtenerEmailUsuarioActual() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            return firebaseUser.getEmail();
        } else {
            return null;
        }
    }

    private void obtenerGroupIdUsuario(String email) {
        if (email != null) {

            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
            Query query = usersRef.orderByChild("email").equalTo(email);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            if (user != null) {
                                String groupId = user.getIdGroup();

                                // Aquí puedes usar el groupId obtenido para filtrar la lista de usuarios
                                filtrarListaUsuariosPorGrupo(idgrupo);

                                break; // Solo necesitamos el primer usuario que coincida con el email
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejar el error en caso de que la consulta sea cancelada
                }
            });
        } else {
            // No se pudo obtener el email del usuario actual
            // Manejar el caso según tus necesidades
        }
    }

    // Método para filtrar la lista de usuarios por groupId
    private void filtrarListaUsuariosPorGrupo(String groupId) {

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        Query query = usersRef.orderByChild("groupId").equalTo(String.valueOf(groupId));

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<User> listaUsuariosFiltrada = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    listaUsuariosFiltrada.add(user);
                    Log.d("FragmentChatLista",listaUsuariosFiltrada.toString());
                    // Obtener la lista de mensajes para cada usuario
                    obtenerMensajesParaUsuario(user.getId(), listaUsuariosFiltrada);
                }


                userList.addAll(listaUsuariosFiltrada);
                adapterFragmentChat.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error en caso de que la consulta sea cancelada
            }
        });
    }

    // Método para obtener la lista de mensajes para un usuario
    private void obtenerMensajesParaUsuario(String userId, List<User> listaUsuariosFiltrada) {


        if (userId != null) {
            DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages");
            Query messagesQuery = messagesRef.child(userId);

            messagesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Message> listaMensajesUsuario = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Obtener los datos de cada mensaje
                        String messageId = snapshot.getKey();
                        String messageText = snapshot.child("message").getValue(String.class);

                        Message message = new Message();
                        message.setSenderId(messageId);
                        message.setMessage(messageText);
                        listaMensajesUsuario.add(message);
                    }

                    // Agregar la lista de mensajes al usuario correspondiente
                    for (User user : listaUsuariosFiltrada) {
                        Log.d("FragmentChatCicloUser",user.getUsername());
                        if (user.getId().equals(userId)) {
                            user.setMessageList(listaMensajesUsuario);
                            adapterFragmentChat.notifyDataSetChanged();
                            break;
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejar el error en caso de que la consulta sea cancelada
                }
            });

            // Notificar cambios en el adaptador después de agregar los mensajes a los usuarios correspondientes
            adapterFragmentChat.notifyDataSetChanged();
        }
    }
    private AdapterFragmentChat.OnItemClickListener onItemClickListener = new AdapterFragmentChat.OnItemClickListener() {
        @Override
        public void onItemClick(User user) {
            // Aquí puedes iniciar un nuevo Activity y pasar los datos del usuario seleccionado
            Intent intent = new Intent(getContext(), ActivityChat.class);
            intent.putExtra("email", user.getEmail());
            intent.putExtra("imgUrl", user.getImageUrl());
            // Agrega otros datos que necesites pasar al nuevo Activity
            startActivity(intent);
        }
    };
}
