package com.company.automaticfishfeederapp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class JobSchedule extends BroadcastReceiver {
    private DatabaseReference databaseReference;
    private String deviceId;
    @Override
    public void onReceive(Context context, Intent intent) {
        //MediaPlayer mediaPlayer=MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        //mediaPlayer.start();
        LoginSession sessionManagement =new LoginSession(context);
        HashMap<String, String> user = sessionManagement.readLoginSession();
        deviceId = user.get(LoginSession.KEY_DEVICEID);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("FishFeeding");

        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Notify")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Alarm Reminders")
                .setContentText("Hey, Wake Up!")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManagerCompat.notify(200, builder.build());

        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/"  + Settings.System.DEFAULT_ALARM_ALERT_URI);

        Ringtone r = RingtoneManager.getRingtone(context,sound);
        r.play();

        feedingNoNow(deviceId);
    }

    private void feedingNoNow(String deviceId)
    {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("deviceId", deviceId);
        hashMap.put("triggerValue", 0);

        databaseReference.child(deviceId).updateChildren(hashMap);
    }

}