package com.goldsikka.goldsikka.Activitys.Events;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.squareup.picasso.Picasso;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetails extends AppCompatActivity {

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
    @BindView(R.id.tvdes)
    TextView tvdes;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvenetype)
    TextView tvenetype;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivevent)
    ImageView ivevent;
//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.ivwedding)
//    ImageView ivwedding;

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
    @BindView(R.id.imagecouple)
    ImageView imagecouple;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.imageweddingcard)
    ImageView imageweddingcard;

    ApiDao apiDao;

    String  stimage,steventname,steventdate,steventtime,
            stothertype,stholdername,stgroom,stbride,stvenue,stdescription,steventtype,stwedding,steventid;
    RelativeLayout backbtn;



TextView unameTv, uidTv, titleTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        ButterKnife.bind(this);

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
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            steventid = bundle.getString("eventid");
        }
        getdetails();
    }
    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
        // finish();
    }

    public void getdetails(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (!NetworkUtils.isConnected(this)) {
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }
        else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<EventModel> getdetails = apiDao.geteventdetails("Bearer "+AccountUtils.getAccessToken(this),steventid);
            getdetails.enqueue(new Callback<EventModel>() {
                @Override
                public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                    int statuscode  = response.code();
                    Log.e("Stattuscode", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK){
                        dialog.dismiss();
                        EventModel model = response.body();
                        steventtype = model.getEvent_type();
                        stholdername = model.getHolder_name();
                        steventtime = model.getMuhurtham_time();
                        stdescription = model.getDescription();
                        steventname = model.getEvent_name();
                        steventdate = model.getEvent_date();
                        stvenue = model.getVenue();
                        stgroom = model.getGroom_name();
                        stothertype =model.getOther_event_type();
                        stbride = model.getBride_name();
                        stimage = model.getPhotoImageLink();
                        stwedding = model.getWeddingCardPhotoLink();
                        setdetails();
                    }else {
                        dialog.dismiss();
                       // ToastMessage.onToast(EventDetails.this,"Technical Issue try after some time",ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<EventModel> call, Throwable t) {

                    dialog.dismiss();
                  //  ToastMessage.onToast(EventDetails.this,"we have some issue try after some time",ToastMessage.ERROR);

                }
            });
        }
    }

    public void setdetails() {
        tvevent.setText(":  "+steventname);
        tvdate.setText(":  "+steventdate);
        tvtime.setText(":  "+steventtime);
        tvvenue.setText(":  "+stvenue);
        tvdes.setText(":  "+stdescription);

        if (steventtype.equals("MRG")) {
            llholder.setVisibility(View.GONE);
            llbride.setVisibility(View.VISIBLE);
            llgroom.setVisibility(View.VISIBLE);
            tvbride.setText(":  "+stbride);
            tvgroom.setText(":  "+stgroom);
            tvenetype.setText(":  "+"Marriage");
            ivevent.setVisibility(View.GONE);
            imagecouple.setVisibility(View.VISIBLE);
            imageweddingcard.setVisibility(View.VISIBLE);
            Glide.with(this).load(stwedding).into(imagecouple);
            Glide.with(this).load(stimage).into(imageweddingcard);


        } else if (steventtype.equals("OTH")) {
            llholder.setVisibility(View.VISIBLE);
            llbride.setVisibility(View.GONE);
            llgroom.setVisibility(View.GONE);
            tvholder.setText(":  "+stholdername);
            ivevent.setVisibility(View.GONE);
            tvenetype.setText(":  "+stothertype);
            ivevent.setVisibility(View.VISIBLE);
            imagecouple.setVisibility(View.GONE);
            imageweddingcard.setVisibility(View.GONE);


            Glide.with(this).load(stimage).into(ivevent);
        }
    }
}