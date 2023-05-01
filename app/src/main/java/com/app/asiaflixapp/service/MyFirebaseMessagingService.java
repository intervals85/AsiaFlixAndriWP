package com.app.asiaflixapp.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.app.asiaflixapp.R;
import com.app.asiaflixapp.activity.MainActivity;
import com.app.asiaflixapp.activity.SplashActivity;
import com.app.asiaflixapp.helper.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    Context context;

    @Override
    public void onNewToken(String s) {
        Log.i("getToken", s);
    }
    @SuppressLint("WrongThread")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // remoteMessage object contains all you need ;-)
//        String notificationTitle = remoteMessage.getNotification().getTitle();
//        String notificationContent = remoteMessage.getNotification().getBody();
        String notificationTitle = remoteMessage.getData().get("judul");
        String notificationContent = remoteMessage.getData().get("isi");
        String imageUrl = remoteMessage.getData().get("image");
        // lets create a notification with these data
        if (notificationTitle!=null) {
            Log.i("NotificationTest", notificationTitle);
            new  ParsePageTask().execute(Utils.INSTANCE.getBase_url());
        }else {
            showNotification(remoteMessage.getNotification().getBody());
        }

    }

    class ParsePageTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            try {
                Document doc = Jsoup.connect(urls[0]).get();
                Elements link = doc.getElementsByClass("container");
                return link.toString();
            } catch (Exception ignored) {
            }

            return "";
        }

        protected void onPostExecute(String result) {
            try {
                Document document = Jsoup.parse(result);
                Elements elementsDrama = document.getElementsByClass("content").select("div.block-tab")
                        .select("div.tab-container") .select("div.left-tab-1").select("ul").select("li");
                String title = elementsDrama.first().select("a").attr("title");
                String image = elementsDrama.first().select("a").select("img").attr("data-original");
                String eps = elementsDrama.first().select("a").select("span.ep").text();
                String link = elementsDrama.first().select("a").attr("href");

                if (chekPref()==null || chekPref().equals("") || !chekPref().equals(link)) {
                    createNotification(getString(R.string.app_name), title, fetchGambar(image));
                    savePref(link);
                }else {
                    Log.i("NotificationTest","UDAHHHH");
                }
            } catch (NullPointerException | IndexOutOfBoundsException n) {
                n.getMessage();
            }
        }
    }


    private void createNotification(String notificationTitle, String notificationContent, String imageUrl){
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.putExtra("from", "Notif");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(SplashActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0 /* Request code */, notificationIntent,
                PendingIntent.FLAG_ONE_SHOT);

        // Let's create a notification builder object
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getResources().getString(R.string.notification_channel_id));
        // Create a notificationManager object
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        // If android version is greater than 8.0 then create notification channel
        if (android.os.Build.VERSION.SDK_INT >=     android.os.Build.VERSION_CODES.O) {

            // Create a notification channel
            NotificationChannel notificationChannel = new NotificationChannel(getResources().getString(R.string.notification_channel_id),    getResources().getString(R.string.notification_channel_name),     NotificationManager.IMPORTANCE_DEFAULT);
            // Set properties to notification channel
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300});

            // Pass the notificationChannel object to notificationManager
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
// Set the notification parameters to the notification builder object
        builder.setContentTitle(notificationTitle)
                .setContentText(notificationContent)
                .setSmallIcon(R.mipmap.ic_logo)
                .setSound(defaultSoundUri)
                .setAutoCancel(true);
// Set the image for the notification
        if (imageUrl != null) {
            Glide.with(this)
                    .asBitmap()
                    .load(imageUrl)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            builder.setStyle(
                                    new NotificationCompat.BigPictureStyle()
                                            .bigPicture(resource)
                                            .bigLargeIcon(null)
                            ).setLargeIcon(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });

//            Bitmap bitmap = getBitmapfromUrl( imageUrl);
//            builder.setStyle(
//                    new NotificationCompat.BigPictureStyle()
//                            .bigPicture(bitmap)
//                            .bigLargeIcon(null)
//            ).setLargeIcon(bitmap);

        }

        notificationManager.notify(1, builder.build());
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            Log.e("awesome", "Error in getting notification image: " + e.getLocalizedMessage());
            return null;
        }
    }

    public String fetchGambar(String html){
        try {

            Document document = Jsoup.parse(html);
            String image = document.select("img").attr("src").replace("w200-h113", "");
            Log.i("NotificationTest", image);
            return image;
        }catch (NullPointerException n) {
            n.getMessage();
            return "";
        }
    }

    public void savePref(String url){
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("link", String.valueOf(url));
        editor.commit();
    }

    private String chekPref(){
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        try {

            return  pref.getString("link",null);
        }catch (NullPointerException n){
            return "";
        }
    }


    public void showNotification(String message) {
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.putExtra("from", "Notif");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0 /* Request code */, notificationIntent,
                PendingIntent.FLAG_ONE_SHOT);

        String id = "main channel";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager)  getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            CharSequence name = "Channel Name";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(id, name, importance);
            notificationChannel.setDescription(description);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            if (notificationManager != null) {

                notificationManager.createNotificationChannel(notificationChannel);

            }
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), id);
        notificationBuilder.setSmallIcon(R.mipmap.ic_logo);
        notificationBuilder.setContentTitle(getString(R.string.app_name));
        notificationBuilder.setContentText(message);
        notificationBuilder.setLights(Color.WHITE, 500, 5000);
        notificationBuilder.setColor(getResources().getColor(R.color.primary));
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(1400, notificationBuilder.build());


    }
}
