package ch.heigvd.iict.sym.sym_labo4;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ch.heigvd.iict.sym.wearcommon.Constants;

public class NotificationActivity extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 1; //code to use for the notification id

    private Button notification_btn_display_notification;
    private Button notification_btn_display_notification_with_action;
    private Button notification_btn_display_notification_with_action_wearable_only;

    private final String CHANNEL_ID = "Labo3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        if(getIntent() != null)
            onNewIntent(getIntent());

        notification_btn_display_notification  = findViewById(R.id.notification_btn_display_notification);

        notification_btn_display_notification_with_action = findViewById(R.id.notification_btn_display_notification_with_action);

        notification_btn_display_notification_with_action_wearable_only = findViewById(R.id.notification_btn_display_notification_with_action_wearable_only);

        createNotificationChannel();

        notification_btn_display_notification.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PendingIntent pendingIntent = createPendingIntent(1,"Hello");
                NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("My simple Notif")
                        .setContentText("this is a simple notification from my phone")
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_MAX);
                builder.setContentIntent(pendingIntent);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NotificationActivity.this);
                notificationManager.notify(1, builder.build());
            }
        });

        notification_btn_display_notification_with_action.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent actionIntent = new Intent(Intent.ACTION_VIEW);
                PendingIntent actionPendingIntent =
                        PendingIntent.getActivity(NotificationActivity.this, 0, actionIntent, 0);
                PendingIntent pendingIntent = createPendingIntent(1,"Hello");
                NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("My action Notif")
                        .setContentText("this is a notification with actions")
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .addAction(R.drawable.common_google_signin_btn_text_light_focused,
                                getString(R.string.accept), actionPendingIntent);
                        builder.setContentIntent(pendingIntent);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NotificationActivity.this);
                notificationManager.notify(1, builder.build());
            }
        });

        notification_btn_display_notification_with_action_wearable_only.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent actionIntent = new Intent(Intent.ACTION_VIEW);
                PendingIntent actionPendingIntent =
                        PendingIntent.getActivity(NotificationActivity.this, 0, actionIntent, 0);

                NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender();
                wearableExtender.addAction( new NotificationCompat.Action.Builder(R.drawable.common_google_signin_btn_text_light_focused, getString(R.string.accept), actionPendingIntent).build());

                PendingIntent pendingIntent = createPendingIntent(1,"Hello");
                NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("My wearable Notif")
                        .setContentText("this is a notification with actions for wearable only")
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .extend(wearableExtender);

                builder.setContentIntent(pendingIntent);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NotificationActivity.this);
                notificationManager.notify(1, builder.build());
            }
        });


    }

    /*
     *  Code fourni pour les PendingIntent
     */

    /*
     *  Method called by system when a new Intent is received
     *  Display a toast with a message if the Intent is generated by
     *  createPendingIntent method.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent == null) return;
        if(Constants.MY_PENDING_INTENT_ACTION.equals(intent.getAction())) {
            Toast.makeText(this, "" + intent.getStringExtra("msg"), Toast.LENGTH_SHORT).show();
            NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID); //we close the notification
        }
    }

    /**
     * Method used to create a PendingIntent with the specified message
     * The intent will start a new activity Instance or bring to front an existing one.
     * See parentActivityName and launchMode options in Manifest
     * See https://developer.android.com/training/notify-user/navigation.html for TaskStackBuilder
     * @param requestCode The request code
     * @param message The message
     * @return The pending Intent
     */
    private PendingIntent createPendingIntent(int requestCode, String message) {
        Intent myIntent = new Intent(NotificationActivity.this, NotificationActivity.class);
        myIntent.setAction(Constants.MY_PENDING_INTENT_ACTION);
        myIntent.putExtra("msg", message);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(myIntent);

        return stackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
