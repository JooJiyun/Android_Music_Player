package com.jiyun.classicalmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.autofill.AutofillId;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.jiyun.classicalmusic.Audio.AudioApplication;
import com.jiyun.classicalmusic.DB.MainAdapter;
import com.jiyun.classicalmusic.DB.MusicsInfo;
import com.jiyun.classicalmusic.DB.PlaylistDBHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    private AdView mAdView;

    private PlaylistDBHelper playlistDBHelper;

    private int playlist_id;
    private int music_pos;
    private boolean music_exist = false;
    private ArrayList<Integer> music_list;

    TextView txt_main_title_list, txt_main_title_music,  txt_main_time_end;
    ImageView img_main;
    ImageButton btn_main_show_list;
    ImageButton btn_main_prev_music, btn_main_stop_music, btn_main_next_music, btn_main_cycle_music;
    ListView list_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        // region AD

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { } });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // endregion


        // region match vars

        txt_main_title_list = findViewById(R.id.txt_main_title_list);
        txt_main_title_music = findViewById(R.id.txt_main_title_music);
        txt_main_time_end = findViewById(R.id.txt_main_time_end);

        btn_main_show_list = findViewById(R.id.btn_main_show_list);
        btn_main_prev_music = findViewById(R.id.btn_main_prev_music);
        btn_main_stop_music = findViewById(R.id.btn_main_stop_music);
        btn_main_next_music = findViewById(R.id.btn_main_next_music);
        btn_main_cycle_music = findViewById(R.id.btn_main_cycle_music);

        img_main = findViewById(R.id.img_main);
        list_main = findViewById(R.id.list_main);

        // endregion

        // region initial values, db, ui

        playlistDBHelper = new PlaylistDBHelper(getApplicationContext(), "PlayList.db",null,1);

        music_pos = 0;
        playlist_id = -1;
        music_exist = false;

        changePlaylist(playlist_id);
        changeMusic();
        update_list();

        // endregion

        // region button listeners
        btn_main_show_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShowListActivity.class);
                startActivity(intent);
            }
        });

        btn_main_prev_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music_list ==null){
                    Toast.makeText(getApplicationContext(),"There are no previous songs.",Toast.LENGTH_SHORT).show();
                    return;
                }
                music_pos--;
                music_pos = AudioApplication.getInstance().getServiceInterface().play(music_pos);
                changeMusic();
            }
        });
        btn_main_next_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music_list ==null){
                    Toast.makeText(getApplicationContext(),"There are no previous songs.",Toast.LENGTH_SHORT).show();
                    return;
                }
                music_pos++;
                music_pos = AudioApplication.getInstance().getServiceInterface().play(music_pos);
                changeMusic();
            }
        });
        btn_main_stop_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playToggle();
            }
        });
        btn_main_cycle_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AudioApplication.getInstance().getServiceInterface().isLooping()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        btn_main_cycle_music.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_gray)));
                    }
                    Toast.makeText(getApplicationContext(),"Repeat all songs in the playlist.",Toast.LENGTH_SHORT).show();
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        btn_main_cycle_music.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_700)));
                    }
                    Toast.makeText(getApplicationContext(),"Repeat this song only.",Toast.LENGTH_SHORT).show();
                }
                AudioApplication.getInstance().getServiceInterface().toggleLoop();
            }
        });

        list_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                music_pos = position;
                music_pos = AudioApplication.getInstance().getServiceInterface().play(music_pos);
                changeMusic();
            }
        });

        // endregion


        //txt_main_title_list.setSingleLine(true);    // 한줄로 표시하기
        //txt_main_title_list.setEllipsize(TextUtils.TruncateAt.MARQUEE); // 흐르게 만들기
        //txt_main_title_list.setSelected(true); // 항상 선택되게 -> 선택되야 marquee 작동됨

        txt_main_title_music.setSingleLine(true);    // 한줄로 표시하기
        txt_main_title_music.setEllipsize(TextUtils.TruncateAt.MARQUEE); // 흐르게 만들기
        txt_main_title_music.setSelected(true); // 항상 선택되게 -> 선택되야 marquee 작동됨


        onRestart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        music_pos = intent.getIntExtra("music_pos",0);
        playlist_id = intent.getIntExtra("playlist_id",-1);
        boolean show = intent.getBooleanExtra("show",true);
        if(playlist_id==-1)music_exist = false;
        changeMusic();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        music_pos = AudioApplication.getInstance().getServiceInterface().getMusicPos();
        playlist_id = AudioApplication.getInstance().getServiceInterface().getPlaylistId();
        changePlaylist(playlist_id);
    }

    private void playToggle(){
        if(!music_exist){
            Toast.makeText(getApplicationContext(),"There are no songs.", Toast.LENGTH_SHORT).show();
            return;
        }
        AudioApplication.getInstance().getServiceInterface().togglePlay();
        if(AudioApplication.getInstance().getServiceInterface().isPlaying()){
            btn_main_stop_music.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_baseline_pause_24));
        }
        else{
            btn_main_stop_music.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_baseline_play_arrow_24));
        }
    }

    private void changeMusic(){
        if(!music_exist){
            txt_main_time_end.setText("00:00");
            txt_main_title_music.setText("not selected");
            img_main.setImageResource(R.drawable.music_thumbs);
            music_pos = 0;
            return;
        }
        int music_no = playlistDBHelper.getData(playlist_id).getMusics_list().get(music_pos);
        txt_main_title_music.setText(MusicsInfo.getTitle(music_no));
        txt_main_time_end.setText(MusicsInfo.getTime(music_no));
        img_main.setImageResource(MusicsInfo.getImg(music_no));

        update_list();
    }

    private void changePlaylist(int new_playlist_id){
        playlist_id = new_playlist_id;
        if(playlistDBHelper.getData(playlist_id)==null){
            txt_main_title_list.setText("not selected");
            music_pos = 0;
            playlist_id = -1;
            music_exist = false;
            changeMusic();
            update_list();
            return;
        }
        txt_main_title_list.setText(playlistDBHelper.getData(playlist_id).getTitle());
        music_list = playlistDBHelper.getData(playlist_id).getMusics_list();
        AudioApplication.getInstance().getServiceInterface().setPlayList(music_list, playlist_id);
        if(music_list.size()==0){
            music_exist = false;
            changeMusic();
            update_list();
            return;
        }

        music_exist = true;
        changeMusic();
        update_list();
    }

    private void update_list(){
        if(AudioApplication.getInstance().getServiceInterface().isPlaying()){
            btn_main_stop_music.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_baseline_pause_24));
        }
        else{
            btn_main_stop_music.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_baseline_play_arrow_24));
        }
        if(AudioApplication.getInstance().getServiceInterface().isLooping()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btn_main_cycle_music.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_700)));
            }
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btn_main_cycle_music.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_gray)));
            }
        }

        if(playlistDBHelper.getData(playlist_id)==null){
            MainAdapter adapter = new MainAdapter(this, R.layout.item_main_playlist, new ArrayList<>(),  0);
            list_main.setAdapter(adapter);
            return;
        }
        music_list = playlistDBHelper.getData(playlist_id).getMusics_list();
        MainAdapter adapter = new MainAdapter(this, R.layout.item_main_playlist, music_list,  music_pos);
        list_main.setAdapter(adapter);
    }

}