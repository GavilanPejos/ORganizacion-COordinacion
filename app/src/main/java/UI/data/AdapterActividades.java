package UI.data;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jmgavilan.organizacion_coordinacion.R;

import java.util.List;

public class AdapterActividades extends RecyclerView.Adapter<ViewHolderActividades> {

    Context context;
    List<Actividades> actividades;

    public AdapterActividades(Context context, List<Actividades> actividades) {
        this.context = context;
        this.actividades = actividades;
    }

    @NonNull
    @Override
    public ViewHolderActividades onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderActividades(LayoutInflater.from(context).inflate(R.layout.item_view_actividades, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderActividades holder, int position) {
        holder.nombreActividad.setText(actividades.get(position).nombreActividad);
        holder.nombreTrabajador.setText(actividades.get(position).trabajadores);
        holder.fechaInicio.setText(actividades.get(position).fechaInicio);
        holder.fechaFin.setText(actividades.get(position).fechaFinal);

        holder.nombreActividad.setMinLines(2);
        holder.fechaInicio.setMinLines(2);
        holder.fechaFin.setMinLines(2);
    }


    @Override
    public int getItemCount() {
        Log.d("OVERLORD", "getItemCount: "+actividades.size());
        return actividades.size();
    }
}
