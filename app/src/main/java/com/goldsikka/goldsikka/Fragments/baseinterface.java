package com.goldsikka.goldsikka.Fragments;

import android.view.View;

import androidx.annotation.NonNull;

import com.goldsikka.goldsikka.model.Listmodel;

import java.util.List;

public interface baseinterface {

    void responce(@NonNull retrofit2.Response<Listmodel> response, int stauscode);
    void listresponce(@NonNull retrofit2.Response<List<Listmodel>> response, int stauscode);
}
