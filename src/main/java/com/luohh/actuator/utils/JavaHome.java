package com.luohh.actuator.utils;

import java.io.File;


/**
 * @author luohuihua
 */
public class JavaHome {

    public final String path = System.getenv("JAVA_HOME") + File.separator + "bin" + File.separator;

}
