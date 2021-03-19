package com.luohh.actuator.jvm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author luohuihua
 */
public class Javav {

    public static String version() {
        StringBuilder sb = null;
        try {
            Process p = Runtime.getRuntime().exec(new String[]{"java", "-version"});
            InputStreamReader inputStreamReader = new InputStreamReader(p.getErrorStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            bufferedReader.close();
            p.destroy();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


}
