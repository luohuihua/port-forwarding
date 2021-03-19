package com.luohh.portforwarding.util.dto;

import lombok.Data;

/**
 * linux服务器 实际占用内存
 */
@Data
public class FreeBean {

    /**
     * 总内存
     */
    public String total;
    /**
     * 已使用内存
     */
    public String used;
    /**
     * 空闲的内存数:
     */
    public String free;

    @Override
    public String toString() {
        return "FreeBean [total=" + total + ", used=" + used + ", free=" + free
                + "]";
    }
}
