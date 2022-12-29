package com.goldsikka.goldsikka.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goldsikka.goldsikka.BuildConfig;
import com.goldsikka.goldsikka.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class InvoicePage extends AppCompatActivity implements View.OnClickListener {

    TextView tvname,tvmobile,tvemail,tvamount,tvgst,tvtotalamount,tvgrams,tvdate,tvid;
    String name,id,date,amount,gst,totalamount,grams;
    private Bitmap bitmap;
    private LinearLayout llScroll;
    private static final int STORAGE_CODE = 100;
    Button btn_download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Invoice");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            amount = bundle.getString("amount");
            gst = bundle.getString("gst");
            totalamount = bundle.getString("totalamount");
            grams = bundle.getString("grams");
            id = bundle.getString("id");
            date = bundle.getString("date");
        }
        initlizeviews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                // todo: goto back activity from here
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initlizeviews() {

        btn_download = findViewById(R.id.btn_download);
        btn_download.setOnClickListener(this);

        llScroll = findViewById(R.id.linear_layout);
        tvname = findViewById(R.id.bill_name);
        tvmobile = findViewById(R.id.bill_mobile);
        tvemail = findViewById(R.id.bill_email);
        tvamount = findViewById(R.id.bill_amount);
        tvtotalamount = findViewById(R.id.bill_totalamount);
        tvgst = findViewById(R.id.bill_gstamount);
        tvgrams = findViewById(R.id.bill_gramsvalue);
        tvdate = findViewById(R.id.bill_date);
        tvid = findViewById(R.id.bill_id);

        setdata();
    }

    public void setdata(){

        tvtotalamount.setText("Total Amount :"+totalamount);
        tvgrams.setText("Grams Value :"+grams);
        tvamount.setText("Amount :"+amount);
        tvgst.setText("Gst :"+gst);
        tvid.setText("Id :"+id);
        tvdate.setText("Date :"+date);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_download:
                download_pdf();
                return;
        }
    }

    public void download_pdf(){
        Log.d("size"," "+llScroll.getWidth() +"  "+llScroll.getWidth());
        bitmap = loadBitmapFromView(llScroll, llScroll.getWidth(), llScroll.getHeight());
        createPdf();
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    private void createPdf(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;

        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);

        // write the document content

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, "/Goldsikka.pdf");
//        String targetPdf = "*/*";
//        File filePath;
//        filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(file));

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("error",e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        document.close();
        // close the document
        Toast.makeText(this, "PDF of Scroll is created!!!", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, STORAGE_CODE);
            }else {

                openGeneratedPDF();
            }
        }else {
            openGeneratedPDF();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    openGeneratedPDF();
                } else {
                    Toast.makeText(this, "Permission was denied! ", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void openGeneratedPDF(){
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, "/Goldsikka.pdf");
        if (file.exists())
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
//            Uri photoURI = Uri.fromFile(file);
            Uri photoURI = FileProvider.getUriForFile(InvoicePage.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);

            intent.setDataAndType(photoURI, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // startActivity(intent);
            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                Toast.makeText(InvoicePage.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }
    }
}