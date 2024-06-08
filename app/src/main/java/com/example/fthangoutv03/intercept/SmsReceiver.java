package com.example.fthangoutv03.intercept;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

    public interface SmsListener {
        void onSmsReceived(String sender, String message);
    }

    private static SmsListener listener;

    public static void setSmsListener(SmsListener smsListener) {
        listener = smsListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                String format = bundle.getString("format");
                for (Object pdu : pdus) {
                    SmsMessage smsMessage;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        smsMessage = SmsMessage.createFromPdu((byte[]) pdu, format);
                    } else {
                        smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    }
                    String sender = smsMessage.getDisplayOriginatingAddress();
                    String message = smsMessage.getMessageBody();


                    if (listener != null) {
                        listener.onSmsReceived(sender, message);
                    }
                }
            }
        }
    }
}
