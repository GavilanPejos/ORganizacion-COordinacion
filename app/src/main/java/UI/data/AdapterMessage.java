package UI.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jmgavilan.organizacion_coordinacion.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterMessage extends RecyclerView.Adapter<AdapterMessage.MyViewHolder> {


    private final List<ListMessage> mensajeslista;
    private final Context context;

    public AdapterMessage(List<ListMessage> mensajeslista, Context context) {
        this.mensajeslista = mensajeslista;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterMessage.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_messages_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMessage.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mensajeslista.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imagenPerfil;
        private TextView nombreUsuario, ultimoMensaje, numeroMensajessinver;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagenPerfil = itemView.findViewById(R.id.AMLIV_imagenUsuario);
            nombreUsuario = itemView.findViewById(R.id.AMLTV_NombreUsuario);
            ultimoMensaje = itemView.findViewById(R.id.AMLTV_UltimoMensaje);
            numeroMensajessinver = itemView.findViewById(R.id.AMLTV_NumMsjSinVer);


        }
    }
}
