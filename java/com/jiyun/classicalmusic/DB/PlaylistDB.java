package com.jiyun.classicalmusic.DB;

import java.util.ArrayList;

public class PlaylistDB {
    int _id;
    private String title;
    private String musics;

    public PlaylistDB(int _id, String title, String musics) {
        this._id = _id;
        this.title = title;
        this.musics = musics;
    }

    public int get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Integer> getMusics_list(){
        ArrayList result = new ArrayList<Integer>();

        if(musics.length()==0){
            return result;
        }
        String[] str_split = musics.split("-");
        for(String str : str_split){
            result.add(Integer.parseInt(str));
        }

        return result;
    }

    public String update_musics(ArrayList _musics){
        String result = "";
        for(int i = 0;i<_musics.size();i++){
            result+=_musics.get(i);
        }
        this.musics = result;
        return result;
    }
}
