package com.jiyun.classicalmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.autofill.AutofillId;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.jiyun.classicalmusic.Audio.AudioApplication;
import com.jiyun.classicalmusic.DB.PlaylistDB;
import com.jiyun.classicalmusic.DB.PlaylistDBHelper;
import com.jiyun.classicalmusic.DB.ShowAdapter;

import java.util.ArrayList;

public class ShowListActivity extends AppCompatActivity implements ShowAdapter.ListBtnClickListener {

    private PlaylistDBHelper playlistDBHelper;
    private ArrayList<PlaylistDB> TotalPlaylist;
    private int playlist_id;

    private ImageButton btn_show_add_list;
    private ListView list_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_list);


        playlist_id = AudioApplication.getInstance().getServiceInterface().getPlaylistId();

        playlistDBHelper = new PlaylistDBHelper(getApplicationContext(), "PlayList.db",null,1);
        TotalPlaylist = playlistDBHelper.getAllData();

        btn_show_add_list = findViewById(R.id.btn_show_add_list);
        list_show = findViewById(R.id.list_show);

        btn_show_add_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlistDBHelper.insert("new playlist");
                update_List();
            }
        });

        update_List();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        update_List();
    }

    private void update_List(){
        TotalPlaylist = playlistDBHelper.getAllData();
        if(TotalPlaylist==null)return;
        ShowAdapter adapter = new ShowAdapter(this, R.layout.item_show_list, TotalPlaylist, playlist_id,this);
        list_show.setAdapter(adapter);
    }

    @Override
    public void onListBtnClick(int position, int resourceId) {
        int _id = (int)TotalPlaylist.get(position).get_id();

        switch(resourceId){
            case R.id.btn_pick_item_playing:
                AudioApplication.getInstance().getServiceInterface().setPlayList(TotalPlaylist.get(position).getMusics_list(), _id);
                AudioApplication.getInstance().getServiceInterface().reset();
                finish();
                break;
            case R.id.btn_show_item_detail:
                Intent intent = new Intent(getApplicationContext(), DetailListActivity.class);
                intent.putExtra("PlaylistId",_id);
                startActivity(intent);
                break;
            case R.id.btn_show_item_delete:
                playlistDBHelper.remove(_id);
                update_List();
                if(_id==playlist_id){
                    AudioApplication.getInstance().getServiceInterface().reset();
                }
                break;
        }
    }
}