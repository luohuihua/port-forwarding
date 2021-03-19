package com.luohh.portforwarding.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * @author luohuihua
 */
@Data
@TableName(value = "s_ip_white_list")
public class IpWhiteList {
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 备注
     */
    private String notes;
}
