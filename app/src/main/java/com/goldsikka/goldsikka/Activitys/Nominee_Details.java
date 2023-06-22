package com.goldsikka.goldsikka.Activitys;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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

import com.goldsikka.goldsikka.Fragments.Nominee_details_fragment;
import com.goldsikka.goldsikka.MainActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.model.data;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Nominee_Details extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llbutton)
    LinearLayout llbutton;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_nomineedelete)
    TextView iv_nomineedelete;
    //GifImageView loading_gif;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvmobileno)
    TextView tvmobileno;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvname)
    TextView tvname;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvrelation)
    TextView tvrelation;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvaddress)
    TextView tvaddress;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvcity)
    TextView tvcity;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvstate)
    TextView tvstate;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvcountry)
    TextView tvcountry;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvpincode)
    TextView tvpincode;

    String st_nominee_name, st_nominee_mobile, st_nominee_address, st_nominee_relation, st_nominee_city, st_nominee_state, st_nomenee_pincode, st_nominee_country;
    ApiDao apiDao;
    String nomineeid;

    TextView unameTv, uidTv, titleTv;

    RelativeLayout backbtn;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nominee__details);
        setContentView(R.layout.activity_nominee__details);

        ButterKnife.bind(this);

        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        backbtn = findViewById(R.id.backbtn);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Nominee Details");

        //  loading_gif = findViewById(R.id.loading_gif);
        get_nominee_details();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_nominee_details();
    }

    private void get_nominee_details() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<data> getnominee_details = apiDao.get_nominee_details("Bearer " + AccountUtils.getAccessToken(this));
            getnominee_details.enqueue(new Callback<data>() {
                @Override
                public void onResponse(Call<data> call, Response<data> response) {
                    int statuscode = response.code();
                    List<Listmodel> list = response.body().getResult();
                    if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_CREATED
                            || statuscode == HttpsURLConnection.HTTP_ACCEPTED) {
                        if (list.size() != 0) {
                            for (Listmodel listmodel : list) {
                                dialog.dismiss();
                                nomineeid = listmodel.getId();
                                if (nomineeid.equals("") || nomineeid == null) {
                                    llbutton.setVisibility(View.VISIBLE);
                                    linearlayout.setVisibility(View.VISIBLE);
                                }
                                st_nominee_name = listmodel.getName();
                                st_nominee_address = listmodel.getAddress();
                                st_nominee_mobile = listmodel.getPhone();
                                st_nominee_city = listmodel.getCity();
                                st_nominee_relation = listmodel.getRelation();
                                st_nominee_state = listmodel.getState_id();
                                st_nominee_country = listmodel.getCountry_id();
                                st_nomenee_pincode = listmodel.getZip();
                                JsonElement liststate = listmodel.getState();
                                if (liststate == null) {
                                    Log.e("jsonelement", "jsonelement error");
                                } else {
                                    try {
                                        JsonObject from = new JsonParser().parse(listmodel.getState().toString()).getAsJsonObject();
                                        JSONObject json_from = new JSONObject(from.toString());
                                        st_nominee_state = json_from.getString("name");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

//                                String listcountry = listmodel.getCountry_id();
//
//                                if (listcountry.equals(null)) {
//                                    Log.e("jsonelement", "jsonelement error");
//                                } else {
//
//                                    try {
//                                        JsonObject from = new JsonParser().parse(listmodel.getCountry_id()).getAsJsonObject();
//                                        JSONObject json_from = new JSONObject(from.toString());
//                                        st_nominee_state = json_from.getString("name");
//
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }


                                if (st_nominee_name == null) {
                                    tvname.setText(R.string.Nominee_Name);
                                    linearlayout.setVisibility(View.VISIBLE);
                                    // llbutton.setVisibility(View.VISIBLE);
                                }
//                                if (st_nominee_address == null) {
//                                    tv_nominee_address.setText("xxxxxx");
//                                    linearlayout.setVisibility(View.VISIBLE);
//                                }
//                                if (st_nominee_mobile == null) {
//                                    tv_nominee_mobile.setText("xxxxxx");
//                                    linearlayout.setVisibility(View.VISIBLE);
//                                }
//                                if (st_nominee_city == null) {
//                                    tv_nominee_city.setText("xxxxxx");
//                                    linearlayout.setVisibility(View.VISIBLE);
//                                }
                                if (st_nominee_relation == null) {
                                    tvrelation.setText(R.string.Relation);
                                    linearlayout.setVisibility(View.VISIBLE);
                                }
//                            if (st_nominee_state == null) {
//                                tv_nominee_state.setText("xxxxxx");
//                                linearlayout.setVisibility(View.VISIBLE);
//                                // llbutton.setVisibility(View.VISIBLE);
//                            }
                                else {
                                    // llbutton.setVisibility(View.GONE);
                                    linearlayout.setVisibility(View.GONE);

                                    set_nominee_details();
                                }
                            }
                        } else {
                            dialog.dismiss();
                            llbutton.setVisibility(View.VISIBLE);
                            linearlayout.setVisibility(View.VISIBLE);
                            // ToastMessage.onToast(Nominee_Details.this, "Please Add the Nominee.", ToastMessage.ERROR);
                        }

                    } else {
                        dialog.dismiss();
                        llbutton.setVisibility(View.VISIBLE);
                        linearlayout.setVisibility(View.VISIBLE);
                        // ToastMessage.onToast(Nominee_Details.this, "Please Add the Nominee.", ToastMessage.ERROR);
                        Log.e("error code", String.valueOf(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<data> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("naminee fail", t.toString());
                    ToastMessage.onToast(Nominee_Details.this, "We Have Some Issues", ToastMessage.ERROR);
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    public void set_nominee_details() {
        tvname.setText(": " + st_nominee_name);
        tvmobileno.setText(": " + st_nominee_mobile);
        tvrelation.setText(": " + st_nominee_relation);
        tvaddress.setText(": " + st_nominee_address);
        tvcity.setText(": " + st_nominee_city);
        tvstate.setText(": " + st_nominee_state);
        tvcountry.setText(": " + st_nominee_country);
        tvpincode.setText(": " + st_nomenee_pincode);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.bt_add_nominee)
    public void add_nominee() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        } else {
            Intent intent = new Intent(this, Nominee_details_fragment.class);
            startActivity(intent);
        }

    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.iv_nomineedelete)
    public void initremove() {
        iv_nomineedelete.setVisibility(View.GONE);
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            iv_nomineedelete.setVisibility(View.GONE);
            //  loading_gif.setVisibility(View.VISIBLE);
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> deletenominee = apiDao.delete_nominee_details("Bearer " + AccountUtils.getAccessToken(this), nomineeid);
            deletenominee.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Listmodel listmodel = response.body();
                    if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_NO_CONTENT) {
                        ToastMessage.onToast(Nominee_Details.this, "Successfully deleted", ToastMessage.SUCCESS);
                        //       loading_gif.setVisibility(View.GONE);
                        linearlayout.setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    //loading_gif.setVisibility(View.GONE);
                    iv_nomineedelete.setVisibility(View.VISIBLE);
                    ToastMessage.onToast(Nominee_Details.this, "We have some issues", ToastMessage.ERROR);
                }
            });

        }
    }
}