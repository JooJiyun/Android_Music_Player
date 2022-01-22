package com.jiyun.classicalmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiyun.classicalmusic.Audio.AudioApplication;
import com.jiyun.classicalmusic.DB.DetailAdapter;
import com.jiyun.classicalmusic.DB.MusicsInfo;
import com.jiyun.classicalmusic.DB.PlaylistDBHelper;

import java.util.ArrayList;

public class DetailListActivity extends AppCompatActivity implements DetailAdapter.ListBtnClickListener {

    private PlaylistDBHelper playlistDBHelper;
    ArrayList<Integer> music_list;
    private int playlist_id_picked;

    private TextView txt_detail_title;
    private ListView list_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_list);

        txt_detail_title = findViewById(R.id.txt_detail_title);
        list_detail = findViewById(R.id.list_detail);
        ImageButton btn_detail_add_music = findViewById(R.id.btn_detail_add_music);
        ImageButton btn_detail_rename = findViewById(R.id.btn_detail_rename);

        playlistDBHelper = new PlaylistDBHelper(DetailListActivity.this, "PlayList.db",null,1);

        playlist_id_picked = getIntent().getIntExtra("PlaylistId",0);
        if(playlist_id_picked==AudioApplication.getInstance().getServiceInterface().getPlaylistId()){
            Toast.makeText(DetailListActivity.this, "Playlists that are being edited cannot be played.",Toast.LENGTH_SHORT);
            AudioApplication.getInstance().getServiceInterface().pause();
        }
        if(playlistDBHelper.getData(playlist_id_picked)==null){
            Toast.makeText(DetailListActivity.this,"The selected playlist does not exist.",Toast.LENGTH_SHORT).show();
            finish();
        }
        music_list = playlistDBHelper.getData(playlist_id_picked).getMusics_list();

        txt_detail_title.setText(playlistDBHelper.getData(playlist_id_picked).getTitle());

        btn_detail_rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // title change dialog
                final EditText edit_detail_new_title = new EditText(DetailListActivity.this);

                FrameLayout container = new FrameLayout(DetailListActivity.this);
                FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

                edit_detail_new_title.setLayoutParams(params);

                container.addView(edit_detail_new_title);

                final AlertDialog.Builder alt_bld = new AlertDialog.Builder(DetailListActivity.this,R.style.MyAlertDialogStyle);

                alt_bld.setTitle("change title")
                        .setMessage("Please enter a new name for the playlist.")
                        .setIcon(R.drawable.ic_baseline_pencil)
                        .setCancelable(true)
                        .setView(container)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String new_title = edit_detail_new_title.getText().toString();
                                txt_detail_title.setText(new_title);
                                playlistDBHelper.modify_title(playlist_id_picked, new_title);
                            }
                        });

                AlertDialog alert = alt_bld.create();
                alert.show();
            }
        });
        btn_detail_add_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetailListActivity.this, R.style.MyAlertDialogStyle)
                        .setTitle("select music")
                        .setCancelable(true)
                        .setItems(MusicsInfo.Titles, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(music_list==null){
                                    music_list = new ArrayList<Integer>();
                                }
                                music_list.add(which);
                                String music_string = "";
                                for(int i =0;i<music_list.size();i++){
                                    music_string += music_list.get(i).toString();
                                    music_string += "-";
                                }
                                if(playlistDBHelper.getData(playlist_id_picked)==null){
                                    Toast.makeText(DetailListActivity.this,"The selected playlist does not exist.",Toast.LENGTH_SHORT);
                                    finish();
                                }
                                playlistDBHelper.modify_music_list(playlist_id_picked, music_string);
                                update_list();
                            } })
                        .setNeutralButton("cancel", null).show();
            }
        });

        update_list();
    }

    private void update_list(){
        if(playlistDBHelper.getData(playlist_id_picked)==null){
            Toast.makeText(DetailListActivity.this,"The selected playlist does not exist.",Toast.LENGTH_SHORT);
            return;
        }
        music_list = playlistDBHelper.getData(playlist_id_picked).getMusics_list();
        DetailAdapter adapter = new DetailAdapter(DetailListActivity.this, R.layout.item_detail_music, music_list,DetailListActivity.this);
        list_detail.setAdapter(adapter);
    }

    @Override
    public void onListBtnClick(int position, int resourceId) {
        music_list.remove(position);
        String music_string = "";
        for(int i =0;i<music_list.size();i++){
            music_string+=music_list.get(i).toString();
            music_string += "-";
        }
        playlistDBHelper.modify_music_list(playlist_id_picked, music_string);
        update_list();
    }


}