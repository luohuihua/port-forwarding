package com.luohh.portforwarding.config;

import com.luohh.portforwarding.model.IpWhiteList;
import com.luohh.portforwarding.model.PortMapperAddress;
import com.luohh.portforwarding.service.IIpWhiteListService;
import com.luohh.portforwarding.service.IPortMapperAddressService;
import com.luohh.portforwarding.socket.DataForwardingSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author luohuihua
 */
@Component
@Slf4j
public class SocketServer implements CommandLineRunner {
    @Autowired
    private SocketConfigProperties socketConfigProperties;

    @Autowired
    private IPortMapperAddressService portMapperAddressService;

    @Autowired
    private IIpWhiteListService ipWhiteListService;

    public void start() {
        //加载映射
        List<PortMapperAddress> portMapperAddressList = portMapperAddressService.list();
        if (portMapperAddressList == null || portMapperAddressList.size() <= 0) {
            portMapperAddressService.saveBatch(socketConfigProperties.getPortMapperAddress());
        } else {
            socketConfigProperties.setPortMapperAddress(portMapperAddressList);
        }
        //加载地址白名单
        List<IpWhiteList> ipWhiteLists = ipWhiteListService.list();
        if (ipWhiteLists == null || ipWhiteLists.size() <= 0) {
            ipWhiteListService.saveBatch(socketConfigProperties.getIpWhiteLists());
        } else {
            socketConfigProperties.setIpWhiteLists(ipWhiteLists);
        }
        //一个端口转发
        if (socketConfigProperties.getPort() != null) {
            //启动本地监听端口
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(socketConfigProperties.getPort());
                final String localAddress = InetAddress.getLocalHost().getHostAddress() + ":" + socketConfigProperties.getPort();
                log.info("socket:" + localAddress + "启动");
                //线程池,无界的任务队列,
                //使用无界任务队列，线程池的任务队列可以无限制的添加新的任务，而线程池创建的最大线程数量就是你corePoolSize设置的数量，
                //也就是说在这种情况下maximumPoolSize这个参数是无效的，哪怕你的任务队列中缓存了很多未执行的任务，当线程池的线程数达到corePoolSize后，就不会再增加了；
                //若后续有新的任务加入，则直接进入队列等待，当使用这种任务队列模式时，一定要注意你任务提交与处理之间的协调与控制，不然会出现队列中的任务由于无法及时处理导致一直增长，直到最后资源耗尽的问题。
                ThreadPoolExecutor pool = new ThreadPoolExecutor(
                        socketConfigProperties.getPoolCore(),
                        socketConfigProperties.getPoolMax(),
                        socketConfigProperties.getPoolKeepAliveTime(),
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>(),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.AbortPolicy()
                );
                while (true) {
                    try {
                        //获取客户端连接
                        final Socket clientSocket = serverSocket.accept();
                        //访问地址
                        String hostAddress = clientSocket.getInetAddress().getHostAddress();
                        //是否通过
                        boolean isPass = false;
                        //循环见检查白名单
                        A:
                        for (IpWhiteList ipWhiteList : socketConfigProperties.getIpWhiteLists()) {
                            //全部
                            if (ipWhiteList.getIp().equals("all")) {
                                isPass = true;
                            } else {
                                //访问IP
                                String[] hostIps = hostAddress.split("\\.");
                                //白名单IP
                                String[] ips = ipWhiteList.getIp().split("\\.");
                                if (hostIps.length == ips.length) {
                                    //是否允许
                                    boolean allow = true;
                                    for (int i = 0; i < hostIps.length; i++) {
                                        String hostIp = hostIps[i];
                                        String ip = ips[i];
                                        if (!ip.equals("*") && !hostIp.equals(ip)) {
                                            allow = false;
                                        }
                                    }
                                    //通过白名单
                                    if (allow) {
                                        isPass = allow;
                                        break A;
                                    }
                                }
                            }
                        }
                        //不通过
                        if (!isPass) {
                            log.info("接收客户端:" + hostAddress + "连接失败，非白名单内IP");
                            clientSocket.close();
                            continue;
                        }
                        log.info("接收客户端:" + hostAddress + "连接成功");
                        InputStream inputStream = clientSocket.getInputStream();
                        byte[] data = new byte[10240];
                        Thread.sleep(300);
                        int readlen = inputStream.read(data);
                        String dataStr = new String(data, "UTF-8");
                        log.info("读取到头部连接内容:" + dataStr);
                        boolean isClose = true;
                        for (PortMapperAddress portMapperAddress : socketConfigProperties.getPortMapperAddress()) {
                            if (portMapperAddress.getServiceName() == null || dataStr.indexOf(portMapperAddress.getServiceName() + ")") == -1) {
                                continue;
                            }
//                            clientSocket.close();
                            isClose = false;
                            log.info("开始连接服务:" + portMapperAddress.getServiceName());
                            final String targetAddress = portMapperAddress.getTargetAddress();
                            if (!portMapperAddress.getEnable()) {
                                log.info("目标:" + targetAddress + "转发停止中。。。。。");
                                clientSocket.close();
                                continue;
                            }
                            new Thread(() -> {
                                //建立远程连接
                                Socket remoteServerSocket = null;
                                try {
                                    //实例化socket
                                    remoteServerSocket = new Socket();
                                    //获取sockaddress对象
                                    SocketAddress socketAddress = new InetSocketAddress(portMapperAddress.getTargetIp(), portMapperAddress.getTargetPort());
                                    //设置超时参数
                                    remoteServerSocket.connect(socketAddress, Math.toIntExact(socketConfigProperties.getTimeOut()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    try {
                                        clientSocket.close();
                                    } catch (IOException ioException) {
                                        ioException.printStackTrace();
                                    }
                                    log.info("连接服务:" + portMapperAddress.getServiceName() + "出现超时，断开客户端连接");
                                    return;
                                }
                                log.info("创建目标连接:" + hostAddress + "->" + targetAddress + "成功");
                                //启动线程处理数据
                                pool.execute(new DataForwardingSocket(hostAddress + "->" + targetAddress, clientSocket, remoteServerSocket, socketConfigProperties.getTimeOut(), data, readlen, inputStream));
                                pool.execute(new DataForwardingSocket(targetAddress + "->" + hostAddress, remoteServerSocket, clientSocket, socketConfigProperties.getTimeOut()));
                            }).start();
                        }
                        if (isClose) {
                            log.info("接收客户端:" + dataStr + "内容，Service not matched");
                            clientSocket.close();
                            continue;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        log.info(ex.getMessage(), ex);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.info(e.getMessage(), e);
            }
        } else {
            //多个端口转发
            for (PortMapperAddress portMapperAddress : socketConfigProperties.getPortMapperAddress()) {
                new Thread(() -> {
                    //启动本地监听端口
                    ServerSocket serverSocket = null;
                    try {
                        serverSocket = new ServerSocket(portMapperAddress.getPort());

                        String localAddress = "";

                        localAddress = InetAddress.getLocalHost().getHostAddress() + ":" + portMapperAddress.getPort();
                        log.info("本地socket:" + localAddress + "->" + portMapperAddress.getTargetIp() + ":" + portMapperAddress.getTargetIp() + "启动");

                        Socket clientSocket = null;
                        Socket remoteServerSocket = null;
                        while (true) {
                            try {
                                //获取客户端连接
                                clientSocket = serverSocket.accept();
                                log.info("接收客户端连接成功");
                                final String targetAddress = portMapperAddress.getTargetAddress();
                                if (!portMapperAddress.getEnable()) {
                                    log.info("目标:" + targetAddress + "转发停止中。。。。。");
                                    clientSocket.close();
                                } else {
                                    //建立远程连接
                                    remoteServerSocket = new Socket(portMapperAddress.getTargetIp(), portMapperAddress.getTargetPort());
                                    log.info("创建目标连接:" + targetAddress + "成功");
                                    //启动线程处理数据
                                    (new DataForwardingSocket(localAddress + "->" + targetAddress, clientSocket, remoteServerSocket, socketConfigProperties.getTimeOut())).start();
                                    (new DataForwardingSocket(targetAddress + "->" + localAddress, remoteServerSocket, clientSocket, socketConfigProperties.getTimeOut())).start();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();

            }
        }
    }

    @Override
    public void run(String... args) throws Exception {
        new Thread(() -> {
            start();
        }).start();
    }
}
