package com.goldsikka.goldsikka.Activitys;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.WelcomeActivity;

import org.jsoup.Jsoup;
import java.util.Locale;
import pl.droidsonroids.gif.GifImageView;

public class SplashActivity extends  BaseActivity {

    Handler handler;
    GifImageView loading_gif;
    boolean isForceUpdate = true;
    String currentVersion;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        new GetVersionCode().execute();
    }



    private class GetVersionCode extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;
            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + SplashActivity.this.getPackageName() + "&hl=in")
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
                }else {
                    openpage();
                  //  getimages();
                  //  Toast.makeText(SplashActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void showUpdateDialog() {
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(SplashActivity.this);

        alertDialogBuilder.setTitle(SplashActivity.this.getString(R.string.app_name));
        alertDialogBuilder.setMessage(SplashActivity.this.getString(R.string.update_message));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.update_now, (dialog, id) -> {
            String uri = String.format(Locale.ENGLISH, "https://play.google.com/store/apps/details?id=com.aravind.goldsikka");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
//            initView();
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

//    @Override
//    protected void onResume() {
////        initView();
//        super.onResume();
//    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
        loading_gif = findViewById(R.id.loading_gif);
        loading_gif.setVisibility(View.VISIBLE);
        openpage();
    }


    public void openpage(){
        handler = new Handler();

        handler.postDelayed(() -> {

            loading_gif.setVisibility(View.GONE);
            //  appname.setVisibility(View.GONE);
            Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
            //  intent.putExtra("banners", String.valueOf(Banners));
            startActivity(intent);
            finish();
        },2000);
    }


}