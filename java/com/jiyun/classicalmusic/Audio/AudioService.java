package com.jiyun.classicalmusic.Audio;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.jiyun.classicalmusic.DB.MusicsInfo;
import com.jiyun.classicalmusic.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class AudioService extends Service {
    private MediaPlayer mMediaPlayer;
    private final IBinder mBinder = new AudioServiceBinder();


    private NotificationPlayer mNotificationPlayer;
    private void updateNotificationPlayer() {
        if (mNotificationPlayer != null) {
            mNotificationPlayer.updateNotificationPlayer();
        }
    }

    private void removeNotificationPlayer() {
        if (mNotificationPlayer != null) {
            mNotificationPlayer.removeNotificationPlayer();
        }
    }


    private int music_pos;
    private int playlist_id;
    private int music_idx;
    private ArrayList<Integer> music_list;
    private boolean isLoop, prepared;


    public class AudioServiceBinder extends Binder {
        AudioService getService() {
            return AudioService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mMediaPlayer = new MediaPlayer();

        music_pos = 0;
        playlist_id = -1;
        music_idx = -1;
        music_list = new ArrayList<Integer>();
        isLoop = false;
        prepared = false;

        mNotificationPlayer = new NotificationPlayer(this);
    }

    public void prepare(){
        prepared = true;
        mMediaPlayer.setLooping(isLoop);
        mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK); // 절전모드 같은거에도 방해받지 않기 위한 락
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                music_pos++;
                play(music_pos);
                broadcast();
            }
        });
    }

    @Override public void onDestroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        super.onDestroy();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (CommandActions.TOGGLE_PLAY.equals(action)) {
                if (isPlaying()) {
                    pause();
                } else {
                    play();
                }
            } else if (CommandActions.REWIND.equals(action)) {
                play(music_pos+1);
            } else if (CommandActions.FORWARD.equals(action)) {
                play(music_pos-1);
            } else if (CommandActions.CLOSE.equals(action)) {
                pause();
                removeNotificationPlayer();
            }
            broadcast();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void broadcast(){
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = activityManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo running = info.get(0);
        ComponentName componentName = running.topActivity;
        if(!componentName.getClassName().equals("com.jiyun.classicalmusic.MainActivity")){
            return;
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("music_pos",music_pos);
        intent.putExtra("playlist_id",playlist_id);
        startActivity(intent);
    }

    public void reset() {
        if(mMediaPlayer!=null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            removeNotificationPlayer();
        }
    }

    public void setMusicList(ArrayList<Integer> music_list, int playlist_id) {
        this.playlist_id = playlist_id;
        this.music_list = music_list;
        this.music_pos = 0;
        if(music_list.size()==0){
            this.music_idx = -1;
        }
        else{
            this.music_idx = music_list.get(0);
        }
    }

    public int play(int music_pos) {
        if(music_list==null){
            Toast.makeText(getApplicationContext(), "(error)music list is null.",Toast.LENGTH_SHORT).show();
            music_idx = -1;
            updateNotificationPlayer();
            return 0;
        }
        if(music_list.size()==0){
            Toast.makeText(getApplicationContext(), "The playlist is empty.",Toast.LENGTH_SHORT).show();
            music_idx = -1;
            updateNotificationPlayer();
            return 0;
        }
        if(music_pos>=music_list.size()){
            music_pos = 0;
        }
        else if(music_pos<0){
            music_pos = music_list.size()-1;
        }
        this.music_pos = music_pos;
        music_idx = music_list.get(music_pos);

        reset();
        mMediaPlayer = MediaPlayer.create(this, MusicsInfo.getMusic(music_list.get(this.music_pos)));
        prepare();
        play();

        return this.music_pos;
    }
    public void play(){
        if(prepared){
            mMediaPlayer.start();
            updateNotificationPlayer();
        }
        else{
            play(music_pos);
        }
    }

    public void pause() {
        if(prepared) {
            mMediaPlayer.pause();
            updateNotificationPlayer();
        }
    }


    public boolean isPlaying() {
        if(!prepared)return false;
        return mMediaPlayer.isPlaying();
    }
    public boolean isLooping(){
        return isLoop;
    }

    public void setLoop(boolean flg){
        isLoop = flg;
        if(prepared) {
            mMediaPlayer.setLooping(flg);
        }
    }
    public int getPlaylist_id() {
        return this.playlist_id;
    }
    public int getMusic_pos(){
        return this.music_pos;
    }
    public int getMusic_idx() {
        return music_idx;
    }
}
