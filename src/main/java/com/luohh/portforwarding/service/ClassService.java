package com.luohh.portforwarding.service;

import com.luohh.actuator.entity.ClassLoadEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;


/**
 * @author luohuihua
 */
@Service
public class ClassService {
    private List<ClassLoadEntity> classLoadEntities = new ArrayList<>();

    /**
     * 获取所有类加载信息
     *
     * @return
     */
    public List<ClassLoadEntity> getAllClassLoad() {
        return classLoadEntities;
    }

    /**
     * 写入类加载信息
     *
     * @param entity
     */
    public void save(ClassLoadEntity entity) {
        classLoadEntities.add(entity);
    }

    /**
     * 清空所有类加载信息
     */
    public void clearAll() {
        classLoadEntities.clear();
    }
}
