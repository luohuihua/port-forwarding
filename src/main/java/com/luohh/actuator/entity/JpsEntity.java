package com.luohh.actuator.entity;

import java.util.List;

/**
 * @author luohuihua
 */
public class JpsEntity {
    //全名
    private String className;
    //小名
    private String smallName;
    //参数
    private List<String> parameters;

    public JpsEntity(String className, String smallName, List<String> parameters) {
        this.className = className;
        this.smallName = smallName;
        this.parameters = parameters;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSmallName() {
        return smallName;
    }

    public void setSmallName(String smallName) {
        this.smallName = smallName;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

}
