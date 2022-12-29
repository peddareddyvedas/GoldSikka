package com.goldsikka.goldsikka.Activitys.Events;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventConformation extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvevent)
    TextView tvevent;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvholder)
    TextView tvholder;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvgroom)
    TextView tvgroom;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvbride)
    TextView tvbride;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvdate)
    TextView tvdate;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvtime)
    TextView tvtime;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvvenue)
    TextView tvvenue;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvdescription)
    TextView tvdescription;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tveventtype)
    TextView tveventtype;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivevent)
    ImageView ivevent;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llholder)
    LinearLayout llholder;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llgroom)
    LinearLayout llgroom;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llbride)
    LinearLayout llbride;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btsubmit)
    Button btsubmit;

    String  stimage,steventname,steventdate,steventtime,stholdername,stgroom,stbride,stvenue,stdescription,steventtype,stid,steventid;
    String  stfrom,stveventtype;
    RelativeLayout backbtn;

    TextView unameTv, uidTv, titleTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_conformation);

        ButterKnife.bind(this);
            Bundle bundle = getIntent().getExtras();
            if (bundle!= null){
                steventtype =   bundle.getString("etype");
                steventname =   bundle.getString("ename");
                stbride = bundle.getString("ebride");
                stgroom =   bundle.getString("egroom");
                stholdername =   bundle.getString("ehoder");
                stvenue =  bundle.getString("evenue");
                steventdate =  bundle.getString("edate");
                steventtime = bundle.getString("etime");
                stdescription=      bundle.getString("edes");
                stid=      bundle.getString("iid");
                steventid = bundle.getString("ieventid");
                stimage = bundle.getString("iimage");
                stfrom = bundle.getString("from");
                stveventtype = bundle.getString("othtype");

                Log.e("info imageefef",stimage);

            }

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Events");
        setdetails();
    }
    @Override
    public void onBackPressed() {

        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
        // finish();
    }
    public void setdetails(){
        tvevent.setText(":  "+steventname);
        tvdate.setText(":  "+steventdate);
        tvtime.setText(":  "+steventtime);
        tvvenue.setText(":  "+stvenue);
        tvdescription.setText(":  "+stdescription);
        if (steventtype.equals("MRG")){
            llholder.setVisibility(View.GONE);
            llbride.setVisibility(View.VISIBLE);
            llgroom.setVisibility(View.VISIBLE);
            tvbride.setText(":  "+stbride);
            tvgroom.setText(":  "+stgroom);
            tveventtype.setText(":  "+"Marriage");
            Glide.with(this)
                    .load(stimage)
                    .into(ivevent);
        }
        else if (steventtype.equals("OTH")){
            llholder.setVisibility(View.VISIBLE);
            llbride.setVisibility(View.GONE);
            llgroom.setVisibility(View.GONE);
            tvholder.setText(":  "+stholdername);
            tveventtype.setText(":  "+stveventtype);
            Glide.with(this)
                    .load(stimage)
                    .into(ivevent);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btsubmit)
    public void openevevntscreen(){
        if (stfrom.equals("create")){
        Intent intent = new Intent(this, ContactsforEvents.class);
        intent.putExtra("eventid",stid);
        startActivity(intent);
        }
        else if (stfrom.equals("validation")){
            Intent intent = new Intent(this,EventGift.class);
            intent.putExtra("eventid",steventid);
            startActivity(intent);
        }
    }
}