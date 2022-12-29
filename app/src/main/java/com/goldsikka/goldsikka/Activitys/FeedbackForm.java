package com.goldsikka.goldsikka.Activitys;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goldsikka.goldsikka.Models.Enquiryformmodel;
import com.goldsikka.goldsikka.NewDesignsActivity.MainFragmentActivity;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.List_Model;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackForm extends AppCompatActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_feedback_form)
    EditText etFeedback;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rating)
    RatingBar rating;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    String stForm;
    float stRating = 0;

    ApiDao apiDao;
    Listmodel list;
TextView unameTv, uidTv, titleTv;

    RelativeLayout backbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form);

        ButterKnife.bind(this);

        etFeedback.setHint(Html.fromHtml(getString(R.string.feedback_here)));

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
        titleTv.setText("Feedback Form");

        InputFilter[] editFilters = etFeedback.getFilters();
        InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
        System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
        newFilters[editFilters.length] = new InputFilter.LengthFilter(250);
        etFeedback.setFilters(newFilters);
    }

    @Override
    public void onBackPressed() {
        //NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
         finish();
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @OnClick(R.id.btn_submit)
    public void BtnSubmit(){

        stForm = etFeedback.getText().toString().trim();
        stRating = rating.getRating();
        if (stForm.isEmpty()){
            ToastMessage.onToast(this,"Please enter feedback form",ToastMessage.ERROR);

        }else if (stRating == 0.0){
            ToastMessage.onToast(this,"Please rate ",ToastMessage.ERROR);

        }else {
            sendFeedback();
        }
    }
    public void sendFeedback(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            dialog.dismiss();
            return;
        }else {
            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
            Call<Listmodel> getplans = apiDao.feedbackForm("Bearer "+AccountUtils.getAccessToken(this),stForm, String.valueOf(stRating));

            getplans.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    Log.e("Status COde Form", String.valueOf(statuscode));
                    list = response.body();
                    if (statuscode == HttpsURLConnection.HTTP_CREATED){

                        dialog.dismiss();
                        ToastMessage.onToast(FeedbackForm.this,list.getMessage(),ToastMessage.SUCCESS);
                        Intent intent = new Intent(FeedbackForm.this, MainFragmentActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else {
                        dialog.dismiss();
                     //   ToastMessage.onToast(FeedbackForm.this,"Technical Error",ToastMessage.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                  //  ToastMessage.onToast(FeedbackForm.this,"We ahve some issues ",ToastMessage.ERROR);
                }
            });

        }
    }
}