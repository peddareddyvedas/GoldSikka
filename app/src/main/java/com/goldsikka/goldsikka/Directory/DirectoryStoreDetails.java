package com.goldsikka.goldsikka.Directory;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.NumberFormat;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.goldsikka.goldsikka.Activitys.LocationTracker;


import com.goldsikka.goldsikka.Directory.Adapters.DirectoryStoreProfileAdapter;
import com.goldsikka.goldsikka.Directory.Adapters.DirectoryStoreVideoAdapter;
import com.goldsikka.goldsikka.Fragments.Ecommerce.Adapters.PageviewecomAdapter;
import com.goldsikka.goldsikka.Models.BannersModel;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectoryStoreDetails extends AppCompatActivity {
    TextView unameTv, uidTv, titleTv;
    RelativeLayout backbtn, call, direction, website, share;
    ImageView pimg;
    Bundle bundle;
    String id = "";
    ApiDao apiDao;
    List<Listmodel> flist;
    String sid;
    String toname, mobile, latitude, longitude, pan_card, aadhar_card;

    String verifiedstatus = " ";
    Listmodel listmodel;
    String stForm, pid;
    RelativeLayout reportrl;
    /*    AppCompatRatingBar rating;
        EditText etFeedback;*/
    TextView storename, mobilenumber, locationadd, gmailtext, opentime, closetime, storeage, storeowner, viewall, enquiryform;
    String storenamee, mblnumber, emailnumber, opentimee, closetimee, storeagee;
    String strNew;
    // ArrayList<String> get_banners = new ArrayList<>();
    private Geocoder geocoder;

    DirectoryStoreimageAdapter directoryStoreimageAdapter;
    DirectoryStoreProfileAdapter directoryStoreProfileAdapter;
    DirectoryStoreVideoAdapter directoryStoreVideoAdapter;
    ArrayList<Listmodel> storeprofilelist;
    ArrayList<Listmodel> storeimagelist;
    ArrayList<Listmodel> imagevideolist;

    RecyclerView profileRV, recyclerstore, imagevideo;
    float stRating = 0;

    String locationlat, locationlong;
    LinearLayout copylayout;
    ImageView img_close, verifiedimg;
    TextView enquiryname, enquiryemail, phonenumber, storeowners, storeownername, shopname;
    EditText et_enquiryname, et_enquiry_email, et_phonenumber, et_feedback_form, et_shopname;

    double addlat, addlong;
    ImageView imageView_close, popimage, viewpagerimage;
    AlertDialog predictalertdialog;
    TextView tvpredictaccess, tvpoptitle;
    public static boolean isFromnboard = false;
    String storeusername, storepan, storeaddhar;
    String businessname, businessphone, businessemail, businessmessage;
    String enquirynamest, enquiryshopname, enquiryemailst, enquiryfeedback;
    String serverlatitude, serverlogitude;
    String get_banners;
    List<BannersModel> sliderItemList;

    RatingBar rating;
    EditText etFeedback;
    Button btn_submit;
    FrameLayout layoutframe;
    TextView emailerror, numbererror, nameerror, messageerror;
    ImageView imagelocation;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES;
    private ArrayList<String> urls = new ArrayList<>();
    boolean isAllFieldsChecked = false;
    CardView cardclick;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storedetailpage);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
            Log.e("id", "" + id);
            toname = bundle.getString("store_name");
            mobile = bundle.getString("mobile");
            latitude = bundle.getString("latitude");
            longitude = bundle.getString("longitude");
            verifiedstatus = bundle.getString("status");
            Log.e("statusss", "" + verifiedstatus);
            Log.e("mobile", "" + mobile);
            Log.e("toname", "" + toname);
        }
        init();
        LocationTracker.getInstance().fillContext(getApplicationContext());
        LocationTracker.getInstance().startLocation();
    }


    public void init() {
        uidTv = findViewById(R.id.uid);
        unameTv = findViewById(R.id.uname);
        titleTv = findViewById(R.id.title);
        uidTv.setText(AccountUtils.getCustomerID(getApplicationContext()));
        unameTv.setText(AccountUtils.getName(getApplicationContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Directory Store");
        backbtn = findViewById(R.id.backbtn);
        call = findViewById(R.id.call);
        direction = findViewById(R.id.direction);
        website = findViewById(R.id.website);
        share = findViewById(R.id.share);
        pimg = findViewById(R.id.ourbrandimg);
        recyclerstore = findViewById(R.id.recyclerstore);
        profileRV = findViewById(R.id.profileRV);
        storename = findViewById(R.id.storename);
        mobilenumber = findViewById(R.id.mobilenumber);
        locationadd = findViewById(R.id.location);
        copylayout = findViewById(R.id.copylayout);
        imagevideo = findViewById(R.id.imagevideo);
        gmailtext = findViewById(R.id.gmailtext);
        closetime = findViewById(R.id.closetime);
        opentime = findViewById(R.id.opentime);
        storeage = findViewById(R.id.storeagee);
        viewall = findViewById(R.id.viewall);
        storeowners = findViewById(R.id.storeowners);
        storeownername = findViewById(R.id.storeownername);
        verifiedimg = findViewById(R.id.verifiedimg);
        viewpagerimage = findViewById(R.id.viewpagerimage);

        layoutframe = findViewById(R.id.layoutframe);
        imagelocation = findViewById(R.id.imagelocation);
        if (verifiedstatus.equals("0")) {
            //  verifiedimg.setImageResource(R.drawable.notverifiedimage);

        } else {
            verifiedimg.setImageResource(R.drawable.ic_verify);
        }
        storeowners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    storeownerpop();

            }
        });
        enquiryform = findViewById(R.id.enquiryform);
        enquiryform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  enquiryopendialog();

            }
        });
        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DirectoryStoreDetails.this, DirectoryReviewslistActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("store_name", toname);
                startActivity(intent);
            }
        });
        reportrl = findViewById(R.id.reportrl);
        reportrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DirectoryStoreDetails.this, DirectrotyStoreReportActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);

            }
        });

        mobilenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mobile));
                Log.e("mobile ", "mobile");
                startActivity(intent);*/
            }
        });
        rating = findViewById(R.id.direrating);
        etFeedback = findViewById(R.id.et_feedback_form);
        etFeedback.setHint(Html.fromHtml(getString(R.string.enterreviewtext)));
        btn_submit = findViewById(R.id.btn_submit);

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String srate = String.valueOf(rating);
                Log.e("ratingchange", "" + srate);

                if (srate.equals("0.0")) {
                    etFeedback.setVisibility(View.GONE);
                    layoutframe.setVisibility(View.GONE);
                } else {
                    etFeedback.setVisibility(View.VISIBLE);
                    layoutframe.setVisibility(View.VISIBLE);
                }
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stForm = etFeedback.getText().toString().trim();
                stRating = rating.getRating();
                if (stForm.isEmpty()) {
                    ToastMessage.onToast(getApplicationContext(), "Please enter review form", ToastMessage.ERROR);

                } else if (stRating == 0.0) {
                    ToastMessage.onToast(getApplicationContext(), "Please give rating ", ToastMessage.ERROR);

                } else {
                    sendFeedback(etFeedback);
                }
            }
        });
        intilizerecyclerview();
        getRatingsdata();
        getstoredata();
        getstoreowner();
        predictaccesdialog();
        //     getDirectoryBanners();;

        getdcireEcomBanners();
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mobile));
                Log.e("mobile ", "mobile");
                startActivity(intent);
            }
        });
        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String originLocation = String.valueOf(LocationTracker.getInstance().currentLocation.getLatitude()) + "," + String.valueOf(LocationTracker.getInstance().currentLocation.getLongitude());
                String destinationLocation = String.valueOf(serverlatitude) + "," + String.valueOf(serverlogitude);
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + originLocation + "&daddr=" + destinationLocation + "&mode=d"));
                startActivity(intent);

            }
        });




       /* direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String latitude, logitude;
                latitude = String.valueOf(LocationTracker.getInstance().currentLocation.getLatitude());
                logitude = String.valueOf(LocationTracker.getInstance().currentLocation.getLongitude());

                String location = latitude + "," + logitude;

                serverlatitude = latitude;
                serverlogitude = logitude;

                Log.e("latitude", "" + serverlatitude);
                Log.e("longitude", "" + serverlogitude);

                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + serverlatitude + "," + serverlogitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
*/
        imagelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String originLocation = String.valueOf(LocationTracker.getInstance().currentLocation.getLatitude()) + "," + String.valueOf(LocationTracker.getInstance().currentLocation.getLongitude());
                String destinationLocation = String.valueOf(serverlatitude) + "," + String.valueOf(serverlogitude);
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + originLocation + "&daddr=" + destinationLocation + "&mode=d"));
                startActivity(intent);
            }
        });
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.goldsikka.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               share();
                //toname
                //startActivity(new Intent(getApplicationContext(), ActivityVideoView.class));

            }
        });
        cardclick = findViewById(R.id.cardclick);
        cardclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastMessage.onToast(getApplicationContext(), "fsfsfddddddcliclfff", ToastMessage.ERROR);

                startActivity(new Intent(getApplicationContext(), ActivityVideoView.class));
            }
        });
    }

    private void getstoreowner() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        } else {
            apiDao = ApiClient.getClient("").create(ApiDao.class);
            Call<List<Listmodel>> getproductslist = apiDao.getownerdetails("Bearer " + AccountUtils.getAccessToken(this));
            getproductslist.enqueue(new Callback<List<Listmodel>>() {
                @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
                @Override
                public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    dialog.dismiss();

                    Log.e("statusrandom", String.valueOf(statuscode));
                    flist = response.body();
                    if (statuscode == 200 || statuscode == 201) {
                        Log.e("storeowner", String.valueOf(statuscode));
                        if (flist != null) {
                            for (Listmodel listmodel : flist) {
//                                storeownername = listmodel.getName()
                            }
                        } else {
                            dialog.dismiss();
                            Log.e("catname", "No cats");
                        }
                    } else if (statuscode == 422) {
                        dialog.dismiss();
                        Log.e("cv", String.valueOf(statuscode));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Log.e("ughb", String.valueOf(t));
                }
            });
        }
    }

    private void storeownerpop() {
        final Dialog pdialog = new Dialog(DirectoryStoreDetails.this);
        pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pdialog.setCancelable(false);
        pdialog.setContentView(R.layout.storebusinessownerpop);
        pdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = pdialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;

        //wmlp.gravity = Gravity.BOTTOM | Gravity.END;
        btn_submit = pdialog.findViewById(R.id.btn_submit);
        img_close = pdialog.findViewById(R.id.img_close);
        enquiryname = pdialog.findViewById(R.id.enquiryname);
        et_enquiryname = pdialog.findViewById(R.id.et_enquiryname);
        enquiryemail = pdialog.findViewById(R.id.enquiryemail);
        et_enquiry_email = pdialog.findViewById(R.id.et_enquiry_email);
        phonenumber = pdialog.findViewById(R.id.phonenumber);
        et_phonenumber = pdialog.findViewById(R.id.et_phonenumber);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                storeusername = et_enquiryname.getText().toString();
                storepan = et_enquiry_email.getText().toString();
                storeaddhar = et_phonenumber.getText().toString();


                if (storeusername.isEmpty()) {
                    et_enquiryname.requestFocus();
                    et_enquiryname.setError("Enter your Name");
                } else if (storepan.isEmpty()) {
                    et_enquiry_email.requestFocus();
                    et_enquiry_email.setError("Enter your PAN Number");

                } else if (storeaddhar.isEmpty()) {
                    et_phonenumber.requestFocus();
                    et_phonenumber.setError("Enter your Addhar Number");

                } else {
                    storeownerdetails(et_enquiry_email.getText().toString(), et_phonenumber.getText().toString(), id);

                }
            }
        });


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdialog.dismiss();
            }
        });

        pdialog.show();
//        predictalertdialog.getWindow().setLayout(720, 1020);
//         pdialog.getWindow().setLayout(820, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    private void storeownerdetails(String pan, String aadhar, String id) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getplans = apiDao.poststoreowner("Bearer " + AccountUtils.getAccessToken(this), pan, aadhar, id);

            getplans.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    dialog.dismiss();
                    Log.e("ddrrrrrrowner", String.valueOf(statuscode));
                    listmodel = response.body();

                    if (statuscode == 200 || statuscode == 201) {
                        Log.e("ggbhbjhbhjreview", String.valueOf(statuscode));

                        ToastMessage.onToast(getApplicationContext(), "Business Contact details added sucessfully", ToastMessage.SUCCESS);
                    } else if (statuscode == 404) {
                        dialog.dismiss();
                        ToastMessage.onToast(getApplicationContext(), "Already Business added", ToastMessage.ERROR);

                    } else {
                        dialog.dismiss();
                        //ToastMessage.onToast(getApplicationContext(), "Technical Error", ToastMessage.ERROR);

                    }


                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                    ToastMessage.onToast(getApplicationContext(), "We have some issues ", ToastMessage.ERROR);
                }
            });
        }
    }

    public void predictaccesdialog() {
        final Dialog pdialog = new Dialog(DirectoryStoreDetails.this);
        pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pdialog.setCancelable(false);
        pdialog.setContentView(R.layout.storecontactbusinesspopup);
        pdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = pdialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;

//        wmlp.gravity = Gravity.BOTTOM | Gravity.END;
        btn_submit = pdialog.findViewById(R.id.btn_submit);
        img_close = pdialog.findViewById(R.id.img_close);

        enquiryname = pdialog.findViewById(R.id.enquiryname);
        et_enquiryname = pdialog.findViewById(R.id.et_enquiryname);
        enquiryemail = pdialog.findViewById(R.id.enquiryemail);
        et_enquiry_email = pdialog.findViewById(R.id.et_enquiry_email);
        phonenumber = pdialog.findViewById(R.id.phonenumber);
        et_phonenumber = pdialog.findViewById(R.id.et_phonenumber);
        et_feedback_form = pdialog.findViewById(R.id.et_feedback_form);

        nameerror = pdialog.findViewById(R.id.nameerror);
        emailerror = pdialog.findViewById(R.id.emailerror);
        numbererror = pdialog.findViewById(R.id.numbererror);
        messageerror = pdialog.findViewById(R.id.messageerror);


        et_enquiryname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable name) {
                businessphone = String.valueOf(name);
                Log.e("editstruing", "" + businessphone);

                if (name.toString().trim().length() > 2) {
                    nameerror.setVisibility(View.GONE);


                }

            }
        });
        et_enquiry_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable name) {
                businessemail = String.valueOf(name);
                Log.e("editstruing", "" + businessemail);

                if (name.toString().trim().length() > 2) {
                    emailerror.setVisibility(View.GONE);


                }

            }
        });
        et_phonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable name) {
                businessphone = String.valueOf(name);
                Log.e("editstruing", "" + businessphone);

                if (name.toString().trim().length() > 2) {
                    numbererror.setVisibility(View.GONE);


                }

            }
        });
        et_feedback_form.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable name) {
                businessmessage = String.valueOf(name);
                Log.e("editstruing", "" + businessmessage);

                if (name.toString().trim().length() > 2) {
                    messageerror.setVisibility(View.GONE);


                }

            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                businessvalidations(pdialog);
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdialog.dismiss();
            }
        });

        pdialog.show();


    }

    private boolean businessvalidations(final Dialog pdifffff) {

        businessname = et_enquiryname.getText().toString();
        businessemail = et_enquiry_email.getText().toString();
        businessphone = et_phonenumber.getText().toString();
        businessmessage = et_feedback_form.getText().toString();

        if (businessname.length() == 0) {
            nameerror.setVisibility(View.VISIBLE);
            nameerror.setText("Please Enter the Name");
            return false;
        }
        if (businessemail.length() == 0) {
            emailerror.setVisibility(View.VISIBLE);
            emailerror.setText("Please Enter the Email");
            return false;
        }
        if (businessphone.length() == 0) {
            numbererror.setVisibility(View.VISIBLE);
            numbererror.setText("Please Enter the PhoneNumber");
            return false;
        }
        if (businessmessage.length() == 0) {
            messageerror.setVisibility(View.VISIBLE);
            messageerror.setText("Please Enter the Message");
            return false;
        } else {

            //Businesscontact(businessname, businessemail, businessphone, businessmessage);


            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Please Wait....");
            dialog.setCancelable(false);
            dialog.show();

            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getplans = apiDao.postbusinessform("Bearer " + AccountUtils.getAccessToken(this), businessname, businessemail, businessphone, businessmessage);

            getplans.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    dialog.dismiss();
                    Log.e("ddrrrrrrowner", String.valueOf(statuscode));
                    listmodel = response.body();

                    if (statuscode == 200 || statuscode == 201) {
                        Log.e("ggbhbjhbhjreview", String.valueOf(statuscode));
                        pdifffff.dismiss();
                        ToastMessage.onToast(getApplicationContext(), "Business Contact details added sucessfully", ToastMessage.SUCCESS);
                    } else if (statuscode == 404) {
                        dialog.dismiss();
                        ToastMessage.onToast(getApplicationContext(), "Already Business added", ToastMessage.ERROR);

                    } else {
                        dialog.dismiss();
                        ToastMessage.onToast(getApplicationContext(), "Please Enter Fields", ToastMessage.ERROR);

                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                    ToastMessage.onToast(getApplicationContext(), "We have some issues ", ToastMessage.ERROR);
                }
            });


        }

        return true;


       /* if (businessname.length() > 0) {
            if (businessemail.isEmpty()) {
                if (businessphone.length() < 10) {
                    if (businessmessage.length() > 0) {

                        if (isConn()) {
                            Businesscontact(businessname, businessemail, businessphone, businessmessage);

                        } else {
                            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        messageerror.setVisibility(View.VISIBLE);
                        messageerror.setText("Please Enter the Message");
                    }

                } else {
                    numbererror.setVisibility(View.VISIBLE);
                    numbererror.setText("Please Enter the PhoneNumber");
                }

            } else {
                emailerror.setVisibility(View.VISIBLE);
                emailerror.setText("Please Enter the Email");
            }

        } else {
            nameerror.setVisibility(View.VISIBLE);
            nameerror.setText("Please Enter the Name");
        }*/
    }

    public boolean isConn() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }

    private void Businesscontact(String name, String email, String phone, String message) {
        Log.e("name", "" + name);
        Log.e("emailid", "" + email);
        Log.e("mobileno", "" + phone);
        Log.e("mobileno", "" + message);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getplans = apiDao.postbusinessform("Bearer " + AccountUtils.getAccessToken(this), name, email, phone, message);

            getplans.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    dialog.dismiss();
                    Log.e("ddrrrrrrowner", String.valueOf(statuscode));
                    listmodel = response.body();

                    if (statuscode == 200 || statuscode == 201) {
                        Log.e("ggbhbjhbhjreview", String.valueOf(statuscode));

                        ToastMessage.onToast(getApplicationContext(), "Business Contact details added sucessfully", ToastMessage.SUCCESS);
                    } else if (statuscode == 404) {
                        dialog.dismiss();
                        ToastMessage.onToast(getApplicationContext(), "Already Business added", ToastMessage.ERROR);

                    } else {
                        dialog.dismiss();
                        ToastMessage.onToast(getApplicationContext(), "Please Enter Fields", ToastMessage.ERROR);

                    }


                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                    ToastMessage.onToast(getApplicationContext(), "We have some issues ", ToastMessage.ERROR);
                }
            });
        }
    }


    public void enquiryopendialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setCancelable(false);
        View bottomSheet = LayoutInflater.from(this)
                .inflate(R.layout.addenquirypopup, findViewById(R.id.bottomsheet));
        bottomSheetDialog.setContentView(bottomSheet);
        ((View) bottomSheet.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        btn_submit = bottomSheet.findViewById(R.id.btn_submit);
        img_close = bottomSheet.findViewById(R.id.img_close);
        enquiryname = bottomSheet.findViewById(R.id.enquiryname);
        et_enquiryname = bottomSheet.findViewById(R.id.et_enquiryname);
        enquiryemail = bottomSheet.findViewById(R.id.enquiryemail);
        et_enquiry_email = bottomSheet.findViewById(R.id.et_enquiry_email);
        shopname = bottomSheet.findViewById(R.id.shopname);
        et_shopname = bottomSheet.findViewById(R.id.et_shopname);
        et_feedback_form = bottomSheet.findViewById(R.id.et_feedback_form);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enquiryvalidations();
            }
        });


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();
    }

    public void enquiryvalidations() {
        enquirynamest = et_enquiryname.getText().toString();
        enquiryemailst = et_enquiry_email.getText().toString();
        enquiryshopname = et_shopname.getText().toString();
        enquiryfeedback = et_feedback_form.getText().toString();

        et_enquiryname.setHint(Html.fromHtml(getString(R.string.user_name)));
        et_enquiry_email.setHint(Html.fromHtml(getString(R.string.user_email)));
        et_shopname.setHint(Html.fromHtml(getString(R.string.shop_name)));


        if (enquirynamest.length() == 3) {
            et_enquiryname.requestFocus();
            et_enquiryname.setError("Please Enter Your Name");
        }

        if (enquiryemailst.isEmpty()) {
            et_enquiry_email.requestFocus();
            et_enquiry_email.setError("Please Enter Your Email");
        }

        if (enquiryshopname.length() == 0) {
            et_shopname.requestFocus();
            et_shopname.setError("Please Enter Your shop name");
        }

       /* if (enquiryfeedback.isEmpty()) {
            et_feedback_form.requestFocus();
            et_feedback_form.setError("Please Enter Your Message");


        }*/
        else {
            enqueryform(enquirynamest, enquiryemailst, enquiryshopname, enquiryfeedback);

            //  Toast.makeText(getApplicationContext(), "In Development", Toast.LENGTH_SHORT).show();
        }


    }

    private void enqueryform(String email, String subject, String name, String body) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getplans = apiDao.postenquiry("Bearer " + AccountUtils.getAccessToken(this), email, subject, name, body);

            getplans.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    dialog.dismiss();
                    Log.e("ddrrrrrrowner", String.valueOf(statuscode));
                    listmodel = response.body();


                  /*  try {
                        String errorBody = response.errorBody().string();
                        Log.e("errorBody", "" + errorBody);

//                    if (!errorBody.equals(null)) {
//                        Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
//
//                    } else {
//
//                        Toast.makeText(getApplicationContext(), "File Uploaded Successfully...", Toast.LENGTH_LONG).show();
//
//                    }



                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    if (statuscode == 200 || statuscode == 201) {
                        Log.e("ggbhbjhbhjreview", String.valueOf(statuscode));

                        ToastMessage.onToast(getApplicationContext(), "Business Contact details added sucessfully", ToastMessage.SUCCESS);
                    } else if (statuscode == 404) {
                        dialog.dismiss();
                        ToastMessage.onToast(getApplicationContext(), "Already Business added", ToastMessage.ERROR);

                    } else {
                        dialog.dismiss();
                        ToastMessage.onToast(getApplicationContext(), "Technical Error", ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                    ToastMessage.onToast(getApplicationContext(), "We have some issues", ToastMessage.ERROR);
                }
            });
        }
    }

    private void getRatingsdata() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();

        } else {
            apiDao = ApiClient.getClient("").create(ApiDao.class);
            Call<List<Listmodel>> getproductslist = apiDao.getretaingsdata("Bearer " + AccountUtils.getAccessToken(this), id);
            getproductslist.enqueue(new Callback<List<Listmodel>>() {
                @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
                @Override
                public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    dialog.dismiss();

                    Log.e("statusrandom", String.valueOf(statuscode));
                    flist = response.body();
                    if (statuscode == 200 || statuscode == 201) {
                        Log.e("status", String.valueOf(statuscode));
                        if (flist != null) {
                            for (Listmodel listmodel : flist) {
                                storeprofilelist.add(listmodel);
                                directoryStoreProfileAdapter.notifyDataSetChanged();

                            }

                        } else {
                            dialog.dismiss();
                            Log.e("catname", "No cats");
                        }
                    } else if (statuscode == 422) {
                        dialog.dismiss();
                        Log.e("cv", String.valueOf(statuscode));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                    //  dialog.dismiss();

                    Log.e("ratingggggggughb", String.valueOf(t));
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  getRatingsdata();
    }

    public void getstoredata() {
        Log.e("id", "" + id);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getplans = apiDao.getstoredataa("Bearer " + AccountUtils.getAccessToken(this), id);

            getplans.enqueue(new Callback<Listmodel>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    dialog.dismiss();
                    Log.e("StatusCOdeForm", String.valueOf(statuscode));
                    listmodel = response.body();
                    if (statuscode == 200 || statuscode == 201) {
                        Log.e("statuscode", String.valueOf(statuscode));
                        List<Listmodel> list = Collections.singletonList(response.body());
                        if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_CREATED) {

                            if (list != null) {
                                for (Listmodel listmodel : list) {
                                    storenamee = listmodel.getStore_name();
                                    storename.setText("Name : " + storenamee);
                                    mblnumber = listmodel.getMobile();
                                    mobilenumber.setText(mblnumber);
                                    strNew = mblnumber.replaceFirst("91", "");
                                    mblnumber = listmodel.getMobile();
                                    mobilenumber.setText(strNew);
                                    emailnumber = listmodel.getEmail();
                                    gmailtext.setText(emailnumber);
                                    opentimee = listmodel.getStore_open_timings();
                                    closetimee = listmodel.getStore_close_timings();
                                    opentime.setText(opentimee);
                                    closetime.setText(closetimee);
                                    storeagee = listmodel.getAge_of_store();
                                    storeage.setText(storeagee + " years");
                                    serverlatitude = listmodel.getLatitude();
                                    serverlogitude = listmodel.getLongitude();
                                    String loadviewimage = listmodel.getImage();

                                    if (listmodel.getImage() != null) {

                                        Picasso.with(getApplicationContext()).load(loadviewimage).into(viewpagerimage);

                                        try {
                                            Glide.with(getApplicationContext()).load(loadviewimage).into(viewpagerimage);
                                        } catch (Exception ignored) {
                                            Glide.with(getApplicationContext()).load(R.drawable.background_image).into(viewpagerimage);
                                        }

                                    } else {
                                        Picasso.with(getApplicationContext()).load(R.drawable.background_image).into(viewpagerimage);
                                    }

                                    try {
                                        String _24HourTime = closetimee;
                                        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                                        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                                        Date _24HourDt = _24HourSDF.parse(_24HourTime);
                                        closetime.setText(_12HourSDF.format(_24HourDt));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        String _24HourTime = opentimee;
                                        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                                        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                                        Date _24HourDt = _24HourSDF.parse(_24HourTime);
                                        opentime.setText(_12HourSDF.format(_24HourDt));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    // initpager();

                                    storeimagelist.add(listmodel);
                                    directoryStoreimageAdapter.notifyDataSetChanged();

                                    locationlat = listmodel.getLatitude();
                                    locationlong = listmodel.getLongitude();
                                    NumberFormat f = NumberFormat.getInstance(); // Gets a NumberFormat with the default locale, you can specify a Locale as first parameter (like Locale.FRENCH)
                                    NumberFormat ff = NumberFormat.getInstance(); // Gets a NumberFormat with the default locale, you can specify a Locale as first parameter (like Locale.FRENCH)
                                    double storelocation = 0.0;
                                    double storelocation1 = 0.0;
                                    try {
                                        storelocation = f.parse(locationlat).doubleValue(); // myNumber now contains 20
                                        Log.e("latitudeloc", "" + storelocation);
                                        storelocation1 = ff.parse(locationlong).doubleValue(); // myNumber now contains 20
                                        Log.e("longitudeloc", "" + storelocation1);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        Geocoder geo = new Geocoder(DirectoryStoreDetails.this.getApplicationContext(), Locale.getDefault());
                                        List<Address> addresses = geo.getFromLocation(storelocation, storelocation1, 1);

                                        //List<Address> addresses = geo.getFromLocation(17.445149235635782, 78.46129640918444, 1);
                                        Log.e("locationaddressssssssss", "" + addresses);
                                        locationadd.setText(addresses.get(0).getAddressLine(0));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    copylayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                            ClipData clipData = ClipData.newPlainText("text", locationadd.getText());
                                            manager.setPrimaryClip(clipData);
                                            Toast.makeText(DirectoryStoreDetails.this, "copied", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    } else if (statuscode == 404) {
                        dialog.dismiss();
                        ToastMessage.onToast(getApplicationContext(), "Already the rating has given", ToastMessage.ERROR);

                    } else {
                        dialog.dismiss();
                        ToastMessage.onToast(getApplicationContext(), "Technical Error", ToastMessage.ERROR);

                    }

                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                    ToastMessage.onToast(getApplicationContext(), "We have some issues ", ToastMessage.ERROR);
                }
            });

        }
    }

    public void sendFeedback(EditText etback) {

        Log.e("id", "" + id);
        Log.e("stRating", "" + stRating);
        String convert = String.valueOf(stRating);

        String sensrate = convert.substring(0, convert.length() - 2);
        Log.e("rate", "" + sensrate);


        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getplans = apiDao.poststorereview("Bearer " + AccountUtils.getAccessToken(this), id, sensrate, stForm);

            getplans.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    dialog.dismiss();
                    Log.e("StatusCOdeForm", String.valueOf(statuscode));
                    listmodel = response.body();


                    if (statuscode == 200 || statuscode == 201) {
                        Log.e("statuscode", String.valueOf(statuscode));
                        ToastMessage.onToast(getApplicationContext(), "Thanks for your feedback", ToastMessage.SUCCESS);

                        etFeedback.setText("");
                        rating.setRating(0.0F);
                        etFeedback.setVisibility(View.GONE);
                        layoutframe.setVisibility(View.GONE);
                        //    getRatingsdata();
                        //  etback.setVisibility(View.GONE);
                    } else if (statuscode == 404) {
                        dialog.dismiss();
                        ToastMessage.onToast(getApplicationContext(), "Already the rating has given", ToastMessage.ERROR);

                    } else if (statuscode == 422) {
                        dialog.dismiss();
                        ToastMessage.onToast(getApplicationContext(), "Please Enter Feedback Text ", ToastMessage.ERROR);

                    } else {
                        dialog.dismiss();
                        ToastMessage.onToast(getApplicationContext(), "Technical Error", ToastMessage.ERROR);

                    }


                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                    ToastMessage.onToast(getApplicationContext(), "We have some issues ", ToastMessage.ERROR);
                }
            });

        }
    }

    public void getDirectoryBanners() {

        sliderItemList.clear();
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(getApplicationContext())).create(ApiDao.class);
        Call<List<Listmodel>> getdetails = apiDao.get_directoriesstoreimages("Bearer " + AccountUtils.getAccessToken(this), id);

        getdetails.enqueue(new Callback<List<Listmodel>>() {
            @Override
            public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                int statuscoe = response.code();
                Log.e("bannerStatuscode", String.valueOf(statuscoe));
                if (statuscoe == HttpsURLConnection.HTTP_OK || statuscoe == HttpsURLConnection.HTTP_CREATED) {
                    List<Listmodel> list = response.body();
                    sliderItemList.clear();
                    if (list != null) {
                        for (Listmodel listmodel : list) {
                            get_banners = listmodel.getImage();
                            Log.e("Banners", "" + get_banners);
                            init();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No Images", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                // Toast.makeText(activity, "onFailure", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void getdcireEcomBanners() {

        sliderItemList.clear();
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(getApplicationContext())).create(ApiDao.class);
        Call<List<Listmodel>> getdetails = apiDao.get_directoriesstoreimages("Bearer " + AccountUtils.getAccessToken(this), id);


        getdetails.enqueue(new Callback<List<Listmodel>>() {
            @Override
            public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                int statuscoe = response.code();
                Log.e("bannerStatuscode", String.valueOf(statuscoe));
                if (statuscoe == HttpsURLConnection.HTTP_OK || statuscoe == HttpsURLConnection.HTTP_CREATED) {
                    List<Listmodel> list = response.body();
                    sliderItemList.clear();

                    if (list != null) {
                        for (Listmodel listmodel : list) {
                            get_banners = listmodel.getImages();
                            Log.e("Banners", "" + get_banners);
                            initee();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No Images", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                // Toast.makeText(activity, "onFailure", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initee() {
        float density = 0;
        String[] image = new String[]{get_banners};
        for (int i = 0; i < image.length; i++) {
            Log.e("bannerurl", String.valueOf(urls));
            urls.add(image[i]);
        }


        mPager.setAdapter(new PageviewecomAdapter(getApplicationContext(), urls));

        CirclePageIndicator indicator = findViewById(R.id.circleindicator);
        indicator.setViewPager(mPager);
        try {
            density = getResources().getDisplayMetrics().density;
        } catch (Exception e) {
            density = 0;
        }
        //Set circle indicator radius
        indicator.setRadius(4 * density);
        NUM_PAGES = urls.size();
        Log.e("pages", String.valueOf(NUM_PAGES));

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                Log.e("currentposition", "call" + position);
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {


            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }


    private void initpager() {
        float density = 0;
        String[] image = new String[]{get_banners};
        for (int i = 0; i < image.length; i++) {
            Log.e("bannerurl", String.valueOf(urls));
            urls.add(image[i]);
        }

        mPager.setAdapter(new PageviewecomAdapter(getApplicationContext(), urls));

        CirclePageIndicator indicator = findViewById(R.id.circleindicator);

        indicator.setViewPager(mPager);
        try {
            density = getResources().getDisplayMetrics().density;
        } catch (Exception e) {
            density = 0;
        }
        //Set circle indicator radius
        indicator.setRadius(4 * density);
        NUM_PAGES = urls.size();
        Log.e("pages", String.valueOf(NUM_PAGES));

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                Log.e("currentposition", "call" + position);
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {


            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    public void getphotos() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        Call<List<Listmodel>> getproductsbycat = null;
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);

        getproductsbycat = apiDao.get_ecomTopSellinglist();
        getproductsbycat.enqueue(new Callback<List<Listmodel>>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NotNull Call<List<Listmodel>> call, @NotNull Response<List<Listmodel>> response) {
                int statuscode = response.code();
                Log.e("topsellstatuscode", String.valueOf(statuscode));
                flist = response.body();
                dialog.dismiss();
                if (statuscode == 200 || statuscode == 201) {
                    Log.e("topsellstatuscode1", String.valueOf(statuscode));
                    if (flist != null) {
                        for (Listmodel listmodel : flist) {

                            Log.e("catname", "No cats");

                        }
                    } else {
                    }
                } else if (statuscode == 422) {
                    dialog.dismiss();
                    Log.e("cv", String.valueOf(statuscode));
                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e("ughb", String.valueOf(t));
            }
        });
    }

    private void intilizerecyclerview() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        profileRV.setLayoutManager(linearLayoutManager);
        profileRV.setHasFixedSize(true);
        storeprofilelist = new ArrayList<>();
        directoryStoreProfileAdapter = new DirectoryStoreProfileAdapter(this, storeprofilelist);
        profileRV.setAdapter(directoryStoreProfileAdapter);
        sliderItemList = new ArrayList<>();


        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerstore.setLayoutManager(linearLayoutManager1);
        recyclerstore.setHasFixedSize(true);
        storeimagelist = new ArrayList<>();
        directoryStoreimageAdapter = new DirectoryStoreimageAdapter(storeimagelist, this);
        recyclerstore.setAdapter(directoryStoreimageAdapter);

        mPager = (ViewPager) findViewById(R.id.pager);

    }

    public void share() {
        String title = toname;
        String content = "http://www.goldsikka.com";
        String titleAndContent = "Shop Name: " + title + "\n Shop Website: " + content;

        Intent intentShare = new Intent();
        intentShare.setAction(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_TEXT, titleAndContent);
        startActivity(intentShare);

        /*Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = toname;

        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));
*/
    }

    public class DirectoryStoreimageAdapter extends RecyclerView.Adapter<DirectoryStoreimageAdapter.ViewHolder> {
        private Context context;
        ArrayList<Listmodel> storeimagelist;

        public DirectoryStoreimageAdapter(ArrayList<Listmodel> storeimagelist, Context context) {
            this.context = context;
            this.storeimagelist = storeimagelist;

        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.activity_store_image_adapter, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Listmodel listmodel = storeimagelist.get(position);

            String image = listmodel.getImage();


            Log.e("lmmmmm", "" + listmodel.getPname());
            Log.e("ddddddm", "" + listmodel.getPimg());


            if (listmodel.getImage() != null) {

                Picasso.with(context.getApplicationContext()).load(image).into(holder.iv_categoryimg);

                try {
                    Picasso.with(context).load(image).into(holder.iv_categoryimg);
                } catch (Exception ignored) {
                    Picasso.with(context).load(R.drawable.background_image).into(holder.iv_categoryimg);
                }

            } else {
                Picasso.with(context).load(R.drawable.background_image).into(holder.iv_categoryimg);
            }

        }

        @Override
        public int getItemCount() {
            return storeimagelist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView iv_categoryimg;
            TextView tv_category, topproduc, topprice;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);


                iv_categoryimg = itemView.findViewById(R.id.topsellimg);
                iv_categoryimg.setOnClickListener(this);
                itemView.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
//                itemClickListener.onItemClick( v, getAdapterPosition() );
            }
        }

    }

}
