package com.goldsikka.goldsikka.Directory.Adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.model.Listmodel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DirectoryImageurlsAdapter extends PagerAdapter {

    Context context;
    private ArrayList<String> urls;
    List<Listmodel> listModels;
    private LayoutInflater inflater;

    public DirectoryImageurlsAdapter(Context context, ArrayList<String> urls) {
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
        View imageLayout = inflater.inflate(R.layout.activity_directory_imageurls_adapter, view, false);
        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.image);
        final VideoView videoView = imageLayout.findViewById(R.id.storevideo);

        if(urls.get( position ).toLowerCase( Locale.ROOT ).contains( ".jpg" ) || urls.get( position ).toLowerCase( Locale.ROOT ).contains( ".png" )){
//            videoView.setVisibility( View.GONE );
//            imageView.setVisibility( View.VISIBLE );

            Picasso.with(context)
                    .load(urls.get(position))
                    .into(imageView);
        }else{
//            videoView.setVisibility( View.VISIBLE );
//            imageView.setVisibility( View.GONE );

            Uri uri = Uri.parse(urls.get( position ));
            VideoView simpleVideoView = (VideoView) imageLayout.findViewById(R.id.storevideo); // initiate a video view
            simpleVideoView.setVideoURI(uri);
            simpleVideoView.start();
        }


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