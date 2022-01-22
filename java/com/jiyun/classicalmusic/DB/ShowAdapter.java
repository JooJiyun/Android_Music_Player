package com.jiyun.classicalmusic.DB;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jiyun.classicalmusic.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ShowAdapter extends ArrayAdapter implements View.OnClickListener{

    private int current_playlist_id;

    private ListBtnClickListener listBtnClickListener = null ;
    public interface ListBtnClickListener {
        void onListBtnClick(int position, int resourceid);
    }

    public ShowAdapter(@NonNull Context context, int resource, @NonNull List objects, int _id, ListBtnClickListener listBtnClickListener) {
        super(context, resource, objects);
        current_playlist_id = _id;
        this.listBtnClickListener = listBtnClickListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_show_list, parent, false);
        }

        ImageButton btn_pick_item_playing = convertView.findViewById(R.id.btn_pick_item_playing);
        TextView txt_show_item_title = convertView.findViewById(R.id.txt_show_item_title);
        Button btn_show_item_detail = convertView.findViewById(R.id.btn_show_item_detail);
        Button btn_show_item_delete = convertView.findViewById(R.id.btn_show_item_delete);

        PlaylistDB list_db = (PlaylistDB) getItem(position);

        if(list_db.get_id() == current_playlist_id){
            btn_pick_item_playing.setColorFilter(R.color.dark_gray);
        }
        else{
            btn_pick_item_playing.setColorFilter(R.color.teal_200);
        }

        txt_show_item_title.setText((list_db.getTitle()));


        btn_pick_item_playing.setTag(position);
        btn_pick_item_playing.setOnClickListener(this);

        btn_show_item_delete.setTag(position);
        btn_show_item_delete.setOnClickListener(this);

        btn_show_item_detail.setTag(position);
        btn_show_item_detail.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View view){
        if (this.listBtnClickListener != null) {
            this.listBtnClickListener.onListBtnClick((int)view.getTag(), view.getId()) ;
        }
    }
}
