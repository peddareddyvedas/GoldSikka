package com.goldsikka.goldsikka.Activitys;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ReferralDetails extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.tvreferraldetails)
    TextView tvreferraldetails;
    @SuppressLint("NonConstantResourceId")
    @BindView(value = R.id.btrefer)
    Button btrefer;

    String streferraldetails,streferralcode;

TextView unameTv, uidTv, titleTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_details);

        ButterKnife.bind(this);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("Info ");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Info");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle!= null){
            streferraldetails = bundle.getString("moreinfo");
            streferralcode = bundle.getString("referralcode");
        }
        tvreferraldetails.setText(streferraldetails);
//        tvreferraldetails.setText("A paragraph is a group of words put together to form a group that is usually longer than a sentence. Paragraphs are often made up of several sentences. ... This makes it easier to see when one paragraph ends and another begins. In most organized forms of writing, such as essays, paragraphs contain a topic sentence .\n" +
//                "A paragraph is a group of words put together to form a group that is usually longer than a sentence. Paragraphs are often made up of several sentences. ... This makes it easier to see when one paragraph ends and another begins. In most organized forms of writing, such as essays, paragraphs contain a topic sentence .\n" +
//                "A paragraph is a group of words put together to form a group that is usually longer than a sentence. Paragraphs are often made up of several sentences. ... This makes it easier to see when one paragraph ends and another begins. In most organized forms of writing, such as essays, paragraphs contain a topic sentence .\nA paragraph is a group of words put together to form a group that is usually longer than a sentence. Paragraphs are often made up of several sentences. ... This makes it easier to see when one paragraph ends and another begins. In most organized forms of writing, such as essays, paragraphs contain a topic sentence .\n" +
//                "A paragraph is a group of words put together to form a group that is usually longer than a sentence. Paragraphs are often made up of several sentences. ... This makes it easier to see when one paragraph ends and another begins. In most organized forms of writing, such as essays, paragraphs contain a topic sentence .\n" +
//                "A paragraph is a group of words put together to form a group that is usually longer than a sentence. Paragraphs are often made up of several sentences. ... This makes it easier to see when one paragraph ends and another begins. In most organized forms of writing, such as essays, paragraphs contain a topic sentence .\n" +
//                "A paragraph is a group of words put together to form a group that is usually longer than a sentence. Paragraphs are often made up of several sentences. ... This makes it easier to see when one paragraph ends and another begins. In most organized forms of writing, such as essays, paragraphs contain a topic sentence .\n" +
//                "A paragraph is a group of words put together to form a group that is usually longer than a sentence. Paragraphs are often made up of several sentences. ... This makes it easier to see when one paragraph ends and another begins. In most organized forms of writing, such as essays, paragraphs contain a topic sentence .\n" +
//                "A paragraph is a group of words put together to form a group that is usually longer than a sentence. Paragraphs are often made up of several sentences. ... This makes it easier to see when one paragraph ends and another begins. In most organized forms of writing, such as essays, paragraphs contain a topic sentence .\n" +
//                "A paragraph is a group of words put together to form a group that is usually longer than a sentence. Paragraphs are often made up of several sentences. ... This makes it easier to see when one paragraph ends and another begins. In most organized forms of writing, such as essays, paragraphs contain a topic sentence .\n" +
//                "A paragraph is a group of words put together to form a group that is usually longer than a sentence. Paragraphs are often made up of several sentences. ... This makes it easier to see when one paragraph ends and another begins. In most organized forms of writing, such as essays, paragraphs contain a topic sentence .");

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            //   onBackPressed();
            return false;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btrefer)
    public void shareapp(){
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
        }else {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            String msg = "Join with me on Goldsikka, a Trusted app for Buy/ Sell / Loan - Gold. ";
            String refermsg = msg+"\n"+"Enter my code : "+streferralcode+"\nTo avail the Benefits of Goldsikka :\n"+"Google Play Store "+"https://play.google.com/store/apps/details?id=com.goldsikka.goldsikka"+
                    "\nApp Store "+"https://apps.apple.com/in/app/goldsikka/id1549497564";
            sendIntent.putExtra(Intent.EXTRA_TEXT, refermsg);
            sendIntent.setType("text/plain");
            Intent.createChooser(sendIntent, "Share via");
            startActivity(sendIntent);
        }

    }
}