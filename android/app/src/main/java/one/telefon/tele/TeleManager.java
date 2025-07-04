package one.telefon.tele;

import android.os.Bundle;
import android.util.Log;

import android.app.Service;
import android.app.Activity;

import android.content.Intent;
import android.content.Context;


import android.telecom.Call;

import com.facebook.react.HeadlessJsTaskService;

import one.telefon.tele.TeleModule;

public class TeleManager {


    private static String LOG_TAG = "LOG_telefon.one[TeleManager]";

    private static Call currentCall;

    
    private static void sendHeadless(String action, String extra1s, String extra2s, String extra3s, int extra1i, int extra2i,
            long extra1l, long extra2l) {
        
       /*         // if (!isAppOnForeground((context))) {
            Context context = TeleModule.currentContext;//getApplicationContext();
        if(context==null) return;
        //Intent recIntent = new Intent(context, MainService.class);

        recIntent.putExtra("action", action);
        recIntent.putExtra("extra1s", extra1s);
        recIntent.putExtra("extra2s", extra2s);
        recIntent.putExtra("extra3s", extra3s);

        recIntent.putExtra("extra1i", extra1i);
        recIntent.putExtra("extra2i", extra2i);

        recIntent.putExtra("extra1l", extra1l);
        recIntent.putExtra("extra2l", extra2l);

        context.startService(recIntent);
        HeadlessJsTaskService.acquireWakeLockNow(context);
        // }*/
    }

    public static void getCurrentCall()
    {
        updateCall(currentCall,"getCurrentCall");
    }

    public static void updateCall(Call call, String extra1s) {
        currentCall = call;

        if (call==null)
        {
            sendHeadless("TeleService", extra1s, "", "", 0, 0, 0, 0);
            return;
        }

        Call.Details details = call.getDetails();

        String num = details.getHandle().toString();
        String name = details.getCallerDisplayName();

        long creationTimeMillis;

        // if (android.os.Build.VERSION.SDK_INT >= 26) {
        // if (Build.VERSION.SDK_INT >= 26) {
        // creationTimeMillis = details.getCreationTimeMillis();
        // } else {
        creationTimeMillis = 0;
        // }

        int state = call.getState();

        int direction;

        long connectTimeMillis = details.getConnectTimeMillis();

        // if (android.os.Build.VERSION.SDK_INT >= 29) {
        // direction = details.getCallDirection();

        // } else {
        direction = 0;
        // }

        sendHeadless("TeleService", extra1s, name, num, state, direction, creationTimeMillis, connectTimeMillis);
    }

    /*
     * public static void cancel() { if (currentCall.getDetails().getState() ==
     * Call.STATE_RINGING) rejectCall(); else disconnectCall(); }
     */

    // incoming
    public static void reject() {
        Log.d(LOG_TAG, "reject()");
        currentCall.reject(false, "");
    }

    public static void answer() {
        Log.d(LOG_TAG, "answer()");
        currentCall.answer(currentCall.getDetails().getVideoState());
    }

    // outgoing
    public static void disconnect() {
        Log.d(LOG_TAG, "disconnect()");
        currentCall.disconnect();
    }

}
