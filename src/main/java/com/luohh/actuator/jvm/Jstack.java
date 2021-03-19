package com.luohh.actuator.jvm;

import com.luohh.actuator.entity.JstackEntity;
import com.luohh.actuator.utils.ArrayUtil;
import com.luohh.actuator.utils.ExecuteCmd;
import com.luohh.actuator.utils.PathUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * @author luohuihua
 */
public class Jstack {
    private final static String prefix = "java.lang.Thread.State: ";

    /**
     * 该进程的线程信息
     * X轴为时间，Y轴为值的变化
     *
     * @return
     */
    public static JstackEntity jstack() {
        String id = getPid();
        Properties props = System.getProperties();
        String home = props.getProperty("java.home").replace("jre", "bin");
        String s = ExecuteCmd.execute(new String[]{home + "/jstack", id});
        int total = ArrayUtil.appearNumber(s, "nid=");
        int RUNNABLE = ArrayUtil.appearNumber(s, prefix + "RUNNABLE");
        int TIMED_WAITING = ArrayUtil.appearNumber(s, prefix + "TIMED_WAITING");
        int WAITING = ArrayUtil.appearNumber(s, prefix + "WAITING");
        return new JstackEntity(id, total, RUNNABLE, TIMED_WAITING, WAITING);
    }

    /**
     * 导出线程快照
     *
     * @return
     */
    public static String dump() throws IOException {
        String id = getPid();
        Properties props = System.getProperties();
        String home = props.getProperty("java.home").replace("jre", "bin");
        String path = PathUtil.getRootPath("dump/" + id + "_thread.txt");
        String s = ExecuteCmd.execute(new String[]{home + "/jstack", id});
        File file = new File(path);
        FileUtils.write(file, s, Charset.forName("UTF-8"));
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
