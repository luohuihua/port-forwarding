package com.luohh.portforwarding.util;

import com.alibaba.fastjson.JSONObject;
import com.luohh.actuator.utils.ArrayUtil;
import com.luohh.actuator.utils.ExecuteCmd;
import com.luohh.portforwarding.util.dto.FreeBean;
import com.luohh.portforwarding.util.dto.KvBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * linux  jvm操作
 */
public class JvmUtils {


    /**
     * 获取哦系统实际占用内存
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static FreeBean free(String url) throws IOException {
        List<KvBean> list = new ArrayList<>();
        Properties props = System.getProperties();
        String home = props.getProperty("java.home").replace("jre", "bin");
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", home+"/free");
        params.put("value", "-m");
        ArrayList<String> executeList = new ArrayList<>();
        for (String s : params.keySet()) {
            executeList.add(params.get(s));
        }
        String[] array =executeList.toArray(new String[executeList.size()]);
        String result = ExecuteCmd.execute(array);
        JSONObject info = JSONObject.parseObject(result);
        String s = info.getString("result");
        BufferedReader reader = new BufferedReader(new StringReader(s));
        String[] keys = ArrayUtil.trim(reader.readLine().split("\\s+|\t"));
        String[] values = ArrayUtil.trim(reader.readLine().split("\\s+|\t"));
        for (int i = 0; i < keys.length; i++) {
            list.add(new KvBean(keys[i], values[i]));
        }
        FreeBean b = new FreeBean();
        b.setTotal(list.get(1).getValue());
        b.setUsed(list.get(2).getValue());
        b.setFree(list.get(3).getValue());
        return b;
    }


    /**
     * 现在时间
     *
     * @return
     */
    public static String time() {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm:ss");
        return format.format(new Date());
    }


}
