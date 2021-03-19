package com.luohh.portforwarding.util.dto;

import lombok.Data;

/**
 * @author luohuihua
 */
@Data
public class KvBean {
    private String key;
    private String value;

    public KvBean(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
