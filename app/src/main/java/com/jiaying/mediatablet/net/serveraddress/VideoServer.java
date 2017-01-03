package com.jiaying.mediatablet.net.serveraddress;

import com.jiaying.mediatablet.db.IdataPreference;


/**
 * Created by hipil on 2016/5/6.
 */
public class VideoServer extends AbstractServer {

    private static VideoServer videoServer = new VideoServer();
    private IdataPreference idataPreference;

    private VideoServer() {
    }

    public static VideoServer getInstance() {
        if (videoServer == null) {
            videoServer = new VideoServer();
        }
        return videoServer;
    }

    public void setIdataPreference(IdataPreference idataPreference) {
        this.idataPreference = idataPreference;
    }

    @Override
    public String getIp() {
        return idataPreference.readStr("video_server_ip");
    }

    @Override
    public int getPort() {
        return idataPreference.readInt("video_server_port");
    }

    @Override
    public void setIp(String ip) {
        this.idataPreference.writeStr("video_server_ip", ip);
        this.idataPreference.commit();
    }

    @Override
    public void setPort(int port) {
        this.idataPreference.writeInt("video_server_port", port);
        this.idataPreference.commit();
    }
}
