package com.example.fthangoutv03.intercept;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.fthangoutv03.DataSource;

public class SmsReceiver extends BroadcastReceiver {

    private DataSource datasource;

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

                    // Init data source
                    DataSource datasource = new DataSource(context);
                    datasource.open();

                    // add contact
                    if (!datasource.getAllContacts()
                            .stream()
                            .anyMatch(contact -> contact.getPhone().equals(sender))) {
                        datasource.addContact(sender, "", sender, null);
                     }

                    // close bdd
                    datasource.close();


                    if (listener != null) {
                        listener.onSmsReceived(sender, message);
                    }
                }
            }
        }
    }
}
