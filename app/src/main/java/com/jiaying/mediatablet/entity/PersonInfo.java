package com.jiaying.mediatablet.entity;

import android.graphics.Bitmap;

/**
 * Created by hipil on 2016/5/17.
 */
public class PersonInfo {
    private String address;
    private String birth_year,birth_month,birth_day;
    private Bitmap faceBitmap;
    private String gender;
    private String id;
    private String name;
    private String nation;

    public PersonInfo(){}

    public PersonInfo(String address, String birth_year, String birth_month, String birth_day,
                      Bitmap faceBitmap, String gender, String id, String name, String nation) {
        this.address = address;
        this.birth_year = birth_year;
        this.birth_month = birth_month;
        this.birth_day = birth_day;
        this.faceBitmap = faceBitmap;
        this.gender = gender;
        this.id = id;
        this.name = name;
        this.nation = nation;
    }

    public String getAddress() {
        return address;
    }

    public String getBirth_year() {
        return birth_year;
    }

    public String getBirth_month() {
        return birth_month;
    }

    public String getBirth_day() {
        return birth_day;
    }

    public Bitmap getFaceBitmap() {
        return faceBitmap;
    }

    public String getGender() {
        return gender;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNation() {
        return nation;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirth_year(String birth_year) {
        this.birth_year = birth_year;
    }

    public void setBirth_month(String birth_month) {
        this.birth_month = birth_month;
    }

    public void setBirth_day(String birth_day) {
        this.birth_day = birth_day;
    }

    public void setFaceBitmap(Bitmap faceBitmap) {
        this.faceBitmap = faceBitmap;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }
}
