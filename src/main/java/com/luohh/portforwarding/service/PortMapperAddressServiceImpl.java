package com.luohh.portforwarding.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luohh.portforwarding.model.PortMapperAddress;
import com.luohh.portforwarding.mapper.PortMapperAddressMapper;
import org.springframework.stereotype.Service;

/**
 * 端口映射地址service
 *
 * @author luohuihua
 */
@Service
public class PortMapperAddressServiceImpl extends ServiceImpl<PortMapperAddressMapper, PortMapperAddress> implements IPortMapperAddressService {
}
