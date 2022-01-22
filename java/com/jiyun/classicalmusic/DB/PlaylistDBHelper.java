package com.jiyun.classicalmusic.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PlaylistDBHelper extends SQLiteOpenHelper {

    public PlaylistDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE if not exists PLAYLISTS ("
                + "_id integer primary key autoincrement,"
                + "title text,"
                + "musics text);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        db.execSQL("DROP TABLE IF EXISTS PLAYLISTS");
        onCreate(db);
        */
    }

    public void insert(String title){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO PLAYLISTS VALUES(null, '"+title+"', '' );");
        db.close();
    }

    public void remove(int _id){
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM PLAYLISTS WHERE _id = "+_id+";");
        db.close();
    }

    public void modify_music_list(int _id, String musics){
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("UPDATE PLAYLISTS SET musics = '"+musics+"' WHERE _id = "+_id+";");
        db.close();
    }
    public void modify_title(int _id, String title){
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("UPDATE PLAYLISTS SET title = '"+title+"' WHERE _id = "+_id+";");
        db.close();
    }

    public PlaylistDB getData(int _id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM PLAYLISTS WHERE _id = "+_id+";",null);
        if(c.moveToNext()){
            return new PlaylistDB(c.getInt(0),c.getString(1),c.getString(2));
        }
        return null;
    }

    public ArrayList getAllData(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList result = new ArrayList<PlaylistDB>();
        Cursor c = db.rawQuery("SELECT * FROM PLAYLISTS;",null);
        while(c.moveToNext()){
            result.add(new PlaylistDB(c.getInt(0), c.getString(1),c.getString(2)));
        }
        return result;
    }
}
