package com.fidea.letter;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.fidea.letter.api.APIClient;
import com.fidea.letter.api.APIInterface;
import com.fidea.letter.models.NotificationModel;
import com.fidea.letter.util.Util;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BroadcastReceiver extends android.content.BroadcastReceiver {

    private ConnectivityReceiverListener mConnectivityReceiverListener;
    private String REGISTER_DIARY = "RegisterDiary";
    public static String FROM_NOTIFICATION = "from_notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TAG", "HERE NEVER CALLED????");
        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            setAlarmManager(context);
        } else {
            getInfo(context);
        }
    }

    private void setAlarmManager(Context context) {
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BroadcastReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        // setRepeating() lets you specify a precise custom interval--in this case,
        // 20 minutes.
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 2, alarmIntent);
    }

    private void getInfo(Context context) {
        APIInterface apiInterface = APIClient.Companion.getRetrofit(Util.Companion.getToken(context),
                Util.Companion.getCacheDir(context)).create(APIInterface.class);
        apiInterface.getNotification().enqueue(new Callback<ArrayList<NotificationModel>>() {
            @Override
            public void onResponse(Call<ArrayList<NotificationModel>> call,
                                   Response<ArrayList<NotificationModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showNotification(response.body(), context);
                } else {
                    Log.i("TAG", "Error in getting notifications " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NotificationModel>> call, Throwable t) {
                Log.i("TAG", "Error in getting notifications " + t.getMessage());
            }
        });
    }

    private void showNotification(ArrayList<NotificationModel> notificationMessages, Context context) {
        for (int i = 0; i < notificationMessages.size(); i++) {
            NotificationModel notificationMessage = notificationMessages.get(i);
            try {
                NotificationManager mNotificationManager;
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context.getApplicationContext(), "notify_001");
                Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
                intent.putExtra(FROM_NOTIFICATION, "yeah");
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                bigText.bigText(notificationMessage.getBigText());
                bigText.setBigContentTitle(notificationMessage.getBigContentTitle());
                bigText.setSummaryText(notificationMessage.getSummaryText());

                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                mBuilder.setContentTitle(notificationMessage.getContentTitle());
                mBuilder.setContentText(notificationMessage.getContentText());
                mBuilder.setPriority(notificationMessage.getImportance());
                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setStyle(bigText);
                mNotificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String channelId = "1234";
                    NotificationChannel channel = new NotificationChannel(channelId, "چندماهمه",
                            NotificationManager.IMPORTANCE_HIGH);
                    mNotificationManager.createNotificationChannel(channel);
                    mBuilder.setChannelId(channelId);
                }


                mNotificationManager.notify(0, mBuilder.build());
                Log.i("TAG", "Notif?");
            } catch (Exception e) {
                Log.i("TAG", "ERROR IN CREATING NOTIFICATION");
            }
        }
    }

    public BroadcastReceiver() {

    }

    BroadcastReceiver(ConnectivityReceiverListener listener) {
        mConnectivityReceiverListener = listener;
    }


    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
