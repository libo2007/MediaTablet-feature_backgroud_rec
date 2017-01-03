package com.jiaying.mediatablet.net.serveraddress;

import com.jiaying.mediatablet.db.IdataPreference;

/**
 * Created by hipil on 2016/5/6.
 */
public class SignalServer extends AbstractServer {

    private static SignalServer signalServer = new SignalServer();
    private IdataPreference idataPreference;

    private SignalServer() {
    }

    public static SignalServer getInstance() {
        if (signalServer == null) {
            signalServer = new SignalServer();
        }
        return signalServer;
    }

    public void setIdataPreference(IdataPreference idataPreference) {
        this.idataPreference = idataPreference;
    }

    @Override
    public String getIp() {
        return idataPreference.readStr("signal_server_ip");
    }

    @Override
    public int getPort() {
        return idataPreference.readInt("signal_server_port");
    }

    @Override
    public void setIp(String ip) {
        this.idataPreference.writeStr("signal_server_ip", ip);
        this.idataPreference.commit();
    }

    @Override
    public void setPort(int port) {
        this.idataPreference.writeInt("signal_server_port", port);
        this.idataPreference.commit();
    }
}
