package com.goldsikka.goldsikka.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.goldsikka.goldsikka.Activitys.Edit_CustomerAddress;
import com.goldsikka.goldsikka.Activitys.Nominee_Details;
import com.goldsikka.goldsikka.Activitys.Profile_Details;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Nominee_details_fragment extends AppCompatActivity implements View.OnClickListener {


    EditText et_nominee_name, et_nominy_mobile, et_nominee_address, et_nominee_city, et_pincode;
    TextView tv_nominee_name, tv_nominy_mobile, tv_relation, tv_address, tv_nominee_city, tv_pin, tv_state, tv_nominee_country;
    Button btn_addnominee;

    Spinner spin_state, spin_country, spin_relation;
    ArrayList<String> statelist, country_list, relationlist;
    String st_statelist, st_countrylist, relationtype;
    String state_id, country_id;
    String state, stcountry;

    //GifImageView loading_gif;
    String st_nominename, st_nomineemobile, st_nomineeaddress, st_pin, strelation, st_city;
    List<Listmodel> list;
    ApiDao apiDao;

    String[] str_relation = {"Relation", "Father", "Mother", "Sister", "Brother", "Daughter", "Son", "Spouse", "Cousin", "GrandParents", "Nephews", "Nieces", "sibling-in-law"};
    RelativeLayout backbtn;

    TextView unameTv, uidTv, titleTv;

    @SuppressLint("NewApi")
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nominee_details_fragment);

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
        titleTv.setText("Add Nominee Details");
        intilizeviews();
        setHint();

    }

    public void setHint() {
        et_nominee_name.setHint(Html.fromHtml(getString(R.string.Nominee_Name)));
        et_nominee_address.setHint(Html.fromHtml(getString(R.string.Address)));
        et_nominee_city.setHint(Html.fromHtml(getString(R.string.City)));
        et_nominy_mobile.setHint(Html.fromHtml(getString(R.string.Nomine_Mobile_Number)));
        et_pincode.setHint(Html.fromHtml(getString(R.string.Zip_Code)));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void intilizeviews() {


        et_nominee_name = findViewById(R.id.et_nominee_name);
        et_nominy_mobile = findViewById(R.id.et_nominy_mobile);
        et_nominee_address = findViewById(R.id.et_nominee_address);
        et_nominee_city = findViewById(R.id.et_nominee_city);
        et_pincode = findViewById(R.id.et_pincode);

        tv_nominee_name = findViewById(R.id.tv_nominee_name);
        tv_nominy_mobile = findViewById(R.id.tv_nominy_mobile);
        tv_relation = findViewById(R.id.tv_relation);
        tv_address = findViewById(R.id.tv_address);
        tv_nominee_city = findViewById(R.id.tv_nominee_city);
        tv_pin = findViewById(R.id.tv_pin);
        tv_state = findViewById(R.id.tv_state);
        tv_nominee_country = findViewById(R.id.tv_nominee_country);

        btn_addnominee = findViewById(R.id.btn_addnominee);
        btn_addnominee.setOnClickListener(this::onClick);

        spin_relation = findViewById(R.id.et_relation);
        ArrayAdapter spinner_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, str_relation);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_relation.setAdapter(spinner_adapter);
        spinnerclick();

        //  loading_gif = findViewById(R.id.loading_gif);
        spin_state = findViewById(R.id.spin_state);
        get_statelist();
        spin_country = findViewById(R.id.spin_country);
        get_countrylist();
    }

    public void spinnerclick() {
        relationlist = new ArrayList<>();
        spin_relation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  st_relation = spin_relation.getItemAtPosition(spin_relation.getSelectedItemPosition() - 1).toString();
                //spinner_signuptype.getSelectedView().setTextColor(getResources().getColor(R.color.Blue));
                strelation = String.valueOf(spin_relation.getSelectedItem());
                ((TextView) view).setTextColor(ContextCompat.getColor(Nominee_details_fragment.this, R.color.DarkBrown));

                switch (strelation) {
                    case "Father":
                        relationtype = "FT";

                        break;
                    case "Mother":
                        relationtype = "MT";

                        break;
                    case "Sister":
                        relationtype = "ST";

                        break;
                    case "Brother":
                        relationtype = "BR";

                        break;
                    case "Daughter":
                        relationtype = "DT";

                        break;
                    case "Son":
                        relationtype = "SO";

                        break;
                    case "Spouse":
                        relationtype = "SP";

                        break;
                    case "Cousin":
                        relationtype = "CU";

                        break;
                    case "GrandParents":
                        relationtype = "GP";

                        break;
                    case "Nephews":
                        relationtype = "NE";

                        break;
                    case "Nieces":
                        relationtype = "NI";

                        break;
                    case "sibling-in-law":
                        relationtype = "SIL";

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
    }

    public void get_statelist() {
        statelist = new ArrayList<>();

        load_state_data();

        spin_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @SuppressLint("NewApi")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setTextColor(ContextCompat.getColor(Nominee_details_fragment.this, R.color.DarkBrown));

                st_statelist = spin_state.getItemAtPosition(spin_state.getSelectedItemPosition()).toString();

                if (!st_statelist.equals("Select State")) {
                    Listmodel listmodel = list.get(i);
                    state_id = listmodel.getId();
//                    for (Listmodel listmodel:list){
                    Log.e("stateid..", state_id);
                    state = listmodel.getName();
                    Log.e("state..", st_statelist);

                } else {
                    //   ToastMessage.onToast(Nominee_details_fragment.this, "Please Select State", ToastMessage.ERROR);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

    }

    public void load_state_data() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<List<Listmodel>> get_address = apiDao.get_states("Bearer " + AccountUtils.getAccessToken(this));
            get_address.enqueue(new Callback<List<Listmodel>>() {
                @Override
                public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    list = (response.body());
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        for (Listmodel listmodel : list) {
                            dialog.dismiss();
                            //  Collections.sort(statelist);


                            statelist.add(listmodel.getName());
                        }
                        spin_state.setAdapter(new ArrayAdapter<String>(Nominee_details_fragment.this,
                                android.R.layout.simple_spinner_dropdown_item, statelist));

                    }
                }


                @Override
                public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                   // ToastMessage.onToast(Nominee_details_fragment.this, "We Have Some Issues", ToastMessage.ERROR);
                    dialog.dismiss();
                }
            });
        }

    }

    public void get_countrylist() {
        country_list = new ArrayList<>();


        load_country_data();
        spin_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @SuppressLint("NewApi")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setTextColor(ContextCompat.getColor(Nominee_details_fragment.this, R.color.DarkBrown));

                st_countrylist = spin_country.getItemAtPosition(spin_country.getSelectedItemPosition()).toString();

                // String listmodel = statelist.get();
                if (!st_countrylist.equals("Select Country")) {
                    Listmodel listmodel = list.get(i);
                    country_id = listmodel.getId();
//                    ToastMessage.onToast(Nominee_details_fragment.this, country_id, ToastMessage.SUCCESS);
                    stcountry = listmodel.getName();
//                    for (Listmodel listmodel:list){
                    Log.e("stateid..", country_id);

                    Log.e("state..", st_countrylist);

                } else {
                    //   ToastMessage.onToast(Nominee_details_fragment.this, "Please Select Country", ToastMessage.ERROR);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

    }

    public void load_country_data() {

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
            Call<List<Listmodel>> get_address = apiDao.getcountries("Bearer " + AccountUtils.getAccessToken(this));
            get_address.enqueue(new Callback<List<Listmodel>>() {
                @Override
                public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    list = (response.body());
                    if (statuscode == HttpsURLConnection.HTTP_OK) {
                        for (Listmodel listmodel : list) {
                            dialog.dismiss();
                            //  Collections.sort(country_list);
                            country_list.add(listmodel.getName());
                        }
                        spin_country.setAdapter(new ArrayAdapter<String>(Nominee_details_fragment.this, android.R.layout.simple_spinner_dropdown_item, country_list));

                    }
                }


                @Override
                public void onFailure(Call<List<Listmodel>> call, Throwable t) {
                 //   ToastMessage.onToast(Nominee_details_fragment.this, "We Have Some Issues", ToastMessage.ERROR);
                    dialog.dismiss();
                }
            });
        }
    }

    public void initvalidation() {

        btn_addnominee.setVisibility(View.GONE);
        //loading_gif.setVisibility(View.VISIBLE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {

                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Nominee_details_fragment.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        validations();
                        btn_addnominee.setVisibility(View.VISIBLE);
                        // loading_gif.setVisibility(View.GONE);
                    }
                });
            }
        }, 500);
    }

    public void validations() {

        st_nominename = et_nominee_name.getText().toString();
        st_nomineemobile = et_nominy_mobile.getText().toString();
        st_nomineeaddress = et_nominee_address.getText().toString();
        st_pin = et_pincode.getText().toString();
        st_city = et_nominee_city.getText().toString();


        tv_nominee_name.setVisibility(View.GONE);
        tv_nominy_mobile.setVisibility(View.GONE);
        tv_nominee_city.setVisibility(View.GONE);
        tv_address.setVisibility(View.GONE);
        tv_pin.setVisibility(View.GONE);
        tv_state.setVisibility(View.GONE);
        tv_nominee_country.setVisibility(View.GONE);

        if (st_nominename.isEmpty()) {
            tv_nominee_name.setVisibility(View.VISIBLE);
            tv_nominee_name.setText("Please enter the name");
        } else if (st_nomineemobile.isEmpty()) {
            tv_nominy_mobile.setText("Please enter the valid phone number ");
            tv_nominy_mobile.setVisibility(View.VISIBLE);
        } else if (strelation.equals("Relation")) {
            ToastMessage.onToast(Nominee_details_fragment.this, "plaese select the relation", ToastMessage.ERROR);
        } else if (st_nomineeaddress.isEmpty()) {
            tv_address.setText("please enter the Address");
            tv_address.setVisibility(View.VISIBLE);
        }
//    else if (state.equals("Select State")){
//        ToastMessage.onToast(Nominee_details_fragment.this, "Please Select State", ToastMessage.ERROR);
//    }
//    else if (stcountry.equals("Select Country")){
//        ToastMessage.onToast(Nominee_details_fragment.this, "Please Select Country", ToastMessage.ERROR);
//    }

        else if (st_city.isEmpty()) {
            tv_nominee_city.setVisibility(View.VISIBLE);
            tv_nominee_city.setText("Please select the city");
        } else if (st_pin.isEmpty()) {
            tv_pin.setVisibility(View.VISIBLE);
            tv_pin.setText("Please enter the ZIP Code");
        } else {
            init_addnominee();
        }


    }

    public void init_addnominee() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            dialog.dismiss();
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getnominee = apiDao.get_nominee("Bearer " + AccountUtils.getAccessToken(this),
                    st_nominename, st_nomineemobile, st_nomineeaddress, country_id, state_id, st_city, st_pin, relationtype);
            getnominee.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Listmodel listmodel = response.body();
                    if (statuscode == HttpsURLConnection.HTTP_CREATED || statuscode == HttpsURLConnection.HTTP_OK) {

                        AccountUtils.setNomineeID(Nominee_details_fragment.this, listmodel.getId());
                        ToastMessage.onToast(Nominee_details_fragment.this, "Successfully updated", ToastMessage.SUCCESS);
//                    Intent intent = new Intent(Nominee_details_fragment.this, Nominee_Details.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                    startActivity(intent);
                        finish();
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        try {
                            tv_nominee_name.setVisibility(View.GONE);
                            tv_nominy_mobile.setVisibility(View.GONE);
                            tv_nominee_city.setVisibility(View.GONE);
                            tv_address.setVisibility(View.GONE);
                            tv_pin.setVisibility(View.GONE);
                            tv_state.setVisibility(View.GONE);
                            tv_nominee_country.setVisibility(View.GONE);

                            assert response.errorBody() != null;
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String st = jsonObject.getString("message");
//                        ToastMessage.onToast(Nominee_details_fragment.this, st, ToastMessage.ERROR);
                            JSONObject er = jsonObject.getJSONObject("errors");
                            try {
                                JSONArray array_mobile = er.getJSONArray("name");
                                for (int i = 0; i < array_mobile.length(); i++) {
                                    //Toast.makeText(this, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                    tv_nominee_name.setVisibility(View.VISIBLE);
                                    tv_nominee_name.setText(array_mobile.getString(i));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray array_mobile = er.getJSONArray("phone");
                                for (int i = 0; i < array_mobile.length(); i++) {
                                    //Toast.makeText(this, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                    tv_nominy_mobile.setVisibility(View.VISIBLE);
                                    tv_nominy_mobile.setText(array_mobile.getString(i));

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray array_mobile = er.getJSONArray("address");
                                for (int i = 0; i < array_mobile.length(); i++) {
                                    //Toast.makeText(this, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                    tv_address.setVisibility(View.VISIBLE);
                                    tv_address.setText(array_mobile.getString(i));

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray array_mobile = er.getJSONArray("city");
                                for (int i = 0; i < array_mobile.length(); i++) {
                                    //Toast.makeText(this, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                    tv_nominee_city.setVisibility(View.VISIBLE);
                                    tv_nominee_city.setText(array_mobile.getString(i));

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray array_mobile = er.getJSONArray("zip");
                                for (int i = 0; i < array_mobile.length(); i++) {
                                    //Toast.makeText(this, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                    tv_pin.setVisibility(View.VISIBLE);
                                    tv_pin.setText(array_mobile.getString(i));

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                JSONArray array_mobile = er.getJSONArray("country_id");
                                for (int i = 0; i < array_mobile.length(); i++) {


                                    //Toast.makeText(this, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                    tv_nominee_country.setVisibility(View.VISIBLE);
                                    tv_nominee_country.setText(array_mobile.getString(i));

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray array_mobile = er.getJSONArray("state_id");
                                for (int i = 0; i < array_mobile.length(); i++) {
                                    //Toast.makeText(this, array_mobile.getString(i), Toast.LENGTH_SHORT).show();
                                    tv_state.setVisibility(View.VISIBLE);
                                    tv_state.setText(array_mobile.getString(i));

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                  //  ToastMessage.onToast(Nominee_details_fragment.this, "We have some issues", ToastMessage.ERROR);
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addnominee:
                if (!NetworkUtils.isConnected(this)) {
                    ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
                    return;
                } else {
                    initvalidation();
                }
                break;

        }

    }
}
