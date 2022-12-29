package com.goldsikka.goldsikka.Fragments.Ecommerce.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.model.Listmodel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PageviewecomAdapter extends PagerAdapter {

    Context context;
    private ArrayList<String> urls;
    List<Listmodel> listModels;
    private LayoutInflater inflater;

    public PageviewecomAdapter(Context context, ArrayList<String> urls) {
        this.context = context;
        this.urls = urls;
        for(int i=0;i<urls.size();i++)
            inflater = LayoutInflater.from(context);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.product_viewpager_ecomm, view, false);
        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.image);

        Picasso.with(context)
                .load(urls.get(position))
                .into(imageView);

        view.addView(imageLayout);
        return imageLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public int getCount() {
        return urls.size();
    }
}
