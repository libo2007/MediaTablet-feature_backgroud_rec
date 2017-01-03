package com.jiaying.mediatablet.db;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hipil on 2016/5/17.
 */
public class DataPreference implements IdataPreference {


    private Context con;

    private SharedPreferences setting;
    private SharedPreferences.Editor editor;


    public DataPreference(Context context) {
        this.con = context;

        this.setting = this.con.getSharedPreferences("preference_name", Context.MODE_PRIVATE);
        this.editor = setting.edit();
    }

    //    读接口
    @Override
    public boolean readBoolean(String key) {
        boolean b = this.setting.getBoolean(key, true);
        return b;
    }

    @Override
    public double readDouble(String key) {
        return this.setting.getFloat(key, -0.1f);
    }

    @Override
    public float readFloat(String key) {
        return this.setting.getFloat(key, -0.1f);
    }

    @Override
    public int readInt(String key) {
        return this.setting.getInt(key, -1);
    }

    @Override
    public long readLong(String key) {
        return this.setting.getLong(key, -1);
    }

    @Override
    public String readStr(String key) {
        return this.setting.getString(key, "wrong");
    }

    @Override
    public void writeBoolean(String key, boolean b) {
        this.editor.putBoolean(key, b);
    }

    @Override
    public void writeDouble(String key, double d) {

        this.editor.putFloat(key, (float) d);
    }

    @Override
    public void writeFloat(String key, float f) {
        this.editor.putFloat(key, f);
    }

    @Override
    public void writeInt(String key, int i) {
        this.editor.putInt(key, i);
    }

    @Override
    public void writeLong(String key, long l) {
        this.editor.putLong(key, l);
    }

    @Override
    public void writeStr(String key, String str) {
        this.editor.putString(key, str);
    }

    @Override
    public void commit() {
        this.editor.commit();
    }
}
