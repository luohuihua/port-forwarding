package com.luohh.portforwarding.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 数据转发socket
 *
 * @author luohuihua
 */
@Slf4j
public class DataForwardingSocket extends Thread {

    /**
     * 标题
     */
    private String title;
    /**
     * 源Socket
     */
    private Socket sourceSocket;
    /**
     * 目标Socket
     */
    private Socket targetSocket;
    /**
     * 输入流
     */
    private InputStream in;
    /**
     * 超时时间
     */
    private Long timeOut;

    /**
     * 客户端一次数据
     */
    private byte[] data;

    private int readlen;

    public DataForwardingSocket(String title, Socket sourceSocket, Socket targetSocket, long timeOut, byte[] data, int readlen, InputStream in) {
        this.data = data;
        this.readlen = readlen;
        this.title = title;
        this.sourceSocket = sourceSocket;
        this.in = in;
        this.targetSocket = targetSocket;
        this.timeOut = timeOut;
    }

    public DataForwardingSocket(String title, Socket sourceSocket, Socket targetSocket, long timeOut) {
        this.title = title;
        this.sourceSocket = sourceSocket;
        this.targetSocket = targetSocket;
        this.timeOut = timeOut;
    }

    @Override
    public void run() {
        Long startTime = System.currentTimeMillis();
        try {
            sourceSocket.setSoTimeout(Math.toIntExact(timeOut));
            targetSocket.setSoTimeout(Math.toIntExact(timeOut));
            if (in == null) {
                in = sourceSocket.getInputStream();
                log.info(getTitle() + "连接线程开始，获取输入流!");
            }
            OutputStream out = targetSocket.getOutputStream();
            sourceSocket.setSoTimeout(Math.toIntExact(timeOut * 40));
            targetSocket.setSoTimeout(Math.toIntExact(timeOut * 40));
            Long lastTime = System.currentTimeMillis();
            log.info(getTitle() + "连接线程开始，获取输出流!");
            while (true) {
                if (sourceSocket.isClosed() || targetSocket.isClosed()) {
                    log.info("连接已关闭:" + title + "结束!");
                    break;
                }
                if (data == null) {
                    data = new byte[10240];
                } else {
//                    log.info(getTitle() + "连接正在写入内容!");
                    lastTime = System.currentTimeMillis();
                    out.write(data, 0, readlen);
                    out.flush();
                    data = null;
//                    log.info(getTitle() + "连接正在写入完成,时间" + (System.currentTimeMillis() - lastTime) / 1000 + "s!");
                    continue;
                }
//                log.info(getTitle() + "连接正在读取内容!");
                //读入数据
                readlen = in.read(data);
//                log.info(getTitle() + "连接读取到内容长度:" + readlen);
                // 连接过长时间没有读取到信息
                if (System.currentTimeMillis() - lastTime > timeOut * 60) {
                    log.info("连接过长时间没有读取到信息，连接:" + title + "结束");
                    break;
                }
                //没有数据，就暂停一下
                if (readlen <= 0) {
                    data = null;
                    Thread.sleep(300);
                    continue;
                } else {
                    lastTime = System.currentTimeMillis();
                }
//                log.info(getTitle() + "连接正在写入内容!");
                out.write(data, 0, readlen);
                out.flush();
                data = null;
//                log.info(getTitle() + "连接正在写入完成,时间" + (System.currentTimeMillis() - lastTime) / 1000 + "s!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage(), e);
        } finally {
            log.info(getTitle() + "连接结束,时长:" + (System.currentTimeMillis() - startTime) / 1000 + "s!");
            //关闭socket
            try {
                if (targetSocket != null) {
                    targetSocket.close();
                }
            } catch (Exception exx) {
            }
            try {
                if (sourceSocket != null) {
                    sourceSocket.close();
                }
            } catch (Exception exx) {
                log.info(exx.getMessage(), exx);
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Socket getSourceSocket() {
        return sourceSocket;
    }

    public void setSourceSocket(Socket sourceSocket) {
        this.sourceSocket = sourceSocket;
    }

    public Socket getTargetSocket() {
        return targetSocket;
    }

    public void setTargetSocket(Socket targetSocket) {
        this.targetSocket = targetSocket;
    }
}
