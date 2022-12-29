package com.goldsikka.goldsikka.Activitys.Events;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateEvents extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.eteventname)
    EditText eteventname;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etholdername)
    EditText etholdername;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.eteventdate)
    EditText eteventdate;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etvenue)
    EditText etvenue;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etdescription)
    EditText etdescription;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ettime)
    EditText ettime;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etgroomname)
    EditText etgroomname;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etbride)
    EditText etbride;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etotheventname)
    EditText etotheventname;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivdate)
    ImageView ivdate;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivtime)
    ImageView ivtime;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivprofile)
    ImageView ivprofile;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivcouple)
    ImageView ivcouple;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivwedding)
    ImageView ivwedding;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.spinevent)
    Spinner spinevent ;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.spinselect)
    Spinner spinselect;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.mllspintype)
    LinearLayout mllspintype;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.mllgrome)
    LinearLayout mllgrome;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.mllbride)
    LinearLayout mllbride;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llotherholder)
    LinearLayout llotherholder;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llotheeteventname)
    LinearLayout llotheeteventname;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llotherimg)
    LinearLayout llotherimg;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.mllcoule)
    LinearLayout mllcoule;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.mllwecding)
    LinearLayout mllwecding;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llmrgimgtext)
    LinearLayout llmrgimgtext;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_eventname)
    TextView tv_eventname;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvholdername)
    TextView tvholdername;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvdate)
    TextView tvdate;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvvenue)
    TextView tvvenue;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvdescription)
    TextView tvdescription;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvimg)
    TextView tvimg;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvspin)
    TextView tvspin;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvspinevent)
    TextView tvspinevent;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvgroom)
    TextView tvgroom;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvbride)
    TextView tvbride;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvcouple)
    TextView tvcouple;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvwedding)
    TextView tvwedding;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvtime)
    TextView tvtime;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvotheventname)
    TextView tvotheventname;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvcouple_photo)
    TextView tvcouple_photo;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvwedding_card)
    TextView tvwedding_card;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvselectimg)
    TextView tvselectimg;




    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.loading_gif)
    GifImageView loading_gif;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btsubmit)
    Button btsubmit;

    ApiDao apiDao;
    String[] steventypearray = {"Select Event Type","Marriage","Others"};
    String [] arrymarriagegender = {"Select Bride or Groom","Bride","BrideGroom"};

    ArrayList<String> eventtypelist ;
    ArrayList<String> marrigepersionlist ;

    String subevent,submarriagegender,steventtype,stmrarigegender;

    private static final String TAG = "Goldsikka";
    private static final int CAMERA_REQUEST = 1887;
    private static final int OTHERSIMG = 1888;
    private static final int WeddingCAMERA_REQUEST = 1889;

    File couplefile,weddingfile,othereventfile;
    Uri coupleuri,weddinguri,otheruri;
    RequestBody couplerequestBody,weddingrequestBody,othereventrequestBody;
    List<MultipartBody.Part> couplepart,weddingparts;

    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    String  steventname,steventdate,steventtime,stholdername,stgroom,stbride,stvenue,stdescription;
    String isteventdate,isteventname,istgroom,istbride,istvenue,istdescription,
            isteventtype,isteventtime,istholdername,iid,iother_event_type,iimage,ieventid,stotheventname;

    RequestBody rqsteventname,rqsteventdate,rqsteventtime,rqstholdername,rqstgroom,
            rqstbride,rqstvenue,rqstdescription,rqsteventtype,rqstmrarigegender,rqstotheventname;

    TextView unameTv, uidTv, titleTv;
    RelativeLayout backbtn;
    ScrollView scrollview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_events);
        ButterKnife.bind(this);
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setHint();
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));

        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Event");

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.YEAR, year);

                updateLabel();
            }

        };

        intilizeviews();
    }

    public void setHint(){
        eteventname.setHint(Html.fromHtml(getString(R.string.event_name)));
        etholdername.setHint(Html.fromHtml(getString(R.string.event_holder)));
        eteventdate.setHint(Html.fromHtml(getString(R.string.event_date)));
        etvenue.setHint(Html.fromHtml(getString(R.string.venue)));
        etdescription.setHint(Html.fromHtml(getString(R.string.event_description)));
        ettime.setHint(Html.fromHtml(getString(R.string.time)));
        etgroomname.setHint(Html.fromHtml(getString(R.string.bridegroom_name)));
        etbride.setHint(Html.fromHtml(getString(R.string.bride_name)));
        etotheventname.setHint(Html.fromHtml(getString(R.string.other_event)));
        tvcouple_photo.setText(Html.fromHtml(getString(R.string.upload_couple_photo)));
        tvwedding_card.setText(Html.fromHtml(getString(R.string.upload_wedding_card_photo)));
        tvselectimg.setText(Html.fromHtml(getString(R.string.upload_photo)));
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
        // finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return false;
//            case R.id.mnqrscaner:
//                payaleartdialog();
//                break;
//            case R.id.eventlist:
//                Intent eventintent = new Intent(this, Eventlist.class);
//                startActivity(eventintent);
//                break;
        }

        return true;
    }

//    AlertDialog alertDialog;
//    LinearLayout llqrcode,lleventid;
//    public void payaleartdialog(){
//        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogview = inflater.inflate(R.layout.eventpayvia,null);
//        alertbuilder.setCancelable(true);
//        alertbuilder.setView(dialogview);
//        alertDialog = alertbuilder.create();
//        alertDialog.show();
//        lleventid = dialogview.findViewById(R.id.lleventid);
//        llqrcode = dialogview.findViewById(R.id.llqrcode);
//
//        llqrcode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CreateEvents.this, EventQRCodeScanner.class);
//                startActivity(intent);
//                alertDialog.dismiss();
//            }
//        });
//        lleventid.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                eventiddilog();
//                alertDialog.dismiss();
//            }
//        });
//    }
//    AlertDialog alertDialogid;
//    EditText eteventid;
//    TextView tvverify,tveventid;
//    String steventid;
//    private void eventiddilog(){
//        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogview = inflater.inflate(R.layout.payviaeventid,null);
//        alertbuilder.setCancelable(true);
//        alertbuilder.setView(dialogview);
//        alertDialogid = alertbuilder.create();
//        alertDialogid.show();
//        tvverify = dialogview.findViewById(R.id.tvverify);
//        eteventid = dialogview.findViewById(R.id.eteventid);
//        tveventid =  dialogview.findViewById(R.id.tveventid);
//        tvverify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                steventid = eteventid.getText().toString().trim();
//                openverifyeventid();
//
//            }
//        });
//
//    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.maintoolmenu,menu);
//        return true;
//    }


    public void intilizeviews(){

        String mydateFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(mydateFormat, Locale.ENGLISH);
        eteventdate.setText(sdf.format(myCalendar.getTime()));

        String mytimeFormat = "HH:mm"; //In which you need put here
        SimpleDateFormat sdftime = new SimpleDateFormat(mytimeFormat, Locale.ENGLISH);
        ettime.setText(sdftime.format(myCalendar.getTime()));


        ArrayAdapter evtypeadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,steventypearray);
        evtypeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinevent.setAdapter(evtypeadapter);
        eventspinnerclick();

        ArrayAdapter genderadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,arrymarriagegender);
        genderadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinselect.setAdapter(genderadapter);
        spinclick();
    }
    public void eventspinnerclick(){

        eventtypelist = new ArrayList<>();
        // spinner_signuptype = findViewById(R.id.sub_category);

        spinevent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subevent = spinevent.getItemAtPosition(spinevent.getSelectedItemPosition()).toString();
                ((TextView) view).setTextColor(ContextCompat.getColor(CreateEvents.this, R.color.DarkBrown));

                if (subevent.equals("Marriage")) {
                    steventtype = "MRG";
                    rqsteventtype = RequestBody.create(MediaType.parse("text/plain"), steventtype);
                    mllspintype.setVisibility(View.VISIBLE);
                    mllbride.setVisibility(View.VISIBLE);
                    mllcoule.setVisibility(View.VISIBLE);
                    mllgrome.setVisibility(View.VISIBLE);
                    mllwecding.setVisibility(View.VISIBLE);
                    llotherholder.setVisibility(View.GONE);
                    llotherimg.setVisibility(View.GONE);
                    llmrgimgtext.setVisibility(View.VISIBLE);
                    llotheeteventname.setVisibility(View.GONE);

                }else if (subevent.equals("Others")){
                    steventtype = "OTH";
                    rqsteventtype = RequestBody.create(MediaType.parse("text/plain"), steventtype);
                    mllspintype.setVisibility(View.GONE);
                    mllbride.setVisibility(View.GONE);
                    mllcoule.setVisibility(View.GONE);
                    mllwecding.setVisibility(View.GONE);
                    mllgrome.setVisibility(View.GONE);
                    llotherholder.setVisibility(View.VISIBLE);
                    llotherimg.setVisibility(View.VISIBLE);
                    llmrgimgtext.setVisibility(View.GONE);
                    llotheeteventname.setVisibility(View.VISIBLE);
                }else if (subevent.equals("Select Event Type")){
                    mllspintype.setVisibility(View.GONE);
                    mllbride.setVisibility(View.GONE);
                    mllcoule.setVisibility(View.GONE);
                    mllwecding.setVisibility(View.GONE);
                    mllgrome.setVisibility(View.GONE);
                    llotherholder.setVisibility(View.GONE);
                    llotherimg.setVisibility(View.VISIBLE);
                    llmrgimgtext.setVisibility(View.GONE);
                    llotheeteventname.setVisibility(View.GONE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
    }

    public void spinclick(){

        marrigepersionlist = new ArrayList<>();
        // spinner_signuptype = findViewById(R.id.sub_category);

        spinselect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                submarriagegender = spinselect.getItemAtPosition(spinselect.getSelectedItemPosition()).toString();
                //spinner_signuptype.getSelectedView().setTextColor(getResources().getColor(R.color.Blue));
                ((TextView) view).setTextColor(ContextCompat.getColor(CreateEvents.this, R.color.DarkBrown));

                if (submarriagegender.equals("Bride")) {
                    stmrarigegender = "F";
                    rqstmrarigegender = RequestBody.create(MediaType.parse("text/plain"), stmrarigegender);

                }else if (submarriagegender.equals("BrideGroom")){
                    stmrarigegender = "M";
                    rqstmrarigegender = RequestBody.create(MediaType.parse("text/plain"), stmrarigegender);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        eteventdate.setText(sdf.format(myCalendar.getTime()));
    }
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.ivdate)
    public void datepicker(){

        DatePickerDialog datePickerDialog =  new DatePickerDialog(CreateEvents.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.ivtime)
    public void timepicker(){

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        int second = mcurrentTime.get(Calendar.SECOND);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CreateEvents.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                ettime.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.ivcouple)
    public void getcoupleimage(){
        requestPermissions(
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
    }

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.ivwedding)
    public void getweddingimage(){
        requestPermissions(
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                2);
    }

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.ivprofile)
    public void getimage(){
        requestPermissions(
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                3);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission Granted ");
            switch (requestCode) {
                case 1:
                    showChooser();
                    break;
                case 2:
                    showweddingChooser();
                    break;
                case 3:
                    showotherimges();
                    break;
            }
        } else {
            Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
        }
    }
    public void showotherimges(){
        Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent1.addCategory(Intent.CATEGORY_OPENABLE);
        intent1.setType("*/*");
        startActivityForResult(intent1, OTHERSIMG);
    }
    public void showweddingChooser(){
        Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent1.addCategory(Intent.CATEGORY_OPENABLE);
        intent1.setType("*/*");
        startActivityForResult(intent1, WeddingCAMERA_REQUEST);
    }
    private void showChooser() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, CAMERA_REQUEST);
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            Log.e("from couple","couple");
            //If the file selection was successful
            if (resultCode == RESULT_OK) {
                if ( data != null && data.getData() != null){

                    try {
                        coupleuri = data.getData();
                        assert coupleuri != null;
                        // Log.e("Uri",uri.toString());
                        Log.i(TAG, "Uri = " + coupleuri.toString());
                        ivcouple.setImageURI(coupleuri);

                        couplefile = FileUtilty.getFile(this, coupleuri);

                        Log.e("file",couplefile.toString());
                        couplepart = new ArrayList<>();
                        couplepart.add(prepareFilePart("photo", coupleuri));


                    } catch (Exception e) {
                        Log.e(TAG, "File select error", e);
                    }
                }
            }
        }else if (requestCode == WeddingCAMERA_REQUEST) {
            Log.e("from wedding", "wedding");
            if (resultCode == RESULT_OK) {
                if (data != null && data.getData() != null) {

                    try {
                        weddinguri = data.getData();
                        assert weddinguri != null;
                        // Log.e("Uri",uri.toString());
                        Log.i(TAG, "Uri = " + weddinguri.toString());
                        ivwedding.setImageURI(weddinguri);
                        weddingfile = FileUtilty.getFile(this, weddinguri);

                        Log.e("file", weddingfile.toString());
                        weddingparts = new ArrayList<>();
                        weddingparts.add(prepareweddingFilePart("wedding_card_photo", weddinguri));


                    } catch (Exception e) {
                        Log.e(TAG, "File select error", e);
                    }
                }
            }
        }
        else if (requestCode == OTHERSIMG ) {
            Log.e("from other","other");
            if (resultCode == RESULT_OK) {
                if (data != null && data.getData() != null) {

                    try {
                        otheruri = data.getData();
                        assert otheruri != null;
                        // Log.e("Uri",uri.toString());
                        Log.i(TAG, "Uri = " + otheruri.toString());
                        ivprofile.setImageURI(otheruri);
                        othereventfile = FileUtilty.getFile(this, otheruri);

                        Log.e("file", othereventfile.toString());
                        couplepart = new ArrayList<>();
                        couplepart.add(Otherwedding("photo", otheruri));
                        Log.e("coucjksdjc", String.valueOf(couplepart));

                    } catch (Exception e) {
                        Log.e(TAG, "File select error", e);
                    }
                }
            }
        }



        super.onActivityResult(requestCode, resultCode, data);
    }

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        couplerequestBody = RequestBody.create(MediaType.parse(Objects.requireNonNull
                (this.getContentResolver().getType(fileUri))), couplefile);

        return MultipartBody.Part.createFormData(partName, couplefile.getName(),couplerequestBody);
    }

    private MultipartBody.Part prepareweddingFilePart(String partName, Uri fileUri) {

        weddingrequestBody = RequestBody.create(MediaType.parse(Objects.requireNonNull
                (this.getContentResolver().getType(fileUri))), weddingfile);

        return MultipartBody.Part.createFormData(partName, weddingfile.getName(),weddingrequestBody);
    }

    private MultipartBody.Part Otherwedding(String partName, Uri fileUri) {

        othereventrequestBody = RequestBody.create(MediaType.parse(Objects.requireNonNull
                (this.getContentResolver().getType(fileUri))), othereventfile);

        return MultipartBody.Part.createFormData(partName, othereventfile.getName(),othereventrequestBody);
    }


    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btsubmit)
    public void initvalidation(){

        btsubmit .setVisibility(View.GONE);
        loading_gif.setVisibility(View.VISIBLE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {

                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                CreateEvents.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!NetworkUtils.isConnected(CreateEvents.this)){
                            ToastMessage.onToast(CreateEvents.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

                        }else {
                            validation();
                            loading_gif.setVisibility(View.GONE);
                            btsubmit.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }
        }, 500);

    }

    public void validation(){
        scrollview = findViewById(R.id.scrollview);

        tv_eventname.setVisibility(View.GONE);
        tvdate.setVisibility(View.GONE);
        tvtime.setVisibility(View.GONE);
        tvdescription.setVisibility(View.GONE);
        tvvenue.setVisibility(View.GONE);
        tvholdername.setVisibility(View.GONE);
        tvimg .setVisibility(View.GONE);
        tvcouple.setVisibility(View.GONE);
        tvwedding.setVisibility(View.GONE);
        tvgroom.setVisibility(View.GONE);
        tvbride.setVisibility(View.GONE);
        tvspin.setVisibility(View.GONE);
        tvspinevent.setVisibility(View.GONE);
        tvotheventname.setVisibility(View.GONE);

        steventname = eteventname.getText().toString().trim();
        scrollview.smoothScrollTo(eteventname.getLeft(), eteventname.getTop());

        steventdate = eteventdate.getText().toString().trim();
        scrollview.smoothScrollTo(eteventdate.getLeft(), eteventdate.getTop());

        steventtime = ettime.getText().toString().trim();
        scrollview.smoothScrollTo(ettime.getLeft(), ettime.getTop());

        stholdername = etholdername.getText().toString().trim();
        scrollview.smoothScrollTo(etholdername.getLeft(), etholdername.getTop());

        stgroom  = etgroomname.getText().toString().trim();
        scrollview.smoothScrollTo(etgroomname.getLeft(), etgroomname.getTop());

        stbride = etbride.getText().toString().trim();
        scrollview.smoothScrollTo(etbride.getLeft(), etbride.getTop());

        stvenue = etvenue.getText().toString().trim();
        scrollview.smoothScrollTo(etvenue.getLeft(), etvenue.getTop());

        stdescription = etdescription.getText().toString().trim();
        scrollview.smoothScrollTo(etdescription.getLeft(), etdescription.getTop());

        stotheventname = etotheventname.getText().toString().trim();
        scrollview.smoothScrollTo(etotheventname.getLeft(), etotheventname.getTop());



        rqsteventname = RequestBody.create(MediaType.parse("text/plain"), steventname);
        rqsteventdate = RequestBody.create(MediaType.parse("text/plain"), steventdate);
        rqsteventtime = RequestBody.create(MediaType.parse("text/plain"), steventtime);
        rqstholdername = RequestBody.create(MediaType.parse("text/plain"), stholdername);
        rqstgroom = RequestBody.create(MediaType.parse("text/plain"), stgroom);
        rqstbride = RequestBody.create(MediaType.parse("text/plain"), stbride);
        rqstvenue = RequestBody.create(MediaType.parse("text/plain"), stvenue);
        rqstdescription = RequestBody.create(MediaType.parse("text/plain"), stdescription);
        rqstdescription = RequestBody.create(MediaType.parse("text/plain"), stdescription);
        rqstotheventname =RequestBody.create(MediaType.parse("text/plain"),stotheventname);

        if (subevent.equals("Select Event Type")){
            tvspinevent.setVisibility(View.VISIBLE);
            tvspinevent.setText("Please select the type of Event..");
            ToastMessage.onToast(this,"Please select the type of Event..",ToastMessage.ERROR);
        }else {
            createevent();
        }

    }
    public void createevent(){

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
            Call<EventModel> postdetails = apiDao.createevent("Bearer "+AccountUtils.getAccessToken(this)
                    ,rqsteventtype,rqsteventname,rqsteventdate,rqsteventtime,rqstdescription,rqstotheventname,rqstvenue
                    ,rqstmrarigegender,rqstbride,
                    rqstgroom,rqstholdername,couplepart,weddingparts);

            postdetails.enqueue(new Callback<EventModel>() {
                @Override
                public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                    int statuscode  = response.code();
                    Log.e("event statuscode ", String.valueOf(statuscode));
                    if (statuscode == HttpsURLConnection.HTTP_OK||statuscode == HttpsURLConnection.HTTP_CREATED){
                        dialog.dismiss();
                        EventModel model = response.body();
                        ToastMessage.onToast(CreateEvents.this,model.getMessage(),ToastMessage.SUCCESS);
                        JsonObject from = new JsonParser().parse(model.getEvent().toString()).getAsJsonObject();
                        try {
                            JSONObject json_from = new JSONObject(from.toString());

                            isteventdate = json_from.getString("event_date");
                            isteventname = json_from.getString("event_name");
                            istgroom = json_from.getString("groom_name");
                            istbride = json_from.getString("bride_name");
                            istvenue = json_from.getString("venue");
                            istdescription = json_from.getString("description");
                            isteventtype = json_from.getString("event_type");
                            isteventtime = json_from.getString("muhurtham_time");
                            istholdername = json_from.getString("holder_name");
                            iid = json_from.getString("id");
                            ieventid = json_from.getString("event_id");
                            iimage = json_from.getString("photoImageLink");
                            iother_event_type = json_from.getString("other_event_type");
                            onsucess("create");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        tv_eventname.setVisibility(View.GONE);
                        tvdate.setVisibility(View.GONE);
                        tvtime.setVisibility(View.GONE);
                        tvdescription.setVisibility(View.GONE);
                        tvvenue.setVisibility(View.GONE);
                        tvholdername.setVisibility(View.GONE);
                        tvimg .setVisibility(View.GONE);
                        tvcouple.setVisibility(View.GONE);
                        tvwedding.setVisibility(View.GONE);
                        tvgroom.setVisibility(View.GONE);
                        tvbride.setVisibility(View.GONE);
                        tvspin.setVisibility(View.GONE);
                        tvspinevent.setVisibility(View.GONE);
                        tvotheventname.setVisibility(View.GONE);
                        dialog.dismiss();
                        assert response.errorBody() != null;

                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String st = jObjError.getString("message");
                            ToastMessage.onToast(CreateEvents.this, st, ToastMessage.ERROR);

                            JSONObject er = jObjError.getJSONObject("errors");
                            try {
                                JSONArray array_mobile = er.getJSONArray("event_name");
                                for (int i = 0; i < array_mobile.length(); i++) {
                                    tv_eventname.setVisibility(View.VISIBLE);
                                    tv_eventname.setText(array_mobile.getString(i));

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray array_mobile = er.getJSONArray("event_date");
                                for (int i = 0; i < array_mobile.length(); i++) {
                                    tvdate.setVisibility(View.VISIBLE);
                                    tvdate.setText(array_mobile.getString(i));

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray array_mobile = er.getJSONArray("description");
                                for (int i = 0; i < array_mobile.length(); i++) {
                                    tvdescription.setVisibility(View.VISIBLE);
                                    tvdescription.setText(array_mobile.getString(i));

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray array_mobile = er.getJSONArray("photo");
                                for (int i = 0; i < array_mobile.length(); i++) {
                                    if (steventtype.equals("OTH")){
                                        tvimg.setVisibility(View.VISIBLE);
                                        tvimg.setText(array_mobile.getString(i));
                                    }
                                    else {
                                        tvcouple.setVisibility(View.VISIBLE);
                                        tvcouple.setText(array_mobile.getString(i));
                                    }

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray array_mobile = er.getJSONArray("venue");
                                for (int i = 0; i < array_mobile.length(); i++) {
                                    tvvenue.setVisibility(View.VISIBLE);
                                    tvvenue.setText(array_mobile.getString(i));

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }  try {
                                JSONArray array_mobile = er.getJSONArray("holder_name");
                                for (int i = 0; i < array_mobile.length(); i++) {
                                    tvholdername.setVisibility(View.VISIBLE);
                                    tvholdername.setText(array_mobile.getString(i));

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray array_mobile = er.getJSONArray("other_event_type");
                                for (int i = 0; i < array_mobile.length(); i++) {
                                    tvotheventname.setVisibility(View.VISIBLE);
                                    tvotheventname.setText(array_mobile.getString(i));

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray array_mobile = er.getJSONArray("muhurtham_time");
                                for (int i = 0; i < array_mobile.length(); i++) {
                                    tvtime.setVisibility(View.VISIBLE);
                                    tvtime.setText(array_mobile.getString(i));

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                JSONArray array_mobile = er.getJSONArray("wedding_card_photo");
                                for (int i = 0; i < array_mobile.length(); i++) {
                                    tvwedding.setVisibility(View.VISIBLE);
                                    tvwedding.setText(array_mobile.getString(i));

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray array_mobile = er.getJSONArray("bride_name");
                                for (int i = 0; i < array_mobile.length(); i++) {
                                    tvbride.setVisibility(View.VISIBLE);
                                    tvbride.setText(array_mobile.getString(i));

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray array_mobile = er.getJSONArray("groom_name");
                                for (int i = 0; i < array_mobile.length(); i++) {
                                    tvgroom.setVisibility(View.VISIBLE);
                                    tvgroom.setText(array_mobile.getString(i));

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray array_mobile = er.getJSONArray("gender");
                                for (int i = 0; i < array_mobile.length(); i++) {
                                    tvspin.setVisibility(View.VISIBLE);
                                    tvspin.setText("Please select the Bride or Groom");

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }



                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                            Log.e("exception",e.toString());
                        }


                    }
                }

                @Override
                public void onFailure(Call<EventModel> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("cer error",t.toString());
                    onsucess("create");
//                    ToastMessage.onToast(CreateEvents.this,t.toString(),ToastMessage.ERROR);
                    //  ToastMessage.onToast(CreateEvents.this,"We have some issue please try after some time",ToastMessage.ERROR);
                }
            });
        }
    }
//    public void openverifyeventid(){
//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setMessage("Please Wait....");
//        dialog.setCancelable(false);
//        dialog.show();
//
//        if (!NetworkUtils.isConnected(this)) {
//            dialog.dismiss();
//            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
//            return;
//        }
//        else {
//            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
//            Call<EventModel> verify = apiDao.eventverify("Bearer "+AccountUtils.getAccessToken(this),"2",steventid);
//            verify.enqueue(new Callback<EventModel>() {
//                @Override
//                public void onResponse(Call<EventModel> call, Response<EventModel> response) {
//                    int statuscode = response.code();
//                    Log.e("code", String.valueOf(statuscode));
//                    if (statuscode == HttpsURLConnection.HTTP_OK||statuscode == HttpsURLConnection.HTTP_CREATED){
//                        dialog.dismiss();
//                        EventModel model = response.body();
//                        JsonObject from = new JsonParser().parse(model.getEventDetails().toString()).getAsJsonObject();
//                        try {
//                            JSONObject json_from = new JSONObject(from.toString());
//
//                            isteventdate = json_from.getString("event_date");
//                            isteventname = json_from.getString("event_name");
//                            istgroom = json_from.getString("groom_name");
//                            istbride = json_from.getString("bride_name");
//                            istvenue = json_from.getString("venue");
//                            istdescription = json_from.getString("description");
//                            isteventtype = json_from.getString("event_type");
//                            isteventtime = json_from.getString("muhurtham_time");
//                            istholdername = json_from.getString("holder_name");
//                            iid = json_from.getString("id");
//                            ieventid = json_from.getString("event_id");
//                            iimage = json_from.getString("photoImageLink");
//
//                            onsucess("validation");
//                            alertDialogid.dismiss();
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    else if (statuscode == 422){
//                        dialog.dismiss();
//                        tveventid.setVisibility(View.GONE);
//                        assert response.errorBody() != null;
//                        alertDialogid.dismiss();
//                        try {
//                            JSONObject jObjError = new JSONObject(response.errorBody().string());
//                            String st = jObjError.getString("message");
//                            ToastMessage.onToast(CreateEvents.this, st, ToastMessage.ERROR);
//
//                            JSONObject er = jObjError.getJSONObject("errors");
//                            try {
//                                JSONArray array_mobile = er.getJSONArray("eventId");
//                                for (int i = 0; i < array_mobile.length(); i++) {
//                                    tveventid.setVisibility(View.VISIBLE);
//                                    tveventid.setText(array_mobile.getString(i));
//
//                                }
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//
//                        } catch (JSONException | IOException e) {
//                            e.printStackTrace();
//                            Log.e("exception",e.toString());
//                        }
//                    }
//                    else {
//                        dialog.dismiss();
//                        tveventid.setVisibility(View.GONE);
//                        assert response.errorBody() != null;
//                        alertDialogid.dismiss();
//                        try {
//                            JSONObject jObjError = new JSONObject(response.errorBody().string());
//                            String st = jObjError.getString("message");
//                            ToastMessage.onToast(CreateEvents.this, st, ToastMessage.ERROR);
//
//
//                        } catch (JSONException | IOException e) {
//                            e.printStackTrace();
//                            Log.e("exception",e.toString());
//                        }
//
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<EventModel> call, Throwable t) {
//                    Log.e("fail", String.valueOf(t));
//                    dialog.dismiss();
//                    alertDialogid.dismiss();
//                    ToastMessage.onToast(CreateEvents.this,"We have some issues",ToastMessage.ERROR);
//                }
//            });
//        }
//    }
    public void onsucess(String from){

            Intent intent = new Intent(this, EventConformation.class);
            intent.putExtra("etype", isteventtype);
            intent.putExtra("ename", isteventname);
            intent.putExtra("ebride", istbride);
            intent.putExtra("egroom", istgroom);
            intent.putExtra("ehoder", istholdername);
            intent.putExtra("evenue", istvenue);
            intent.putExtra("edate", isteventdate);
            intent.putExtra("etime", isteventtime);
            intent.putExtra("edes", istdescription);
            intent.putExtra("iid", iid);
            intent.putExtra("ieventid", ieventid);
            intent.putExtra("iimage", iimage);
            intent.putExtra("othtype", iother_event_type);
            intent.putExtra("from", from);
            Log.e("imageefef", iimage);
            startActivity(intent);
    }
}