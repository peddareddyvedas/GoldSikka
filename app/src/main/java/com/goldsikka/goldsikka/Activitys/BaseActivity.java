package com.goldsikka.goldsikka.Activitys;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.goldsikka.goldsikka.Fragments.baseinterface;
import com.goldsikka.goldsikka.R;
import com.goldsikka.goldsikka.Utils.NetworkUtils;
import com.goldsikka.goldsikka.Utils.ToastMessage;
import com.goldsikka.goldsikka.model.Listmodel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getLayoutId();

    protected abstract void initView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();


    }

    public void responsemethod(Call<Listmodel> call) {
//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setMessage("Please Wait....");
//        dialog.setCancelable(false);
//        dialog.show();
        if (!NetworkUtils.isConnected(this)){
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        }else {

            call.enqueue(new Callback<Listmodel>() {

                @Override
                public void onResponse(@NonNull Call<Listmodel> call, @NonNull retrofit2.Response<Listmodel> response) {
                    int statuscode = response.code();
                    ((baseinterface) BaseActivity.this).responce(response, statuscode);
                    //  dialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<Listmodel> call, @NonNull Throwable t) {
                    // dialog.dismiss();
                  //  Toast.makeText(BaseActivity.this, "Technical problem", Toast.LENGTH_SHORT).show();
                }
            });
        }

}

    public void Listresponce(Call<List<Listmodel>> call) {
//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setMessage("Please Wait....");
//        dialog.setCancelable(false);
//        dialog.show();
        if (!NetworkUtils.isConnected(this)) {
            ToastMessage.onToast(this, getString(R.string.error_no_internet_connection), ToastMessage.ERROR);
            return;
        } else {

            call.enqueue(new Callback<List<Listmodel>>() {

                @Override
                public void onResponse(@NonNull Call<List<Listmodel>> call, @NonNull retrofit2.Response<List<Listmodel>> response) {
                    int statuscode = response.code();
                    ((baseinterface) BaseActivity.this).listresponce(response, statuscode);
                    /// dialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<List<Listmodel>> call, @NonNull Throwable t) {
                    // dialog.dismiss();
                  //  Toast.makeText(BaseActivity.this, "Technical problem", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
