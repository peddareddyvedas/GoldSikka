package com.goldsikka.goldsikka.Activitys.Events;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.AccountUtils;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.interfaces.ApiDao;
import com.goldsikka.goldsikka.netwokconnection.ApiClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.net.ssl.HttpsURLConnection;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventQRCodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler  {

    ZXingScannerView scannerView;
    ImageView ivgalary;

    ApiDao apiDao;
    String contents;

    String from ;
    int PICK_PHOTO =100;
    String isteventdate,isteventname,istgroom,istbride,istvenue,istdescription,isteventtype,isteventtime,istholdername,iid,iimage,ieventid;
TextView unameTv, uidTv, titleTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_code_scanner);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle("Scan & Pay");
        unameTv = findViewById(R.id.uname);
        uidTv = findViewById(R.id.uid);
        titleTv = findViewById(R.id.title);

        unameTv.setText(AccountUtils.getName(this));
        uidTv.setText(AccountUtils.getCustomerID(this));
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Scan & Pay");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        scannerView = findViewById(R.id.scanview);
        ivgalary = findViewById(R.id.ivgalary);
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        from = "scan";
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
        ivgalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_PHOTO);
            }
        });

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
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void handleResult(Result rawResult) {
        if (from.equals("gallery")){
           // openverifyeventid(rawResult);
        }else if (from.equals("scan")){
           // openverifyeventid(rawResult);
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
//
//    public void openverifyeventid(Result result){
//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setMessage("Please Wait....");
//        dialog.setCancelable(false);
//        dialog.show();
//
//        if (!NetworkUtils.isConnected(this)) {
//            dialog.dismiss();
//            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
//        }
//        else {
//            apiDao = ApiClient.getClient(AccountUtils.getAccessToken(this)).create(ApiDao.class);
//            Call<EventModel> verify = apiDao.eventverify("Bearer "+AccountUtils.getAccessToken(this),"1",result.getText());
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
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                    else if (statuscode == 422){
//                        dialog.dismiss();
//                        onResume();
//                        assert response.errorBody() != null;
//
//                        try {
//                            JSONObject jObjError = new JSONObject(response.errorBody().string());
//                            String st = jObjError.getString("message");
//                            ToastMessage.onToast(EventQRCodeScanner.this, st, ToastMessage.ERROR);
//
//                            JSONObject er = jObjError.getJSONObject("errors");
//                            try {
//                                JSONArray array_mobile = er.getJSONArray("eventId");
//                                for (int i = 0; i < array_mobile.length(); i++) {
//                                    ToastMessage.onToast(EventQRCodeScanner.this,array_mobile.getString(i),ToastMessage.ERROR);
//
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
//                        onResume();
//                        assert response.errorBody() != null;
//
//                        try {
//                            JSONObject jObjError = new JSONObject(response.errorBody().string());
//                            String st = jObjError.getString("message");
//                            ToastMessage.onToast(EventQRCodeScanner.this, st, ToastMessage.ERROR);
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
//                    onResume();
//                    ToastMessage.onToast(EventQRCodeScanner.this,"We have some issues",ToastMessage.ERROR);
//                }
//            });
//        }
//    }
    public void onsucess(String from){

        Intent intent = new Intent(this, EventConformation.class);
        intent.putExtra("etype",isteventtype);
        intent.putExtra("ename",isteventname);
        intent.putExtra("ebride",istbride);
        intent.putExtra("egroom",istgroom);
        intent.putExtra("ehoder",istholdername);
        intent.putExtra("evenue",istvenue);
        intent.putExtra("edate",isteventdate);
        intent.putExtra("etime",isteventtime);
        intent.putExtra("edes",istdescription);
        intent.putExtra("iid",iid);
        intent.putExtra("ieventid",ieventid);
        intent.putExtra("iimage",iimage);
        intent.putExtra("from",from);
        Log.e("imageefef",iimage);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO && resultCode == RESULT_OK && null != data) {
            try {

                final Uri imageUri = data.getData();

                final InputStream imageStream = getContentResolver().openInputStream(imageUri);

                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                try {

                    Bitmap bMap = selectedImage;



                    int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];

                    bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

                    LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);

                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                    Reader reader = new MultiFormatReader();

                    Result result = reader.decode(bitmap);

                    contents = result.getText();
                    from = "gallery";
                    handleResult(result);

                }catch (Exception e){

                    e.printStackTrace();
                    Log.e("gallry",e.toString());
                    Toast.makeText(this, "No qr code found", Toast.LENGTH_SHORT).show();

                }


            } catch (FileNotFoundException e) {

                e.printStackTrace();

                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();

            }

        }
        else {

            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();

        }

    }

}