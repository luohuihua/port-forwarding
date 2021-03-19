package com.luohh.portforwarding.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luohh.portforwarding.model.IpWhiteList;
import org.apache.ibatis.annotations.Mapper;

/**
 * ip白名单映射
 *
 * @author luohuihua
 */
@Mapper
public interface IpWhiteListMapper extends BaseMapper<IpWhiteList> {
}
