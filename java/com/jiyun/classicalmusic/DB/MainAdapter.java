package com.jiyun.classicalmusic.DB;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jiyun.classicalmusic.MainActivity;
import com.jiyun.classicalmusic.R;

import java.util.List;

public class MainAdapter extends ArrayAdapter {

    private int current_music_pos;

    public MainAdapter(@NonNull Context context, int resource, @NonNull List objects, int current_music_pos) {
        super(context, resource, objects);
        this.current_music_pos = current_music_pos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_main_playlist, parent, false);
        }

        ImageView img_main_item_playing = convertView.findViewById(R.id.img_main_item_playing);
        ImageView img_main_item = convertView.findViewById(R.id.img_main_item);
        TextView txt_main_item_title = convertView.findViewById(R.id.txt_main_item_title);
        TextView txt_main_item_time = convertView.findViewById(R.id.txt_main_item_time);

        int music_no = (int) getItem(position);

        if(position != current_music_pos){
            img_main_item_playing.setVisibility(View.INVISIBLE);
        }

        img_main_item.setImageResource(MusicsInfo.getImg(music_no));
        txt_main_item_title.setText(MusicsInfo.getTitle(music_no));
        txt_main_item_time.setText(MusicsInfo.getTime(music_no));

        return convertView;
    }

}
