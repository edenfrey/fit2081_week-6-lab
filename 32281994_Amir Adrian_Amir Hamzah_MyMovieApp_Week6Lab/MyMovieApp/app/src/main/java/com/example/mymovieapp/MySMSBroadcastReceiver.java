package com.example.mymovieapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class MySMSBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for(int i = 0; i < messages.length; i++){
            SmsMessage currentMsg = messages[i];
            String message = currentMsg.getDisplayMessageBody().toString();
            System.out.println(currentMsg);
            Intent sendMessage = new Intent();
            sendMessage.setAction("movieSmsBroadcast");
            sendMessage.putExtra("msgKey", message);
            context.sendBroadcast(sendMessage);
        }
    }
}
