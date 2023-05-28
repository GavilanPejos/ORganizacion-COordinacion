package UI.data;

import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jmgavilan.organizacion_coordinacion.R;

public class ViewHolderActividades extends RecyclerView.ViewHolder {

    TextView nombreActividad;
    TextView nombreTrabajador;
    TextView fechaInicio;
    TextView fechaFin;
    LinearLayout grid;


    public ViewHolderActividades( View itemView) {
        super(itemView);

        nombreActividad=itemView.findViewById(R.id.IVA_NombreActividad);
        nombreTrabajador=itemView.findViewById(R.id.IVA_NombreTrabajador);
        fechaInicio=itemView.findViewById(R.id.IVA_FechaInicio);
        fechaFin=itemView.findViewById(R.id.IVA_FechaFin);
    }
}
