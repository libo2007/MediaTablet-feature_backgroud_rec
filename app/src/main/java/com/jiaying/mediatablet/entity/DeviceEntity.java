package com.jiaying.mediatablet.entity;

import com.jiaying.mediatablet.db.IdataPreference;

/**
 * Created by hipil on 2016/4/25.
 */
public class DeviceEntity {
    private static DeviceEntity ourInstance = new DeviceEntity();
    private IdataPreference dataPreference;

    private DeviceEntity() {

    }

    public synchronized static DeviceEntity getInstance() {
        return ourInstance;
    }

    public void setDataPreference(IdataPreference dataPreference) {
        this.dataPreference = dataPreference;

    }

    public String getAp() {
        return this.dataPreference.readStr("ap");
    }

    public String getOrg() {
        return this.dataPreference.readStr("org");

    }

    public String getPassword() {
        return this.dataPreference.readStr("password");
    }

    public String getServerAp() {
        return this.dataPreference.readStr("serverAp");
    }

    public String getServerOrg() {
        return this.dataPreference.readStr("serverOrg");
    }

    public void setAp(String ap) {
        this.dataPreference.writeStr("ap", ap);
        this.dataPreference.commit();
    }

    public void setOrg(String org) {
        this.dataPreference.writeStr("org", org);
        this.dataPreference.commit();
    }

    public void setPassword(String password) {
        this.dataPreference.writeStr("password", password);
        this.dataPreference.commit();
    }

    public void setServerAp(String serverAp) {
        this.dataPreference.writeStr("serverAp", serverAp);
        this.dataPreference.commit();
    }

    public void setServerOrg(String serverOrg) {
        this.dataPreference.writeStr("serverOrg", serverOrg);
        this.dataPreference.commit();
    }
}
