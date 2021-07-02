package com.luohh.portforwarding.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.luohh.portforwarding.model.IpWhiteList;
import com.luohh.portforwarding.model.PortMapperAddress;
import com.luohh.portforwarding.config.SocketConfigProperties;
import com.luohh.portforwarding.service.IIpWhiteListService;
import com.luohh.portforwarding.service.IPortMapperAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * socket使用的controller
 *
 * @author luohuihua
 */
@RestController
@RequestMapping("/socket")
@Slf4j
public class SocketController {
//    /**
//     * 登录用户名
//     */
//    @Value("${login.user-name}")
//    private String loginUserName;

//    /**
//     * 登录密码
//     */
//    @Value("${login.password}")
//    private String loginPassword;

    @Autowired
    private SocketConfigProperties socketConfigProperties;


    @Autowired
    private IPortMapperAddressService portMapperAddressService;

    @Autowired
    private IIpWhiteListService ipWhiteListService;

    /**
     * 获取IP白名单
     *
     * @return
     */
    @GetMapping(value = "/get-ip-white-list")
    public JSONObject getIpWhiteList() {
        loadData();
        JSONObject jsonpObject = new JSONObject();
        jsonpObject.put("code", 0);
        jsonpObject.put("msg", "");
        jsonpObject.put("count", socketConfigProperties.getIpWhiteLists().size());
        List<IpWhiteList> ipWhiteLists = socketConfigProperties.getIpWhiteLists();
        jsonpObject.put("data", ipWhiteLists);
        return jsonpObject;
    }

    /**
     * 新增IP地址
     *
     * @param ipWhiteList
     */
    @PostMapping(value = "/add-ip")
    public String addIp(IpWhiteList ipWhiteList) {
        if (ipWhiteList == null) {
            return "新增失败，没有传入有效数据";
        }
        if (ipWhiteList.getIp() == null || ipWhiteList.getIp().length() <= 0) {
            return "新增失败，IP地址不能为空";
        }
        QueryWrapper<IpWhiteList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ip", ipWhiteList.getIp());
        int ct = ipWhiteListService.count(queryWrapper);
        if (ct > 0) {
            return "新增失败，存在相同IP地址";
        }
        ipWhiteListService.save(ipWhiteList);
        loadData();
        return "新增成功";
    }

    /**
     * 删除IP地址
     *
     * @param ipWhiteLists
     */
    @PostMapping(value = "/delete-ip")
    public String deleteIp(@RequestBody List<IpWhiteList> ipWhiteLists) {
        if (ipWhiteLists == null || ipWhiteLists.size() <= 0) {
            return "删除失败，没有传入有效数据";
        }
        List<Integer> ids = new ArrayList<>();
        for (IpWhiteList ipWhiteList : ipWhiteLists) {
            ids.add(ipWhiteList.getId());
        }
        ipWhiteListService.removeByIds(ids);
        loadData();
        return "删除成功";
    }

    /**
     * 修改
     *
     * @param ipWhiteList
     */
    @PostMapping(value = "/update-ip")
    public String updateIp(IpWhiteList ipWhiteList) {
        if (ipWhiteList == null) {
            return "修改失败，没有传入有效数据";
        }
        if (ipWhiteList.getIp() == null || ipWhiteList.getIp().length() <= 0) {
            return "修改失败，IP地址不能为空";
        }
        QueryWrapper<IpWhiteList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ip", ipWhiteList.getIp());
        queryWrapper.notIn("id", ipWhiteList.getId());
        int ct = ipWhiteListService.count(queryWrapper);
        if (ct > 0) {
            return "修改失败，存在相同IP地址";
        }
        ipWhiteListService.updateById(ipWhiteList);
        loadData();
        return "修改成功";
    }

    /**
     * 获取配置
     *
     * @return
     */
    @GetMapping(value = "/get-config")
    public JSONObject getSocketConfig(HttpServletRequest httpServletRequest) {
        loadData();
        JSONObject jsonpObject = new JSONObject();
        jsonpObject.put("code", 0);
        jsonpObject.put("msg", "");
        jsonpObject.put("count", socketConfigProperties.getPortMapperAddress().size());
        List<PortMapperAddress> portMapperAddress = socketConfigProperties.getPortMapperAddress();
        if (portMapperAddress != null) {
            String ip = httpServletRequest.getServerName();
            for (PortMapperAddress portMapperAddres : portMapperAddress) {
                portMapperAddres.setResourceIpPort(ip + ":" + socketConfigProperties.getPort());
            }
        }
        jsonpObject.put("data", portMapperAddress);
        return jsonpObject;
    }

    /**
     * 启动转发
     *
     * @return
     */
    @PostMapping(value = "/start-forward")
    public boolean startForward(PortMapperAddress portMapperAddress) {
        if (portMapperAddress == null) {
            return false;
        }
        setForward(portMapperAddress, true);
        return true;
    }

    /**
     * 停止转发
     *
     * @return
     */
    @PostMapping(value = "/stop-forward")
    public boolean stopForward(PortMapperAddress portMapperAddress) {
        if (portMapperAddress == null) {
            return false;
        }
        setForward(portMapperAddress, false);
        return true;
    }

    /**
     * 设置是否转发
     *
     * @param portMapperAddress
     * @param enable
     */
    private void setForward(PortMapperAddress portMapperAddress, boolean enable) {
        for (PortMapperAddress mapperAddress : socketConfigProperties.getPortMapperAddress()) {
            if (portMapperAddress.equals(mapperAddress)) {
                mapperAddress.setEnable(enable);
                portMapperAddressService.updateById(mapperAddress);
                loadData();
            }
        }
    }

    /**
     * 加载数据
     */
    private void loadData() {
        //加载映射
        List<PortMapperAddress> portMapperAddressList = portMapperAddressService.list();
        socketConfigProperties.setPortMapperAddress(portMapperAddressList);
        //加载白名单
        List<IpWhiteList> ipWhiteLists = ipWhiteListService.list();
        socketConfigProperties.setIpWhiteLists(ipWhiteLists);
    }

    /**
     * 登录
     *
     * @param userName
     * @param password
     */
    @PostMapping(value = "/login")
    private String login(String userName, String password) {
//        if (loginUserName.equals(userName) && loginPassword.equals(password)) {
//            return loginUserName;
//        }
        return null;
    }

    /**
     * 新增
     *
     * @param portMapperAddress
     */
    @PostMapping(value = "/add")
    public String add(PortMapperAddress portMapperAddress) {
        if (portMapperAddress == null) {
            return "新增失败，没有传入有效数据";
        }
        if (portMapperAddress.getTargetIp() == null || portMapperAddress.getTargetIp().length() <= 0) {
            return "新增失败，目标IP不能为空";
        }
        if (portMapperAddress.getTargetPort() == null) {
            return "新增失败，目标端口不能为空";
        }
        if (portMapperAddress.getServiceName() == null || portMapperAddress.getServiceName().length() <= 0) {
            return "新增失败，服务名不能为空";
        }
        QueryWrapper<PortMapperAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("service_Name", portMapperAddress.getServiceName());
        int ct = portMapperAddressService.count(queryWrapper);
        if (ct > 0) {
            return "新增失败，存在相同服务名称";
        }
        portMapperAddress.setEnable(true);
        portMapperAddressService.save(portMapperAddress);
        loadData();
        return "新增成功";
    }

    /**
     * 删除
     *
     * @param portMapperAddressList
     */
    @PostMapping(value = "/delete")
    public String delete(@RequestBody List<PortMapperAddress> portMapperAddressList) {
        if (portMapperAddressList == null || portMapperAddressList.size() <= 0) {
            return "删除失败，没有传入有效数据";
        }
        List<Integer> ids = new ArrayList<>();
        for (PortMapperAddress portMapperAddress : portMapperAddressList) {
            ids.add(portMapperAddress.getId());
        }
        portMapperAddressService.removeByIds(ids);
        loadData();
        return "删除成功";
    }

    /**
     * 修改
     *
     * @param portMapperAddress
     */
    @PostMapping(value = "/update")
    public String update(PortMapperAddress portMapperAddress) {
        if (portMapperAddress == null) {
            return "修改失败，没有传入有效数据";
        }
        if (portMapperAddress.getTargetIp() == null || portMapperAddress.getTargetIp().length() <= 0) {
            return "修改失败，目标IP不能为空";
        }
        if (portMapperAddress.getTargetPort() == null) {
            return "修改失败，目标端口不能为空";
        }
        if (portMapperAddress.getServiceName() == null || portMapperAddress.getServiceName().length() <= 0) {
            return "修改失败，服务名不能为空";
        }
        QueryWrapper<PortMapperAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("service_Name", portMapperAddress.getServiceName());
        queryWrapper.notIn("id", portMapperAddress.getId());
        int ct = portMapperAddressService.count(queryWrapper);
        if (ct > 0) {
            return "修改失败，存在相同服务名称";
        }
        portMapperAddress.setEnable(true);
        portMapperAddressService.updateById(portMapperAddress);
        loadData();
        return "修改成功";
    }
}
