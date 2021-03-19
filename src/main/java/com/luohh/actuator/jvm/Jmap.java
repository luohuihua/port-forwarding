package com.luohh.actuator.jvm;


import com.luohh.actuator.utils.ExecuteCmd;
import com.luohh.actuator.utils.PathUtil;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Properties;

/**
 * @author luohuihua
 */
public class Jmap {

    /**
     * 导出堆快照
     *
     * @return
     */
    public static String dump() {
        String id = getPid();
        String path = PathUtil.getRootPath("dump/" + id + "_heap.hprof");
        File file = new File(PathUtil.getRootPath("dump/"));
        if (!file.exists()) {
            file.mkdirs();
        }
        Properties props = System.getProperties();
        String home=props.getProperty("java.home").replace("jre", "bin");
        ExecuteCmd.execute(new String[]{home + "/jmap", "-dump:format=b,file=" + path, id});
        return path;
    }

    /**
     * 获取当前应用进程id
     *
     * @return
     */
    public static String getPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        return pid;
    }
}
