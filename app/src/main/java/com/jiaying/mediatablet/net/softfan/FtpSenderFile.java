package com.jiaying.mediatablet.net.softfan;

/**
 * Created by Administrator on 2015/9/14 0014.
 */


import android.util.Log;

import com.jiaying.mediatablet.net.softfan.SoftFanFTPException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FtpSenderFile {

    public static int Socket_Buf_Size = 256 * 1024;

    public static int Server_Connect_TimeOut = 30000;
    public static int Client_Connect_TimeOut = 3000;

    public static int Server_Connect_SoLinger = 0;
    public static int Client_Connect_SoLinger = 2;

    private static final String RealDateTimeFormatString = "yyyy-MM-dd HH:mm:ss:SSS";
    private static SimpleDateFormat RealDateTimeFormat = new SimpleDateFormat(RealDateTimeFormatString);

    public static String toRealDateText(Date date) {
        synchronized (RealDateTimeFormat) {
            if (date == null) {
                return "";
            }
            return RealDateTimeFormat.format(date);
        }
    }

    private String host;
    private int port;

    public FtpSenderFile(String host, int port) {
        this.setHost(host);
        this.setPort(port);
    }

    public String send(String localFilePath, String remoteFilePath) throws SoftFanFTPException, IOException {
        File srcFile = new File(localFilePath);
        if (!(srcFile.isFile() || srcFile.isDirectory()))
            throw new SoftFanFTPException("不能打开文件: " + localFilePath);
        if (!srcFile.canRead())
            throw new SoftFanFTPException("不能读文件: " + localFilePath);

        InetAddress address = InetAddress.getByName(getHost());
        int port = getPort();
        Socket s;
        int idx = 0;
        while (true) {
            try {
                s = new Socket();
                Log.e("S", "error 2");
                s.setReuseAddress(true);
                s.setKeepAlive(true);
                s.setTcpNoDelay(true);
                s.setSoTimeout(Client_Connect_TimeOut);
                s.setSoLinger(false, Client_Connect_SoLinger);
                s.setReceiveBufferSize(Socket_Buf_Size + 1024);
                s.setSendBufferSize(Socket_Buf_Size + 1024);
                s.setPerformancePreferences(0, 1, 2);
                InetSocketAddress inetSocketAddress = new InetSocketAddress(address, port);
                s.connect(inetSocketAddress);
                break;
            } catch (IllegalArgumentException i) {
                Log.e("camera", "error 2");
            } catch (IOException e) {
                Log.e("传输视频文件代码IOException", " "+e.toString());
            }
            s = null;
            idx++;
            if (idx > 0) {
                throw new SoftFanFTPException("连接到 - " + getHost() + ":" + port + "  失败!");
            }
            synchronized (this) {
                try {
                    this.wait(500);
                } catch (InterruptedException e) {
                }
            }
        }

        try {
            DataInputStream in = new DataInputStream(s.getInputStream());
            try {
                DataOutputStream out = new DataOutputStream(s.getOutputStream());
                try {
                    // 发送文件信息
                    byte[] filePathBytes;
                    try {
                        filePathBytes = remoteFilePath.getBytes("GB2312");
                    } catch (UnsupportedEncodingException e) {
                        throw new SoftFanFTPException(e.getMessage());
                    }

                    int length = filePathBytes.length;
                    out.writeInt(length);
                    out.flush();
                    int pos = 0;
                    int count;

                    Log.e("ERROR", "发送文件信息");
                    while (pos < length) {

                        count = length - pos;
                        if (count > Socket_Buf_Size) {
                            count = Socket_Buf_Size;
                        }
                        out.write(filePathBytes, pos, count);
                        out.flush();
                        pos += count;
                    }

                    // 发送文件时间
                    byte[] fileDateBytes = toRealDateText(new Date(srcFile.lastModified())).getBytes();

                    length = fileDateBytes.length;
                    out.writeInt(length);
                    out.flush();
                    pos = 0;

                    Log.e("ERROR", "发送文件时间");
                    while (pos < length) {


                        count = length - pos;
                        if (count > Socket_Buf_Size) {
                            count = Socket_Buf_Size;
                        }
                        out.write(fileDateBytes, pos, count);
                        out.flush();
                        pos += count;
                    }

                    int res_code = in.readInt();
                    if (res_code == 0xFB) {
                        int end_code = 0xFFAA;
                        out.writeInt(end_code);
                        out.flush();
                        return new String("");
                    }

                    if (res_code != 0xFC) {
                        throw new SoftFanFTPException("系统错误");
                    }

                    // 发送数据段
                    length = (int) srcFile.length();
                    out.writeInt(length);
                    out.flush();

                    byte[] bodyData = new byte[Socket_Buf_Size];

                    java.io.FileInputStream fileIn = new java.io.FileInputStream(srcFile);
                    try {
                        pos = 0;

                        Log.e("ERROR", "发送文件数据");
                        while (pos < length) {

                            count = length - pos;
                            if (count > Socket_Buf_Size) {
                                count = Socket_Buf_Size;
                            }
                            if (count < 1)
                                break;
                            fileIn.read(bodyData, 0, count);
                            out.write(bodyData, 0, count);
                            out.flush();
                            pos += count;
                        }
                    } finally {
                        fileIn.close();
                    }

                    // 接收结果状态
                    res_code = in.readInt();
                    length = in.readInt();
                    byte[] res = null;
                    int end_code = 0xFFAA;
                    if (length > 0) {
                        res = new byte[length];
                        pos = 0;
                        while (pos < length) {
                            count = length - pos;
                            if (count > Socket_Buf_Size) {
                                count = Socket_Buf_Size;
                            }
                            count = in.read(res, pos, count);
                            pos += count;
                        }
                    }
                    out.writeInt(end_code);
                    out.flush();
                    if (res_code != 0) {
                        if (res == null)
                            throw new SoftFanFTPException("系统错误");
                        throw new SoftFanFTPException(new String(res, "GB2312"));
                    }
                    if (res == null)
                        return new String("");
                    return new String(res, "GB2312");
                } finally {
                    out.close();
                }
            } catch (Exception e) {
                Log.e("ERROR", "此处报错" + e.toString());
            } finally {
                in.close();
            }
        } finally {
            s.close();
        }
        return "";
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
