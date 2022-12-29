package com.goldsikka.goldsikka.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class shared_preference {

    private SharedPreferences sp;
    private Context context;

    public shared_preference(Context context)
    {
        this.context= context;
        sp =  context.getSharedPreferences("savefiles", Context.MODE_PRIVATE);
    }
    public void WriteLoginStatus(boolean status){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("save",status);
        editor.commit();
    }
    public boolean readloginstatus(){
        boolean staus = false;
        staus= sp.getBoolean("save",false);
        return staus;
    }

    public void writepinstatus(boolean status){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("pinstatus", status);
        editor.commit();
    }
    public boolean readpinstatus(){
        boolean status = false;
        status = sp.getBoolean("pinstatus", false);
        return status;
    }
}
