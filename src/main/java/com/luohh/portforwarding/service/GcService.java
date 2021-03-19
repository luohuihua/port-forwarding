package com.luohh.portforwarding.service;


import com.luohh.actuator.entity.GcEntity;
import com.luohh.portforwarding.util.JvmUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author luohuihua
 */
@Service
public class GcService {
    /**
     * gc信息集合
     */
    private List<GcEntity> gcEntities = new ArrayList<>();

    /**
     * 写入gc信息
     *
     * @param entity
     */
    public void save(GcEntity entity) {
        entity.setDate(JvmUtils.time());
        gcEntities.add(entity);
    }

    /**
     * 获取所有gc信息
     *
     * @return
     */
    public List<GcEntity> getAllGc() {
        return gcEntities;
    }

    /**
     * 清空所有gc信息
     */
    public void clearAll() {
        gcEntities.clear();
    }
}
