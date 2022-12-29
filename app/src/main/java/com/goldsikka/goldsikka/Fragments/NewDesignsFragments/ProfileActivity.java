
package com.goldsikka.goldsikka.Fragments.NewDesignsFragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.model.Listmodel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {

    String pmname, pmemail, pmnunber, pmbussiname, pmpan, pmaadhar, pmwhatsapp, pmgstnumber, pmpincode, pmaddress, pmimage;

    CircleImageView editprofileimg, profileimg;
    TextView textupi3;
    ImageView accountdeleteimg, accountdeleteimg1;
    LinearLayout addmorell;
    RelativeLayout account3rl;
    EditText et_accounttext2;
    RecyclerView accountRV;
    ArrayList<Listmodel> accountlist;
    Button bt_Submit;
    int selectedPosition = -1;
    List<Listmodel> list;


    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    String cameraPermission[];
    String storagePermission[];

    EditText et_editname, et_gmailtext, et_phonetext;
    String st_name, stemail, st_mobile;
    public byte[] imageInByte;
//////////////////////

    RelativeLayout txtAlubm, txtCamera;
    Uri uri;
    Bitmap converetdImage;
    String profileBase64Obj;
    String encoded;

    int befor, after, star;

    String profilechange = "0";
    RelativeLayout backbtn;

    String editstruing, sssssssss;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);


        et_editname = findViewById(R.id.et_editname);
        et_gmailtext = findViewById(R.id.et_gmailtext);
        et_phonetext = findViewById(R.id.et_phonetext);
        profileimg = findViewById(R.id.profile_image);
        bt_Submit = findViewById(R.id.bt_Submit);

        // When there is change in state of edittext input
        et_editname.addTextChangedListener(new TextWatcher() {
            @Override
            // when there is no text added
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().trim().length() == 0) {


                    // set text to Not typing
                    // confirm.setText("Not Typing");
                } else {

                    // set text to typing
                    // confirm.setText(" Typing");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //confirm.setText(" Typing");
                Log.e("intest", "" + s);
                Log.e("before", "" + before);
                Log.e("count", "" + count);
                Log.e("start", "" + start);
                befor = before;
                after = count;
                star = start;
                sssssssss = String.valueOf(s);
            }

            // after we input some text
            @Override
            public void afterTextChanged(Editable s) {
                editstruing = String.valueOf(s);
                Log.e("editstruing", "" + editstruing);

                if (s.toString().trim().length() == 0) {

                    // set text to Stopped typing
                    // confirm.setText("Stopped Typing");
                }
            }
        });

        editprofileimg = findViewById(R.id.edit_profile);
        addmorell = findViewById(R.id.addmorell);
        account3rl = findViewById(R.id.account3rl);
        et_accounttext2 = findViewById(R.id.et_accounttext2);
        textupi3 = findViewById(R.id.textupi3);
        accountRV = findViewById(R.id.accountRV);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        editprofileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //     showImagePicDialog();

                showBottomSheetDialog();

            }
        });
        bt_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validations();
            }
        });


    }

    public void validations() {
        st_name = et_editname.getText().toString().trim();
        stemail = et_gmailtext.getText().toString().trim();
        st_mobile = et_phonetext.getText().toString().trim();

        if (st_name.length() > 0) {
            if (stemail.length() > 0) {
                if (st_mobile.length() > 0) {
                    if (isConn()) {


                        if (editstruing.equals(sssssssss) && profilechange.equals("-1")) {
                            Log.e("else", "editstruing");
                            Log.e("else", "sssssssss");

                            Log.e("if", "Allprofileedit");

                        } else {
                            Log.e("else", "nothingedit");
                                   /* showRefreshDialogue();
                                    noEditDataUploadtoServer();*/
                        }

                       /* if (befor > after) {
                            Log.e("if", "onlynameedit");
                            *//*showRefreshDialogue();
                            onlyTextUploadtoServer();*//*
                        } else {
                            if (profilechange.equals("-1")) {
                                Log.e("if", "onlyprofileedit");
                               *//* showRefreshDialogue();
                                onlyImageUploadtoServer(uri);*//*
                            } else {
                                if (star > after && star > befor) {
                                    Log.e("if", "Allprofileedit");

                                } else {
                                    Log.e("else", "nothingedit");
                                   *//* showRefreshDialogue();
                                    noEditDataUploadtoServer();*//*
                                }

                            }

                        }*/
                     /*   if (befor > after) {
                            Log.e("if", "onlynameedit");
                          *//*  showRefreshDialogue();
                            onlyTextUploadtoServer();*//*

                        } else if (profilechange.equals("-1")) {
                            Log.e("profileif", "onlyprofileedit");
                            //  done
                         *//* showRefreshDialogue();
                         editAllDatatoUploadServer(uri)
                         showRefreshDialogue()
                            onlyImageUploadtoServer(uri);*//*
                        } else if (profilechange.equals("-1") && befor > 1) {
                            Log.e("elseprofile", "boathnameprofile");
                           *//* showRefreshDialogue();
                            editAllDatatoUploadServer(uri);*//*

                        } else {
                            Log.e("else", "nothingedit");
                            //  done
                           *//* showRefreshDialogue();
                            noEditDataUploadtoServer();*//*
                        }
*/

                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please Enter Name", Toast.LENGTH_SHORT).show();
        }

    }


    ////////////////////////////
    private void showBottomSheetDialog() {

        View view = getLayoutInflater().inflate(R.layout.profile_alert, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();

        TextView selectanoptin = (TextView) dialog.findViewById(R.id.textview);


        txtCamera = (RelativeLayout) dialog.findViewById(R.id.camera);
        txtAlubm = (RelativeLayout) dialog.findViewById(R.id.album);

        TextView camera = (TextView) dialog.findViewById(R.id.txt_camera);
        TextView album = (TextView) dialog.findViewById(R.id.txt_album);
        TextView cancle = (TextView) dialog.findViewById(R.id.txt_cancle);


        txtCamera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else {
                    ClickImageFromCamera();
                }


                dialog.dismiss();
            }
        });
        txtAlubm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (!checkStoragePermission()) {
                    requestStoragePermission();
                } else {
                    GetImageFromGallery();
                }


                dialog.dismiss();
            }
        });

        RelativeLayout cancel = (RelativeLayout) dialog.findViewById(R.id.btn_no);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Log.e("resultCode", "" + resultCode);

        profilechange = String.valueOf(resultCode);
        Log.e("profilechange", "" + profilechange);

        if (requestCode == 0 && resultCode == RESULT_OK) {

            ImageCropFunction();

        } else if (requestCode == 2) {

            if (data != null) {
                uri = data.getData();
                //ImageCropFunction();
                decodeUri(uri);
                //ImageCropFunction();

            }
        } else if (requestCode == 1) {

            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Bitmap yourImage = bundle.getParcelable("data");
                    // convert bitmap to byte
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    yourImage.compress(Bitmap.CompressFormat.PNG, 50, stream);
                    imageInByte = stream.toByteArray();
                    ByteArrayInputStream imageStream = new ByteArrayInputStream(imageInByte);
                    Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                    // theImage = getResizedBitmap(theImage, 200);
                    profileimg.setImageBitmap(theImage);
                    loadEncoded64ImageStringFromBitmap(theImage);

                    converetdImage = getResizedBitmap(theImage, 100);
                    //   Log.e("converetdImage", "" + converetdImage);

                }
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }


    public void ClickImageFromCamera() {
        Intent cameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 1);


    }

    public void GetImageFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Intent GalIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2);
        } else {
            Intent GalIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2);
        }


    }


    public void ImageCropFunction() {
        // Image Crop Code
        try {
            Intent CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri, "image");
            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);
            startActivityForResult(CropIntent, 1);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void decodeUri(Uri uri) {
        ParcelFileDescriptor parcelFD = null;
        try {
            parcelFD = getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor imageSource = parcelFD.getFileDescriptor();

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(imageSource, null, o);

            // the new size we want to scale to
            final int REQUIRED_SIZE = 1024;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(imageSource, null, o2);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            imageInByte = stream.toByteArray();
            ByteArrayInputStream imageStream = new ByteArrayInputStream(imageInByte);
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            //theImage = getResizedBitmap(theImage, 200);
            profileimg.setImageBitmap(theImage);
            loadEncoded64ImageStringFromBitmap(theImage);
            converetdImage = getResizedBitmap(theImage, 100);
            //   Log.e("converetdImage", "" + converetdImage);


        } catch (IOException e) {
            // handle errors
        } finally {
            if (parcelFD != null)
                try {
                    parcelFD.close();
                } catch (IOException e) {
                    // ignored
                }
        }
    }

    public void loadEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        encoded = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        //  Log.e("encodedddddd", "" + encoded);

    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    ///////////////


    public boolean isConn() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void showImagePicDialog() {

        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && writeStorageaccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }

    private void pickFromCamera() {

        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);

    }


    private void pickFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);

    }


}
