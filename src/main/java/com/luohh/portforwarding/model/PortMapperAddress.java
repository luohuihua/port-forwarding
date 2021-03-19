package com.luohh.portforwarding.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * @author luohuihua
 */
@Data
@TableName(value = "s_port_mapper_address")
public class PortMapperAddress {
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 端口
     */
    private Integer port;

    /**
     * 目标IP
     */
    private String targetIp;

    /**
     * 目标端口
     */
    private Integer targetPort;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 是否启动
     */
    private Boolean enable;

    /**
     * 源IP端口
     */
    @TableField(exist = false)
    private String resourceIpPort;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PortMapperAddress that = (PortMapperAddress) o;
        return Objects.equals(targetIp, that.targetIp) &&
                Objects.equals(targetPort, that.targetPort) &&
                Objects.equals(serviceName, that.serviceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetIp, targetPort, serviceName);
    }

    /**
     * 目标地址
     *
     * @return
     */
    public String getTargetAddress() {
        return getTargetIp() + ":" + getTargetPort() + "/" + getServiceName();
    }
}
