package com.jiaying.mediatablet.entity;

/**
 * Created by hipil on 2016/5/2.
 */
public class PlasmaWeightEntity {
    private static PlasmaWeightEntity ourInstance = new PlasmaWeightEntity();

    public static PlasmaWeightEntity getInstance() {
        return ourInstance;
    }

    private PlasmaWeightEntity() {
    }
    private int curWeight;

    public void setSettingWeight(int settingWeight) {
        this.settingWeight = settingWeight;
    }

    private int settingWeight;

    public int getSettingWeight() {
        return settingWeight;
    }

    public int getCurWeight() {
        return curWeight;
    }

    public void setCurWeight(int curWeight) {

        this.curWeight = curWeight;
    }
}
