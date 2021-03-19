package com.luohh.portforwarding.service;

import com.luohh.actuator.entity.ThreadEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author luohuihua
 */
@Service
public class ThreadService {
    private List<ThreadEntity> threadEntities = new ArrayList<>();

    /**
     * 获取所有线程数据
     *
     * @return
     */
    public List<ThreadEntity> getAllThread() {
        return threadEntities;
    }

    /**
     * 写入线程相关信息
     *
     * @param entity
     */
    public void save(ThreadEntity entity) {
        threadEntities.add(entity);
    }


    /**
     * 清空所有线程信息
     */
    public void clearAll() {
        threadEntities.clear();
    }
}
