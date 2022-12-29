package com.goldsikka.goldsikka.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goldsikka.goldsikka.Adapter.PageviewAdapter;
import com.goldsikka.goldsikka.Fragments.baseinterface;
import com.goldsikka.goldsikka.NewDesignsActivity.MainFragmentActivity;
import com.goldsikka.goldsikka.OrganizationWalletModule.Organizationwallet_mainpage;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.ErrorSnackBar;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.Utils.shared_preference;
import com.goldsikka.goldsikka.WelcomeActivity;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.model.Listmodel;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.viewpagerindicator.CirclePageIndicator;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnBoardScreen extends BaseActivity implements baseinterface {

    Handler handler;
    boolean isForceUpdate = true;
    String currentVersion;
    ApiDao apiDao;
    String banners;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES;
    private ArrayList<String> urls = new ArrayList<>();
    Button tv_skip;
    private String id;
    RelativeLayout laypout;

    private MixpanelAPI mMixpanel;

    String accesstoken;
    String complete = "0";

    public shared_preference sharedPreference;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_on_board_screen;

    }

    @Override
    protected void initView() {


        accesstoken = AccountUtils.getAccessToken(this);
        laypout = findViewById(R.id.rlayout);

//        Bundle bundle = getIntent().getExtras();
//        if (bundle !=null){
//            complete = bundle.getString("complete");
//        }

        mMixpanel = MixpanelAPI.getInstance(this, "4ea84a2232be6375f498aea0e6efdfce");

        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new GetVersionCode().execute();

        mPager = (ViewPager) findViewById(R.id.pager);

        apiDao = ApiClient.getClient(accesstoken).create(ApiDao.class);

        tv_skip = findViewById(R.id.tv_skip);
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isConnected(OnBoardScreen.this)) {
                    ToastMessage.onToast(OnBoardScreen.this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);

                    return;
                } else {
                    openpage();
                }

            }
        });

//        if (AccountUtils.getRoleid(this).equals("1")) {
//            tv_skip.setText("Login Or Sign up");
//
//        } else {
//            tv_skip.setText("Login Or Sign up");
//
//        }
        /*sharedPreference = new shared_preference(this);
        if (sharedPreference.readloginstatus()) {
            if (AccountUtils.getRoleid(this).equals("1")) {
                tv_skip.setText("Next");

            } else {
                tv_skip.setText("Login or Sign up");

            }
        }else{
            tv_skip.setText("Login or Sign up");
        }
        init_getbanners();
        try {
            sendToMixpanel();
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        sharedPreference = new shared_preference(this);
        if (sharedPreference.readloginstatus()) {
            if (AccountUtils.getRoleid(this).equals("1") || AccountUtils.getRoleid(this).equals("2")) {
                tv_skip.setText("Skip");

            } else {
                tv_skip.setText("Login or Sign up");

            }
        } else {
            tv_skip.setText("Login or Sign up");
        }
        init_getbanners();
        try {
            sendToMixpanel();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendToMixpanel() throws JSONException {
        JSONObject props = new JSONObject();
        props.put("source", "Pat's affiliate site");
        props.put("Opted out of email", true);
        mMixpanel.track("Sign Up", props);
    }

    public void init_getbanners() {
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {
            apiDao = ApiClient.getClient("").create(ApiDao.class);
            Call<List<Listmodel>> getimgs = apiDao.getonboardimages();
            getimgs.enqueue(new Callback<List<Listmodel>>() {
                @Override
                public void onResponse(Call<List<Listmodel>> call, Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    if (statuscode == HttpsURLConnection.HTTP_OK || statuscode == HttpsURLConnection.HTTP_CREATED) {
                        List<Listmodel> list = response.body();
                        if (list != null) {
                            for (Listmodel listModel : list) {

                                banners = listModel.getScreen_uri();

                                Log.e("banner", String.valueOf(banners));
                                init();
                            }
                        }
                    }

                }

                @Override
                public void onFailure(Call<List<Listmodel>> call, Throwable t) {

                }
            });
        }
    }

    private void init() {

        String[] image = new String[]{banners};
        for (int i = 0; i < image.length; i++) {
            Log.e("bannerurl", String.valueOf(urls));
            urls.add(image[i]);
        }

        mPager.setAdapter(new PageviewAdapter(OnBoardScreen.this, urls));

        CirclePageIndicator indicator = findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(4 * density);
        NUM_PAGES = urls.size();
        Log.e("pages", String.valueOf(NUM_PAGES));

        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            public void run() {
                // yourMethod();
                // Auto start of viewpager
                Handler handler = new Handler();
                Runnable Update = new Runnable() {
                    public void run() {
                        if (currentPage == NUM_PAGES - 1) {
                            currentPage = NUM_PAGES;
                            Log.e("curr", String.valueOf(NUM_PAGES));
                        }
                        mPager.setCurrentItem(currentPage++, true);


                    }
                };
                handler.post(Update);
                handler.postDelayed(Update, 3000);
                Timer swipeTimer = new Timer();
                swipeTimer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            handler.post(Update);

                                        }
                                    },
                        1500, NUM_PAGES * 2000);
            }
        }, NUM_PAGES * 3000);   //3 seconds
        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }

    @Override
    public void responce(@NonNull @NotNull Response<Listmodel> response, int stauscode) {
        if (stauscode == HttpsURLConnection.HTTP_OK || stauscode == HttpsURLConnection.HTTP_ACCEPTED) {
            Listmodel list = response.body();
            Log.e("dasdsad", "" + response.body());
            Boolean gs_pin = list.getIsgspin();
           /* sharedPreference = new shared_preference(this);
                if (sharedPreference.readloginstatus()){
                    if(sharedPreference.readpinstatus()) {
                        Intent intent = new Intent(this, EntryPin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(this, MainFragmentActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }else {
                    Intent intent = new Intent(OnBoardScreen.this, WelcomeActivity.class);
                    //  intent.putExtra("banners", String.valueOf(Banners));
                    startActivity(intent);
                    finish();
                }*/
            sharedPreference = new shared_preference(this);
            if (sharedPreference.readloginstatus()) {
                if (AccountUtils.getRoleid(this).equals("1")) {
                    if (sharedPreference.readpinstatus()) {
                        Intent intent = new Intent(this, EntryPin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(this, MainFragmentActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        MainFragmentActivity.isFromnboard = true;
                        startActivity(intent);
                    }
                } else if (AccountUtils.getRoleid(this).equals("2")) {
                    startActivity(new Intent(this, Organizationwallet_mainpage.class));
                    finish();
                }
            } else {
                Intent intent = new Intent(OnBoardScreen.this, WelcomeActivity.class);
                //  intent.putExtra("banners", String.valueOf(Banners));
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void listresponce(@NonNull @NotNull Response<List<Listmodel>> response, int stauscode) {

    }

    private class GetVersionCode extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;
            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + OnBoardScreen.this.getPackageName() + "&hl=in")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select(".hAyfc .htlgb")
                        .get(7)
                        .ownText();
                return newVersion;
            } catch (Exception e) {
                return newVersion;
            }
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);
            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                if (Float.parseFloat(currentVersion) < Float.parseFloat(onlineVersion)) {
                    //show dialog
                    //   Toast.makeText(SplashActivity.this, "Need Update", Toast.LENGTH_SHORT).show();
                    showUpdateDialog();
                } else {
                    //openpage();
                    //  getimages();
                    //  Toast.makeText(SplashActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (complete.equals("0")) {
//            Call<Listmodel> call = apiDao.isPin("Bearer " + accesstoken);
//            responsemethod(call);
//        }
//
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        if (complete.equals("0")) {
//            Call<Listmodel> call = apiDao.isPin("Bearer " + accesstoken);
//            responsemethod(call);
//        }
//
//    }

    public void showUpdateDialog() {
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(OnBoardScreen.this);

        alertDialogBuilder.setTitle(OnBoardScreen.this.getString(R.string.app_name));
        alertDialogBuilder.setMessage(OnBoardScreen.this.getString(R.string.update_message));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.update_now, (dialog, id) -> {
            String uri = String.format(Locale.ENGLISH, "https://play.google.com/store/apps/details?id=com.goldsikka.goldsikka");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
            dialog.cancel();
        });

        alertDialogBuilder.setNegativeButton(R.string.cancel, (dialog, which) -> {
            if (isForceUpdate) {
                finish();
            }
            dialog.dismiss();
        });
        alertDialogBuilder.show();
    }

    private long backPressed;
    private static final long TIME_DELAY = 2000;

    @Override
    public void onBackPressed() {

        if (backPressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            ErrorSnackBar.onBackExit(this, laypout);
        }
        backPressed = System.currentTimeMillis();
    }


    public void openpage() {
        //   handler = new Handler();
        final ProgressDialog dialog = new ProgressDialog(OnBoardScreen.this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        if (complete.equals("0")) {
            Call<Listmodel> call = apiDao.isPin("Bearer " + accesstoken);
            responsemethod(call);
        }

//        handler.postDelayed(() -> {
//
//            //  appname.setVisibility(View.GONE);
//            Intent intent = new Intent(OnBoardScreen.this, WelcomeActivity.class);
//            //  intent.putExtra("banners", String.valueOf(Banners));
//            startActivity(intent);
//            finish();
//        },2000);
        dialog.dismiss();
    }
}