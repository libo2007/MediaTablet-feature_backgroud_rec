package com.jiaying.mediatablet.db;

/**
 * Created by hipil on 2016/5/17.
 */
public interface IdataPreference {

    //读接口
    public abstract boolean readBoolean(String key);

    public abstract double readDouble(String key);

    public abstract float readFloat(String key);

    public abstract int readInt(String key);

    public abstract long readLong(String key);

    public abstract String readStr(String key);

    //写接口
    public abstract void writeBoolean(String key, boolean b);

    public abstract void writeDouble(String key, double d);

    public abstract void writeFloat(String key, float f);

    public abstract void writeInt(String key, int i);

    public abstract void writeLong(String key, long l);

    public abstract void writeStr(String key, String str);

    //提交
    public abstract void commit();
}
