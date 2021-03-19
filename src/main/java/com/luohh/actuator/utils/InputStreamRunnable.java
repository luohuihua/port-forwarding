package com.luohh.actuator.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author luohuihua
 */
class InputStreamRunnable implements Runnable {
    private BufferedReader bReader = null;

    InputStreamRunnable(InputStream is, String type) {
        try {
            bReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(is), "UTF-8"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        String line;
        int num = 1;
        try {
            while ((line = bReader.readLine()) != null) {
                System.out.println("---->" + String.format("%02d", num++) + " " + line);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (null != bReader) {
                    bReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
