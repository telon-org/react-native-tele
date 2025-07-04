package one.telefon.tele;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import one.telefon.tele.utils.ArgumentUtils;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;

import javax.annotation.Nullable;

public class TeleBroadcastReceiver extends BroadcastReceiver {

    private static String TAG = "one.telefon.tele.TeleBroadcastReceiver";

    private int seq = 0;

    private ReactApplicationContext context;

    private HashMap<Integer, Callback> callbacks = new HashMap<>();

    public TeleBroadcastReceiver(ReactApplicationContext context) {
        this.context = context;
    }

    public void setContext(ReactApplicationContext context) {
        this.context = context;
    }

    public int register(Callback callback) {
        int id = ++seq;
        callbacks.put(id, callback);
        return id;
    }

    public IntentFilter getFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(TeleActions.EVENT_STARTED);
        //filter.addAction(TeleActions.EVENT_ACCOUNT_CREATED);
        //filter.addAction(TeleActions.EVENT_REGISTRATION_CHANGED);
        filter.addAction(TeleActions.EVENT_CALL_RECEIVED);
        filter.addAction(TeleActions.EVENT_CALL_CHANGED);
        filter.addAction(TeleActions.EVENT_CALL_TERMINATED);
        //filter.addAction(TeleActions.EVENT_CALL_SCREEN_LOCKED);
        //filter.addAction(TeleActions.EVENT_MESSAGE_RECEIVED);
        filter.addAction(TeleActions.EVENT_HANDLED);

        return filter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Log.d(TAG, "Received \""+ action +"\" response from service (" + ArgumentUtils.dumpIntentExtraParameters(intent) + ")");

        switch (action) {
            case TeleActions.EVENT_STARTED:
                onCallback(intent);
                break;
/*
                case TeleActions.EVENT_ACCOUNT_CREATED:
                onCallback(intent);
                break;
            case TeleActions.EVENT_REGISTRATION_CHANGED:
                onRegistrationChanged(intent);
                break;
            case TeleActions.EVENT_MESSAGE_RECEIVED:
                onMessageReceived(intent);
                break;
*/
            case TeleActions.EVENT_CALL_RECEIVED:
                onCallReceived(intent);
                break;
            case TeleActions.EVENT_CALL_CHANGED:
                onCallChanged(intent);
                break;
            case TeleActions.EVENT_CALL_TERMINATED:
                onCallTerminated(intent);
                break;
            default:
                onCallback(intent);
                break;
        }


/*
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (state == null) {

            //Outgoing call
            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.e("tag", "Outgoing number : " + number);

        } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {

            Log.e("tag", "EXTRA_STATE_OFFHOOK");

        } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

            Log.e("tag", "EXTRA_STATE_IDLE");

        } else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

            //Incoming call
            String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.e("tag", "Incoming number : " + number);

        } else {
            Log.e("tag extra ", state);
        }
*/


    }
/*
    private void onRegistrationChanged(Intent intent) {
        String json = intent.getStringExtra("data");
        Object params = ArgumentUtils.fromJson(json);
        emit("TeleRegistrationChanged", params);
    }

    private void onMessageReceived(Intent intent) {
        String json = intent.getStringExtra("data");
        Object params = ArgumentUtils.fromJson(json);

        emit("TeleMessageReceived", params);
    }
*/
    private void onCallReceived(Intent intent) {
        String json = intent.getStringExtra("data");
        Object params = ArgumentUtils.fromJson(json);
        emit("TeleCallReceived", params);
    }

    private void onCallChanged(Intent intent) {
        String json = intent.getStringExtra("data");
        Object params = ArgumentUtils.fromJson(json);
        emit("TeleCallChanged", params);
    }

    private void onCallTerminated(Intent intent) {
        String json = intent.getStringExtra("data");
        Object params = ArgumentUtils.fromJson(json);
        emit("TeleCallTerminated", params);
    }

    private void onCallback(Intent intent) {
        // Define callback
        Callback callback = null;

        if (intent.hasExtra("callback_id")) {
            int id = intent.getIntExtra("callback_id", -1);
            if (callbacks.containsKey(id)) {
                callback = callbacks.remove(id);
            } else {
                Log.w(TAG, "Callback with \""+ id +"\" identifier not found (\""+ intent.getAction() +"\")");
            }
        }

        if (callback == null) {
            return;
        }

        // -----
        if (intent.hasExtra("exception")) {
            Log.w(TAG, "Callback executed with exception state: " + intent.getStringExtra("exception"));
            callback.invoke(false, intent.getStringExtra("exception"));
        } else if (intent.hasExtra("data")) {
            Object params = ArgumentUtils.fromJson(intent.getStringExtra("data"));
            callback.invoke(true, params);
        } else {
            callback.invoke(true, true);
        }
    }

    private void emit(String eventName, @Nullable Object data) {
        Log.d(TAG, "emit " + eventName + " / " + data);

        context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, data);
    }
}
