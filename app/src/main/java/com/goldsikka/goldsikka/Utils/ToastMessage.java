package com.goldsikka.goldsikka.Utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goldsikka.goldsikka.R;


public class ToastMessage {

    public static int SUCCESS=1;
    public static int WARNING=2;
    public static int ERROR=3;
    public static int INFO=4;
    public static int STOCK=6;
    public static int THEME =7;
    public static int NOTIFY =8;


    public static void onToast(Context context, String message, int type){
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);

        View layout = LayoutInflater.from(context).inflate(R.layout.content_custom_toast, null, false);
        toast.setView(layout);

        TextView txtMsg = layout.findViewById(R.id.toast_message);
      //  ImageView toastImage = layout.findViewById(R.id.toast_icon);
        LinearLayout toastLayout = layout.findViewById(R.id.toast_layout);

        toast.setGravity(Gravity.FILL_HORIZONTAL| Gravity.TOP, 0, 0);
        toast.setMargin(0, 0);

        //Select View Type
        if(type == SUCCESS){
          //  toastImage.setImageResource(R.drawable.ic_check);
            toastLayout.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
        }else if(type == WARNING){
          //  toastImage.setImageResource(R.drawable.ic_warning);
            toastLayout.setBackgroundColor(context.getResources().getColor(R.color.colorYellow));
        }else if(type == ERROR){
         //   toastImage.setImageResource(R.drawable.ic_clear);
            toastLayout.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
        }else if(type == INFO){
         //   toastImage.setImageResource(R.drawable.ic_info);
            toastLayout.setBackgroundColor(context.getResources().getColor(R.color.colorIndigo));
        }else if (type == STOCK){
          //  toastImage.setImageResource(R.drawable.ic_info);
            toastLayout.setBackgroundColor(context.getResources().getColor(R.color.colorBlueGrey));
        }
        else if(type == NOTIFY){
            //toastImage.setImageResource(R.drawable.ic_warning);
            toastLayout.setBackgroundColor(context.getResources().getColor(R.color.colorOrange));
        }

        txtMsg.setText(message);
        toast.show();
    }
}
