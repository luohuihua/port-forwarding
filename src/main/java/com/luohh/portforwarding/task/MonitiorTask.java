package com.luohh.portforwarding.task;

import com.luohh.actuator.entity.*;
import com.luohh.actuator.jvm.Jstack;
import com.luohh.actuator.jvm.Jstat;
import com.luohh.portforwarding.service.ClassService;
import com.luohh.portforwarding.service.GcService;
import com.luohh.portforwarding.service.ThreadService;
import com.luohh.portforwarding.util.ByteConvKbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 定时获取监控信息
 *
 * @author luohuihua
 */
@Configuration
@EnableScheduling
public class MonitiorTask implements CommandLineRunner {
    @Value("${spring.application.name}")
    private String name;
    @Autowired
    private GcService gcService;
    @Autowired
    private ClassService classService;
    @Autowired
    private ThreadService threadService;

    /**
     * jvm gc和内存收信息收集
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void pullGc() {
        String date = time();
        try {
            List<KVEntity> kvEntities = Jstat.jstatGc();
            GcEntity entity = new GcEntity();
            entity.setId(Integer.valueOf(getPid()));
            entity.setName(name);
            entity.setDate(date);
            entity.setS0C(ByteConvKbUtils.getMB(kvEntities.get(0).getValue()));
            entity.setS1C(ByteConvKbUtils.getMB(kvEntities.get(1).getValue()));
            entity.setS0U(ByteConvKbUtils.getMB(kvEntities.get(2).getValue()));
            entity.setS1U(ByteConvKbUtils.getMB(kvEntities.get(3).getValue()));
            entity.setEC(ByteConvKbUtils.getMB(kvEntities.get(4).getValue()));
            entity.setEU(ByteConvKbUtils.getMB(kvEntities.get(5).getValue()));
            entity.setOC(ByteConvKbUtils.getMB(kvEntities.get(6).getValue()));
            entity.setOU(ByteConvKbUtils.getMB(kvEntities.get(7).getValue()));
            entity.setMC(ByteConvKbUtils.getMB(kvEntities.get(8).getValue()));
            entity.setMU(ByteConvKbUtils.getMB(kvEntities.get(9).getValue()));
            entity.setCCSC(ByteConvKbUtils.getMB(kvEntities.get(10).getValue()));
            entity.setCCSU(ByteConvKbUtils.getMB(kvEntities.get(11).getValue()));
            entity.setYGC(kvEntities.get(12).getValue());
            entity.setYGCT(kvEntities.get(13).getValue());
            entity.setFGC(kvEntities.get(14).getValue());
            entity.setFGCT(kvEntities.get(15).getValue());
            entity.setGCT(kvEntities.get(16).getValue());
            gcService.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 线程加载信息收集
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void pullThread() {
        String date = time();
        try {
            JstackEntity info = Jstack.jstack();
            ThreadEntity entity = new ThreadEntity();
            entity.setId(Integer.valueOf(info.getId()));
            entity.setName(name);
            entity.setDate(date);
            entity.setTotal(info.getTotal());
            entity.setRUNNABLE(info.getRUNNABLE());
            entity.setTIMED_WAITING(info.getTIMED_WAITING());
            entity.setWAITING(info.getWAITING());
            threadService.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 类加载信息收集
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void pullClassload() {
        String date = time();
        try {
            List<KVEntity> jstatClass = Jstat.jstatClass();
            ClassLoadEntity entity = new ClassLoadEntity();
            entity.setId(Integer.valueOf(getPid()));
            entity.setName(name);
            entity.setDate(date);
            entity.setLoaded(jstatClass.get(0).getValue());
            entity.setBytes1(ByteConvKbUtils.getMB(jstatClass.get(1).getValue()));
            entity.setUnloaded(jstatClass.get(2).getValue());
            entity.setBytes2(ByteConvKbUtils.getMB(jstatClass.get(3).getValue()));
            entity.setTime1(jstatClass.get(4).getValue());
            entity.setCompiled(jstatClass.get(5).getValue());
            entity.setFailed(jstatClass.get(6).getValue());
            entity.setInvalid(jstatClass.get(7).getValue());
            entity.setTime2(jstatClass.get(8).getValue());
            classService.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 每2小时清空数据
     */
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void clearAll() {
        gcService.clearAll();
        threadService.clearAll();
        classService.clearAll();
    }

    /**
     * 现在时间
     *
     * @return
     */
    private String time() {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * 获取当前应用进程id
     *
     * @return
     */
    private String getPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        return pid;
    }

    @Override
    public void run(String... args) throws Exception {
        pullGc();
        pullClassload();
        pullThread();
    }

}
