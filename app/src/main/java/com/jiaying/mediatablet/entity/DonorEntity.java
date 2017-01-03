package com.jiaying.mediatablet.entity;

import android.util.Log;

import com.jiaying.mediatablet.db.IdataPreference;
import com.jiaying.mediatablet.utils.BitmapUtils;

/**
 * Created by hipil on 2016/5/17.
 */
public class DonorEntity {

    private static DonorEntity donorEntity = new DonorEntity();

    private IdataPreference dataPreference;

    public synchronized static DonorEntity getInstance() {
        if (donorEntity == null) {
            donorEntity = new DonorEntity();
        }
        return donorEntity;
    }

    public void setDataPreference(IdataPreference dataPreference) {
        this.dataPreference = dataPreference;
    }

    public void setIdentityCard(PersonInfo identityCard) {
        writePersonIdInfo(identityCard);
    }

    public void setDocument(PersonInfo document) {
        writePersonDocInfo(document);
    }

    public PersonInfo getIdentityCard() {
        PersonInfo identityCard = new PersonInfo();
        readPersonIdInfo(identityCard);
        return identityCard;
    }

    public PersonInfo getDocument() {
        PersonInfo document = new PersonInfo();
        readPersonDocInfo(document);
        return document;
    }

    private DonorEntity() {

    }

    // TODO: 2016/5/17 照片没有处理
    private void writePersonIdInfo(PersonInfo personInfo) {

        Log.e("error", "=====writePersonIdInfo=====");
        this.dataPreference.writeStr("iaddress", personInfo.getAddress());
        this.dataPreference.writeStr("ibirth_year", personInfo.getBirth_year());
        this.dataPreference.writeStr("ibirth_month", personInfo.getBirth_month());
        this.dataPreference.writeStr("ibirth_day", personInfo.getBirth_day());
        if (personInfo.getFaceBitmap() != null) {
            this.dataPreference.writeStr("ifaceBitmap", BitmapUtils.bitmapToBase64(personInfo.getFaceBitmap()));
        }

        this.dataPreference.writeStr("igender", personInfo.getGender());
        this.dataPreference.writeStr("iid", personInfo.getId());
        this.dataPreference.writeStr("iname", personInfo.getName());
        this.dataPreference.writeStr("ination", personInfo.getNation());
        this.dataPreference.commit();
    }

    // TODO: 2016/5/17 照片没有处理
    private void readPersonIdInfo(PersonInfo personInfo) {


        personInfo.setAddress(this.dataPreference.readStr("iaddress"));
        personInfo.setBirth_year(this.dataPreference.readStr("ibirth_year"));
        personInfo.setBirth_month(this.dataPreference.readStr("ibirth_month"));
        personInfo.setBirth_day(this.dataPreference.readStr("ibirth_day"));
        if (!this.dataPreference.readStr("dfaceBitmap").equals("wrong")) {
            personInfo.setFaceBitmap(BitmapUtils.base64ToBitmap(this.dataPreference.readStr("ifaceBitmap")));
        }
        personInfo.setGender(this.dataPreference.readStr("igender"));
        personInfo.setId(this.dataPreference.readStr("iid"));
        personInfo.setName(this.dataPreference.readStr("iname"));
        personInfo.setNation(this.dataPreference.readStr("ination"));
    }

    private void writePersonDocInfo(PersonInfo personInfo) {

        Log.e("error", "=====writePersonDocInfo=====");
        this.dataPreference.writeStr("daddress", personInfo.getAddress());
        this.dataPreference.writeStr("dbirth_year", personInfo.getBirth_year());
        this.dataPreference.writeStr("dbirth_month", personInfo.getBirth_month());
        this.dataPreference.writeStr("dbirth_day", personInfo.getBirth_day());
        if (personInfo.getFaceBitmap() != null) {
            this.dataPreference.writeStr("dfaceBitmap", BitmapUtils.bitmapToBase64(personInfo.getFaceBitmap()));
        }
        this.dataPreference.writeStr("dgender", personInfo.getGender());
        this.dataPreference.writeStr("did", personInfo.getId());
        this.dataPreference.writeStr("dname", personInfo.getName());
        this.dataPreference.writeStr("dnation", personInfo.getNation());
        this.dataPreference.commit();
    }

    private void readPersonDocInfo(PersonInfo personInfo) {


        personInfo.setAddress(this.dataPreference.readStr("daddress"));
        personInfo.setBirth_year(this.dataPreference.readStr("dbirth_year"));
        personInfo.setBirth_month(this.dataPreference.readStr("dbirth_month"));
        personInfo.setBirth_day(this.dataPreference.readStr("dbirth_day"));

        if (!this.dataPreference.readStr("dfaceBitmap").equals("wrong")) {
            personInfo.setFaceBitmap(BitmapUtils.base64ToBitmap(this.dataPreference.readStr("dfaceBitmap")));

        }
        personInfo.setGender(this.dataPreference.readStr("dgender"));
        personInfo.setId(this.dataPreference.readStr("did"));
        personInfo.setName(this.dataPreference.readStr("dname"));
        personInfo.setNation(this.dataPreference.readStr("dnation"));
    }


}
