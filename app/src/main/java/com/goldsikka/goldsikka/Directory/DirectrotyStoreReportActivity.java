package com.goldsikka.goldsikka.Directory;

import static com.goldsikka.goldsikka.Activitys.AppSignatureHelper.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectrotyStoreReportActivity extends AppCompatActivity {

    TextView unameTv, uidTv, titleTv;
    RelativeLayout backbtn, phonenumber, Address, email, closerl;
    Button btn_submit;
    ApiDao apiDao;
    List<Listmodel> flist;
    Listmodel listmodel;
    String mobileno = "";
    String name = "";
    String address = "";
    String message = "";
    EditText et_feedback_form;
    Bundle bundle;
    String id;
    TextView phoennumbertext, nametext, addresstext;
    CheckBox itemCheckBox, emailCheckBox, addCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directroty_store_report);
        bundle = getIntent().getExtras();
        id = bundle.getString("id");
        Log.e("id", "" + id);
        uidTv = findViewById(R.id.uid);
        unameTv = findViewById(R.id.uname);
        titleTv = findViewById(R.id.title);
        uidTv.setText(AccountUtils.getCustomerID(getApplicationContext()));
        unameTv.setText(AccountUtils.getName(getApplicationContext()));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Report Issue");
        backbtn = findViewById(R.id.backbtn);
        phonenumber = findViewById(R.id.phonenumber);
        Address = findViewById(R.id.Address);
        email = findViewById(R.id.name);
        btn_submit = findViewById(R.id.btn_submit);
        closerl = findViewById(R.id.closerl);
        et_feedback_form = findViewById(R.id.et_feedback_form);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = et_feedback_form.getText().toString().trim();

                if (mobileno.length() == 0 && name.length() == 0 && address.length() == 0 && message.length() == 0) {
                    ToastMessage.onToast(getApplicationContext(), "Please select atleast one option or Enter message ", ToastMessage.ERROR);

                } else {
                    postreport();

                }
            }
        });

        phoennumbertext = findViewById(R.id.phoennumbertext);
        nametext = findViewById(R.id.nametext);
        addresstext = findViewById(R.id.Addresstext);


        itemCheckBox = findViewById(R.id.itemCheckBox);
        emailCheckBox = findViewById(R.id.emailCheckBox);
        addCheckBox = findViewById(R.id.addCheckBox);


        itemCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                        if (isChecked) {
                                                            // perform logic
                                                            phoennumbertext.setText("Phone Number");
                                                            mobileno = phoennumbertext.getText().toString().trim();
                                                        } else {
                                                            mobileno = "";
                                                        }
                                                    }
                                                }
        );


        emailCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                     @Override
                                                     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                         if (isChecked) {
                                                             // perform logic
                                                             nametext.setText("Name");
                                                             name = nametext.getText().toString().trim();
                                                             Log.e("addresstext", "" + name);

                                                         } else {
                                                             name = "";
                                                         }
                                                     }
                                                 }
        );
        addCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                   @Override
                                                   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                       if (isChecked) {
                                                           // perform logic
                                                           addresstext.setText("Address");
                                                           address = addresstext.getText().toString().trim();
                                                           Log.e("addresstext", "" + address);

                                                       } else {
                                                           address = "";
                                                       }
                                                   }
                                               }
        );

    }

    public void postreport() {
        Log.e("name", "" + name);
        Log.e("address", "" + address);
        Log.e("mobileno", "" + mobileno);
        Log.e("message", "" + message);

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
            Call<Listmodel> getplans = apiDao.postreportissue("Bearer " + AccountUtils.getAccessToken(this), name, address, mobileno, message);

            getplans.enqueue(new Callback<Listmodel>() {
                @Override
                public void onResponse(Call<Listmodel> call, Response<Listmodel> response) {
                    int statuscode = response.code();
                    dialog.dismiss();
                    Log.e("StatusCOdeForm", String.valueOf(statuscode));
                    listmodel = response.body();


                    if (statuscode == 200 || statuscode == 201) {
                        Log.e("reviewstatuscode", String.valueOf(statuscode));

                        ToastMessage.onToast(getApplicationContext(), "Thanks for your feedback", ToastMessage.SUCCESS);
                        //finish();
                        addCheckBox.setChecked(false);
                        itemCheckBox.setChecked(false);
                        emailCheckBox.setChecked(false);
                        et_feedback_form.setText(" ");
                    } else if (statuscode == 404) {
                        dialog.dismiss();
                        ToastMessage.onToast(getApplicationContext(), "Already the rating has given", ToastMessage.ERROR);

                    } else {
                        dialog.dismiss();
                       // ToastMessage.onToast(getApplicationContext(), "Technical Error", ToastMessage.ERROR);
                    }


                }

                @Override
                public void onFailure(Call<Listmodel> call, Throwable t) {
                    dialog.dismiss();
                }
            });

        }
    }

}