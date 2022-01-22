package com.jiyun.classicalmusic.Audio;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.jiyun.classicalmusic.DB.MusicsInfo;
import com.jiyun.classicalmusic.MainActivity;
import com.jiyun.classicalmusic.R;

public class NotificationPlayer {
    private final static int NOTIFICATION_PLAYER_ID = 0x342;
    private AudioService mService;
    private NotificationManager mNotificationManager;
    private NotificationManagerBuilder mNotificationManagerBuilder;
    private boolean isForeground;

    public NotificationPlayer(AudioService service) {
        mService = service;
        mNotificationManager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void updateNotificationPlayer() {
        cancel();
        mNotificationManagerBuilder = new NotificationManagerBuilder();
        mNotificationManagerBuilder.execute();
    }

    public void removeNotificationPlayer() {
        cancel();
        mService.stopForeground(true);
        isForeground = false;
    }

    private void cancel() {
        if (mNotificationManagerBuilder != null) {
            mNotificationManagerBuilder.cancel(true);
            mNotificationManagerBuilder = null;
        }
    }

        private class NotificationManagerBuilder extends AsyncTask<Void, Void, Notification> {
        private RemoteViews mRemoteViews;
        private NotificationCompat.Builder mNotificationBuilder;
        private PendingIntent mMainPendingIntent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Intent mainActivity = new Intent(mService, MainActivity.class);
            mMainPendingIntent = PendingIntent.getActivity(mService, 0, mainActivity, 0);
            mRemoteViews = createRemoteView(R.layout.notification_player);

            //mNotificationBuilder = new NotificationCompat.Builder(mService);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelID="ClassicalMusic"; //알림채널 식별자
                String channelName="ClassicalMusicPlayer"; //알림채널의 이름(별명)
                NotificationChannel channel= new NotificationChannel(channelID,channelName,NotificationManager.IMPORTANCE_DEFAULT);
                mNotificationManager.createNotificationChannel(channel);
                mNotificationBuilder = new NotificationCompat.Builder(mService, channelID);
            }
            else { mNotificationBuilder = new NotificationCompat.Builder(mService); }


            mNotificationBuilder.setSmallIcon(R.mipmap.icon)
                    .setOngoing(true)
                    .setContentIntent(mMainPendingIntent)
                    .setContent(mRemoteViews);

            Notification notification = mNotificationBuilder.build();
            notification.priority = Notification.PRIORITY_MAX;
            notification.contentIntent = mMainPendingIntent;
            if (!isForeground) {
                isForeground = true;
                // 서비스를 Foreground 상태로 만든다
                mService.startForeground(NOTIFICATION_PLAYER_ID, notification);
            }
        }

        @Override
        protected Notification doInBackground(Void... params) {
            mNotificationBuilder.setContent(mRemoteViews);
            mNotificationBuilder.setContentIntent(mMainPendingIntent);
            mNotificationBuilder.setPriority(Notification.PRIORITY_MAX);
            Notification notification = mNotificationBuilder.build();
            updateRemoteView(mRemoteViews, notification);
            return notification;
        }

        @Override
        protected void onPostExecute(Notification notification) {
            super.onPostExecute(notification);
            try {
                mNotificationManager.notify(NOTIFICATION_PLAYER_ID, notification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private RemoteViews createRemoteView(int layoutId) {
            RemoteViews remoteView = new RemoteViews(mService.getPackageName(), layoutId);
            Intent actionTogglePlay = new Intent(CommandActions.TOGGLE_PLAY);
            Intent actionForward = new Intent(CommandActions.FORWARD);
            Intent actionRewind = new Intent(CommandActions.REWIND);
            Intent actionClose = new Intent(CommandActions.CLOSE);
            PendingIntent togglePlay = PendingIntent.getService(mService, 0, actionTogglePlay, 0);
            PendingIntent forward = PendingIntent.getService(mService, 0, actionForward, 0);
            PendingIntent rewind = PendingIntent.getService(mService, 0, actionRewind, 0);
            PendingIntent close = PendingIntent.getService(mService, 0, actionClose, 0);

            remoteView.setOnClickPendingIntent(R.id.btn_nof_pause, togglePlay);
            remoteView.setOnClickPendingIntent(R.id.btn_nof_forward, forward);
            remoteView.setOnClickPendingIntent(R.id.btn_nof_rewind, rewind);
            remoteView.setOnClickPendingIntent(R.id.btn_nof_close, close);
            return remoteView;
        }

        private void updateRemoteView(RemoteViews remoteViews, Notification notification) {
            if (mService.isPlaying()) {
                remoteViews.setImageViewResource(R.id.btn_nof_pause, R.drawable.ic_baseline_pause_24);
            } else {
                remoteViews.setImageViewResource(R.id.btn_nof_pause, R.drawable.ic_baseline_play_arrow_24);
            }

            int music_idx = AudioApplication.getInstance().getServiceInterface().getMusic_idx();
            String title;
            int thumb;
            if(music_idx==-1){
                title = "not selected";
                thumb = R.drawable.music_thumbs;
            }
            else{
                title = MusicsInfo.getTitle(music_idx);
                thumb = MusicsInfo.getImg(music_idx);
            }
            remoteViews.setTextViewText(R.id.txt_nof_title, title);
            remoteViews.setImageViewResource(R.id.img_nof_thumb, thumb);
        }

    }
}
