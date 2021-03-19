package com.luohh.actuator.jvm;


import com.luohh.actuator.entity.KVEntity;
import com.luohh.actuator.utils.ArrayUtil;
import com.luohh.actuator.utils.ExecuteCmd;

import java.io.BufferedReader;
import java.io.StringReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author luohuihua
 */
public class Jstat {

    /**
     * Jstat 模板方法
     *
     * @param strings 命令
     * @return 集合
     */
    private static List<KVEntity> jstat(String[] strings) throws Exception {
        List<KVEntity> list = new ArrayList<>();
        String s = ExecuteCmd.execute(strings);
        assert s != null;
        BufferedReader reader = new BufferedReader(new StringReader(s));
        String[] keys = ArrayUtil.trim(reader.readLine().split("\\s+|\t"));
        String[] values = ArrayUtil.trim(reader.readLine().split("\\s+|\t"));
        // 特殊情况
        if (strings[1].equals("-compiler")) {
            for (int i = 0; i < 4; i++) {
                list.add(new KVEntity(keys[i], values[i]));
            }
            return list;
        }
        // 正常流程
        for (int i = 0; i < keys.length; i++) {
            list.add(new KVEntity(keys[i], values[i]));
        }
        return list;
    }

    /**
     * 类加载信息
     * X轴为时间，Y轴为值的变化
     *
     * @return
     */
    public static List<KVEntity> jstatClass() throws Exception {
        String id = getPid();
        Properties props = System.getProperties();
        String home = props.getProperty("java.home").replace("jre", "bin");
        List<KVEntity> jstatClass = jstat(new String[]{home + "/jstat", "-class", id});
        List<KVEntity> jstatCompiler = jstat(new String[]{home + "/jstat", "-compiler", id});
        jstatClass.addAll(jstatCompiler);
        return jstatClass;
    }

    /**
     * 堆内存信息
     * X轴为时间，Y轴为值的变化
     *
     * @return
     */
    public static List<KVEntity> jstatGc() throws Exception {
        String id = getPid();
        Properties props = System.getProperties();
        String home = props.getProperty("java.home").replace("jre", "bin");
        return jstat(new String[]{home + "/jstat", "-gc", id});
    }

    /**
     * 堆内存百分比
     * 实时监控
     *
     * @return
     */
    public static List<KVEntity> jstatUtil() throws Exception {
        String id = getPid();
        Properties props = System.getProperties();
        String home = props.getProperty("java.home").replace("jre", "bin");
        return jstat(new String[]{home + "/jstat", "-gcutil", id});
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
