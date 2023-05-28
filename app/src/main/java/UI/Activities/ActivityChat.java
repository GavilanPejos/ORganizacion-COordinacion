package UI.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jmgavilan.organizacion_coordinacion.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityChat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        final ImageView backBtn = findViewById(R.id.ACIV_BackBtn);
        final TextView nameTV = findViewById(R.id.ACTV_NombreUsuario);
        final EditText messageET = findViewById(R.id.ACET_messageEdit);
        final CircleImageView imgUser = findViewById(R.id.ACCIV_profilePic);
        final ImageView sendBTN = findViewById(R.id.ACIV_SendBtn);
        //pillar datos
        final String getName = getIntent().getStringExtra("email");
        final String getImgUser = getIntent().getStringExtra("imgUrl");//dudoso
        nameTV.setText(getName);
        imgUser.setImageURI(Uri.parse(getImgUser));
        backBtn.setOnClickListener(v -> finish());

    }
}