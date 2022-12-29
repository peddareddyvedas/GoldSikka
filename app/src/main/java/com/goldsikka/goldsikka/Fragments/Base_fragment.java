package com.goldsikka.goldsikka.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.goldsikka.goldsikka.model.Listmodel;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public abstract class Base_fragment extends Fragment {

    View view;
    private Activity activity;

    protected abstract View initView(View view);

    protected abstract int getLayoutId();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(),container,false);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }
    public void responsemethod(Call<Listmodel> call) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        call.enqueue(new Callback<Listmodel>() {

            @Override
            public void onResponse(@NonNull Call<Listmodel> call, @NonNull retrofit2.Response<Listmodel> response) {
                int statuscode = response.code();
                    ((baseinterface)activity).responce(response,statuscode);
                dialog.dismiss();
            }
            @Override
            public void onFailure(@NonNull Call<Listmodel> call, @NonNull Throwable t) {
                dialog.dismiss();
               // Toast.makeText(activity, "Technical problem", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
