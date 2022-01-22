package com.jiyun.classicalmusic.DB;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jiyun.classicalmusic.R;

import java.util.List;

public class DetailAdapter extends ArrayAdapter implements View.OnClickListener{
    private ListBtnClickListener listBtnClickListener = null ;

    public interface ListBtnClickListener {
        void onListBtnClick(int position, int resourceid);
    }

    public DetailAdapter(@NonNull Context context, int resource, @NonNull List objects, ListBtnClickListener listBtnClickListener) {
        super(context, resource, objects);
        this.listBtnClickListener = listBtnClickListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_detail_music, parent, false);
        }

        ImageView img_detail_item_music = convertView.findViewById(R.id.img_detail_item_music);
        TextView txt_detail_item_title = convertView.findViewById(R.id.txt_detail_item_title);
        TextView txt_detail_item_time = convertView.findViewById(R.id.txt_detail_item_time);
        Button btn_detail_item_delete = convertView.findViewById(R.id.btn_detail_item_delete);

        int music_idx = (int) getItem(position);

        img_detail_item_music.setImageResource(MusicsInfo.getImg(music_idx));
        txt_detail_item_title.setText(MusicsInfo.getTitle(music_idx));
        txt_detail_item_time.setText(MusicsInfo.getTime(music_idx));

        btn_detail_item_delete.setTag(position);
        btn_detail_item_delete.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View view) {
        if (this.listBtnClickListener != null) {
            this.listBtnClickListener.onListBtnClick((int)view.getTag(), view.getId()) ;
        }
    }
}
