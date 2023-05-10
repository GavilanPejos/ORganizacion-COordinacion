package UI.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.jmgavilan.organizacion_coordinacion.R;

public class ViewPageAdapterSlider extends PagerAdapter {

    Context context;
    int imagenes[]={
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image4
    };
    int titulos[]={
            R.string.heading_one,
            R.string.heading_two,
            R.string.heading_three
    };
    int descripciones[]={
            R.string.desc_one,
            R.string.desc_two,
            R.string.desc_three
    };

    public ViewPageAdapterSlider(Context context){
        this.context=context;
    }
    @Override
    public int getCount() {
        return titulos.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout,container,false);
        ImageView slideImage= view.findViewById(R.id.tile);
        TextView slideHeading=view.findViewById(R.id.texttile);
        TextView slideDescrip=view.findViewById(R.id.textdescr);

        slideImage.setImageResource(imagenes[position]);
        slideHeading.setText(titulos[position]);
        slideDescrip.setText(descripciones[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       container.removeView((LinearLayout)object);
    }
}
