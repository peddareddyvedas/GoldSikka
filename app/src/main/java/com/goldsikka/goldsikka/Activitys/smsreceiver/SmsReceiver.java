package com.goldsikka.goldsikka.Activitys.smsreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("call", "smsreceiver");
        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");

        for (int i = 0; i < pdus.length; i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = smsMessage.getDisplayOriginatingAddress();

            //You must check here if the sender is your provider and not another one with same text.
            Log.e("sender", " " + sender);
            String messageBody = smsMessage.getMessageBody();
            String message = smsMessage.getDisplayMessageBody();
            Log.e("msgboday", "" + messageBody);
            Log.e("message", " " + message);
            /////get only sms from particular SMS gateway

            if (sender.contains("GSJPLC")) {
                Log.e("call", "Goldsikka");
                Log.e("mmmmm", message);
                if (mListener != null) {
                    mListener.messageReceived(message);
                }
            }
            //to read the all incoming msgs
            // mListener.messageReceived(messageBody);
        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
