package UI.data;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jmgavilan.organizacion_coordinacion.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterFragmentChat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> userList; // Lista de usuarios

    private static int VIEW_TYPE_USER = 1; // Tipo de vista para el usuario


    public AdapterFragmentChat(List<User> userList, List<Message> messageList) {
        this.userList = userList;
    }

    // Método para crear el ViewHolder según el tipo de vista
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_USER) {
            // Inflar la vista de usuario
            view = inflarVistaUsuario(parent);
            return new UserViewHolder(view);
        }
        throw new IllegalArgumentException("Tipo de vista inválido: " + viewType);
    }

    // Método para asignar los datos a un ViewHolder según su posición
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder instanceof UserViewHolder) {
            // Asignar datos de usuario a la vista
            bindUsuario((UserViewHolder) holder, position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        User user = userList.get(position);
                        onItemClickListener.onItemClick(user);
                    }
                }
            });
        }
    }

    // Método para obtener el número total de elementos en el adaptador
    @Override
    public int getItemCount() {
        Log.d("OVERLORDCHAD", "getItemCount: "+userList.size());
        return userList.size();
    }

    // Método para obtener el tipo de vista según la posición
    @Override
    public int getItemViewType(int position) {

        return VIEW_TYPE_USER;
    }


    // Método para inflar la vista de usuario
    private View inflarVistaUsuario(ViewGroup parent) {

        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_messages_layout, parent, false);
    }


    // Método para asignar los datos de usuario a la vista
    private void bindUsuario(UserViewHolder userViewHolder, int position) {
        if (position < userList.size()) {
            User elemento = userList.get(position);
            if (elemento != null) {
                userViewHolder.username.setText(elemento.getEmail());
                Picasso.get().load(elemento.getImageUrl()).into(userViewHolder.profileImage);
                Log.d("AFCA", elemento.getEmail());
            }
        } else {
            // Mostrar el nuevo registro de usuario en un nuevo item
            User nuevoUsuario = userList.get(position); // Obtén el nuevo usuario de la lista
            // Inflar la vista de usuario para el nuevo item
            View nuevoItemView = inflarVistaUsuario((ViewGroup) userViewHolder.itemView.getParent());
            UserViewHolder nuevoUserViewHolder = new UserViewHolder(nuevoItemView);
            // Asignar datos de usuario a la vista del nuevo item
            bindUsuario(nuevoUserViewHolder, position);
            // Agregar el nuevo item al RecyclerView
            ((ViewGroup) userViewHolder.itemView.getParent()).addView(nuevoItemView);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    // Clase ViewHolder para los elementos de usuario
    public static class UserViewHolder extends RecyclerView.ViewHolder {

        public ImageView profileImage; // Imagen del perfil del usuario
        public TextView username; // Nombre de usuario

        public UserViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.AMLIV_imagenUsuario);
            username = itemView.findViewById(R.id.AMLTV_NombreUsuario);
        }
    }

}
