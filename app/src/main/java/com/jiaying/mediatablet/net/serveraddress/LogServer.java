package com.jiaying.mediatablet.net.serveraddress;

import com.jiaying.mediatablet.db.DataPreference;
import com.jiaying.mediatablet.db.IdataPreference;

/**
 * Created by hipil on 2016/5/6.
 */
public class LogServer extends AbstractServer {

    private static LogServer logServer = new LogServer();
    private IdataPreference idataPreference;

    private LogServer() {
    }

    public static LogServer getInstance() {
        if (logServer == null) {
            logServer = new LogServer();
        }

        return logServer;
    }

    public void setIdataPreference(IdataPreference idataPreference) {
        this.idataPreference = idataPreference;
    }

    @Override
    public String getIp() {
        return idataPreference.readStr("log_server_ip");
    }

    @Override
    public int getPort() {
        return idataPreference.readInt("log_server_port");
    }

    @Override
    public void setIp(String ip) {
        this.idataPreference.writeStr("log_server_ip", ip);
        this.idataPreference.commit();

    }

    @Override
    public void setPort(int port) {
        this.idataPreference.writeInt("log_server_port", port);
        this.idataPreference.commit();
    }
}
