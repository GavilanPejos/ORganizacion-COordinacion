package UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.analytics.FirebaseAnalytics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.jmgavilan.organizacion_coordinacion.R;

import UI.data.ViewPageAdapterSlider;

public class LauncherAct extends AppCompatActivity {
    ViewPager VP_SlideViewPager;
    LinearLayout LL_ARivera;
    Button BTN_skip, BTN_next, BTN_back;
    TextView[] puntitos;
    private FirebaseAnalytics firebaseAnalytics;

    ViewPageAdapterSlider viewPageAdapterSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        Intent intro = new Intent(this, HUDLogin.class);


        BTN_skip = findViewById(R.id.ALBTN_SKIP);
        BTN_back = findViewById(R.id.ALBTN_IZQPREV);
        BTN_next = findViewById(R.id.ALBTN_DERNEXT);
        VP_SlideViewPager = findViewById(R.id.ALFC_SliderContainer);
        LL_ARivera = findViewById(R.id.ALLL_linea);
        viewPageAdapterSlider = new ViewPageAdapterSlider(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.logEvent("hola",savedInstanceState);

        VP_SlideViewPager.setAdapter(viewPageAdapterSlider);

        // configurar indicadores
        indicador(0);

        // ocultar boton back al inicio
        BTN_back.setVisibility(View.INVISIBLE);

        VP_SlideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // actualizar indicadores
                indicador(position);

                // ocultar boton back en la primera pagina
                if (position == 0) {
                    BTN_back.setVisibility(View.INVISIBLE);
                } else {
                    BTN_back.setVisibility(View.VISIBLE);
                }

                // mostrar boton skip en la ultima pagina
                if (position == viewPageAdapterSlider.getCount() - 1) {
                    BTN_skip.setVisibility(View.VISIBLE);
                    BTN_next.setText("START");
                } else {
                    BTN_skip.setVisibility(View.INVISIBLE);
                    BTN_next.setText("NEXT");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        BTN_back.setOnClickListener(v -> {
            // retroceder una pagina
            int currentPosition = VP_SlideViewPager.getCurrentItem();
            VP_SlideViewPager.setCurrentItem(currentPosition - 1, true);
        });

        BTN_next.setOnClickListener(v -> {
            int currentPosition = VP_SlideViewPager.getCurrentItem();
            if (currentPosition == viewPageAdapterSlider.getCount() - 1) {
                // ir a la actividad de inicio
                startActivity(intro);
                finish();
            } else {
                // avanzar una pagina
                VP_SlideViewPager.setCurrentItem(currentPosition + 1, true);
            }
        });

        BTN_skip.setOnClickListener(v -> {
            // ir a la actividad de inicio
            startActivity(intro);
            finish();
        });

    }

    public void indicador(int posicion) {
        puntitos = new TextView[3]; // Se crea un arreglo de 3 TextViews
        LL_ARivera.removeAllViews(); // Se remueven todas las vistas del LinearLayout LL_ARivera

        for (int i = 0; i < puntitos.length; i++) {
            puntitos[i] = new TextView(this); // Se crea un nuevo TextView
            puntitos[i].setText(Html.fromHtml("&#8226;")); // Se establece el texto del TextView con un punto
            puntitos[i].setTextSize(40); // Se establece el tamaño del texto en 40
            puntitos[i].setTextColor(getResources().getColor(R.color.DIU_CIRCLE, getTheme())); // Se establece el color del texto con un recurso de color definido en R.color.DIU_CIRCLE
            LL_ARivera.addView(puntitos[i]); // Se añade el TextView al LinearLayout LL_ARivera
        }
        puntitos[posicion].setTextColor(getResources().getColor(R.color.NOC_CIRCLE, getTheme())); // Se cambia el color del TextView en la posición actual con un recurso de color definido en R.color.NOC_CIRCLE
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // Este método es llamado cuando la página es desplazada
        }

        @Override
        public void onPageSelected(int position) {
            indicador(position); // Se llama la función "indicador" con la posición actual

            if (position == 0) {
                BTN_back.setVisibility(View.INVISIBLE); // Si la posición es 0, se oculta el botón BTN_back
            } else {
                BTN_back.setVisibility(View.VISIBLE); // Si la posición no es 0, se muestra el botón BTN_back
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    //Ocultar barra de navegación y status
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}