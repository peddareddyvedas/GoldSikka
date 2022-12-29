package com.goldsikka.goldsikka.Fragments.Schemes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goldsikka.goldsikka.Models.SchemeModel;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchemeNicknameAdd extends AppCompatActivity {


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etnickname)
    EditText etnickname;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tverror)
    TextView tverror;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_submit)
    Button btn_submit;
    String  stname,stid;
    ApiDao apiDao;
TextView unameTv, uidTv, titleTv;
RelativeLayout backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_nickname_add);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle(getResources().getString(R.string.add_nickname));
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Add Nickname");
        backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            stid = bundle.getString("id");
        }
    }
    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();

    }


    @OnClick(R.id.btn_submit)
    public void validation(){
        tverror.setVisibility(View.GONE);
        stname = etnickname.getText().toString().trim();
        if (stname.isEmpty()){
            tverror.setVisibility(View.VISIBLE);
            tverror.setText("Please enter the name");
        }else {
            AddNickName();
        }


    }
    public void AddNickName(){

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
        apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
        Call<SchemeModel>  addname = apiDao.addnickname("Bearer "+AccountUtils.getAccessToken(this),stid,stname);
        addname.enqueue(new Callback<SchemeModel>() {
            @Override
            public void onResponse(Call<SchemeModel> call, Response<SchemeModel> response) {
                int statuscode = response.code();
                SchemeModel model = response.body();
                if (statuscode == HttpsURLConnection.HTTP_ACCEPTED){
                    dialog.dismiss();
                    AccountUtils.setSchemeNickname(SchemeNicknameAdd.this,model.getNick_name());
                    finish();
//                    Intent intent = new Intent(SchemeNicknameAdd.this,Schemes_usersubscribed_list.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
                }else {
                    dialog.dismiss();
                    tverror.setVisibility(View.GONE);
                    assert response.errorBody() != null;

                    try {
                        JSONObject  jObjError = new JSONObject(response.errorBody().string());
                        String st = jObjError.getString("message");
//                        ToastMessage.onToast(SchemeNicknameAdd.this,st,ToastMessage.ERROR);
                        JSONObject er = jObjError.getJSONObject("errors");
                        try {
                            JSONArray password = er.getJSONArray("nick_name");
                            for (int i = 0; i < password.length(); i++) {
                                tverror.setVisibility(View.VISIBLE);
                                tverror.setText(password.getString(i));
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
            public void onFailure(Call<SchemeModel> call, Throwable t) {
                dialog.dismiss();
                ToastMessage.onToast(SchemeNicknameAdd.this,"We have some Issues please try after sometime",ToastMessage.ERROR);

            }
        });
    }
}