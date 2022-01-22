package com.jiyun.classicalmusic.Audio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.ArrayList;

public class AudioServiceInterface {
    private ServiceConnection mServiceConnection;
    private AudioService mService;

    public AudioServiceInterface(Context context) {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = ((AudioService.AudioServiceBinder) service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mServiceConnection = null;
                mService = null;
            }
        };
        context.bindService(new Intent(context, AudioService.class)
                .setPackage(context.getPackageName()), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void setPlayList(ArrayList<Integer> music_list, int playlist_id){
        mService.setMusicList(music_list, playlist_id);
    }

    public void reset(){
        if(mService!=null){
            mService.reset();
        }
    }

    public int play(int music_pos) {
        if (mService != null) {
            return mService.play(music_pos);
        }
        return -1;
    }

    public void play() {
        if (mService != null) {
            mService.play();
        }
    }

    public void pause() {
        if (mService != null) {
            mService.pause();
        }
    }

    public void togglePlay() {
        if (isPlaying()) {
            pause();
        } else {
            play();
        }
    }

    public boolean isPlaying() {
        if (mService != null) {
            return mService.isPlaying();
        }
        return false;
    }

    public void toggleLoop(){
        mService.setLoop(!isLooping());
    }

    public boolean isLooping(){
        return mService.isLooping();
    }

    public int getMusicPos(){
        return mService.getMusic_pos();
    }

    public int getPlaylistId(){
        return mService.getPlaylist_id();
    }

    public int getMusic_idx() {
        if(mService!=null){
            return mService.getMusic_idx();
        }
        return -1;
    }
}

