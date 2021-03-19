package com.luohh.portforwarding.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luohh.portforwarding.mapper.IpWhiteListMapper;
import com.luohh.portforwarding.model.IpWhiteList;
import org.springframework.stereotype.Service;

/**
 * ip白名单service
 *
 * @author luohuihua
 */
@Service
public class IpWhiteListService extends ServiceImpl<IpWhiteListMapper, IpWhiteList> implements IIpWhiteListService {
}
