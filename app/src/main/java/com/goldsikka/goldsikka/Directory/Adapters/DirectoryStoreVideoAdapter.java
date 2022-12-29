package com.goldsikka.goldsikka.Directory.Adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Directory.DirectoryStoreDetails;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DirectoryStoreVideoAdapter extends RecyclerView.Adapter<DirectoryStoreVideoAdapter.ViewHolder> {

    Context context;
    ArrayList<Listmodel> videolist;

    View mView;
    private ImageView imagePost;
//    private FullscreenVideoLayout videoLayout;
    private VideoView videoView;



    public DirectoryStoreVideoAdapter(Context context,  ArrayList<Listmodel> videolist) {
        this.context = context;
        this.videolist = videolist;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate( R.layout.activity_directory_tagslisting_adapter, parent, false);

        return new ViewHolder(view);
    }


    @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Listmodel listmodel = videolist.get(position);

    }

    @Override
    public int getItemCount() {
        return  videolist.size();

    }

    public void filterList(ArrayList<Listmodel> tagsfilter) {
        videolist = tagsfilter;
        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
//            imagePost =(ImageView)mView.findViewById(R.id.post_image);
//            videoLayout = (FullscreenVideoLayout) mView.findViewById(R.id.post_video);
//
//        }
//        public void setTitle(String title){
//
//            TextView post_title = (TextView)mView.findViewById(R.id.post_title);
//            post_title.setText(title);
//
//        }
//
//        public void setDesc(String desc){
//
//            TextView post_desc = (TextView)mView.findViewById(R.id.post_desc);
//            post_desc.setText(desc);
//        }


//        public void setImage(final Context c,final String imageUrl){

            //

//            Picasso.with(c).load(imageUrl).error(R.mipmap.add_btn).fit().centerInside().placeholder(R.mipmap.add_btn)
//                    .networkPolicy(NetworkPolicy.OFFLINE).into(imagePost, new Callback() {
//                @Override
//                public void onSuccess() {
//
//                }
//
//                @Override
//                public void onError() {
//
//                    //Reloading an image again ...
////                    Picasso.with(c).load(imageUrl).error(R.mipmap.add_btn).placeholder(R.mipmap.add_btn)
////                            .into(imagePost);
//                }
//            });

        }

        public void setVideo(final Context c, final String videoUrl){


            // videoLayout.setActivity(this);
            // videoLayout.setActivity(get);

            Uri videoUri = Uri.parse(videoUrl);
            //                videoLayout.setVideoURI(videoUri);
//                videoLayout.setTag(videoUrl);


//        }


//        @Override
//        public void onClick(View view) {
//
//        }
    }

        @Override
        public void onClick(View v) {
//            itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}