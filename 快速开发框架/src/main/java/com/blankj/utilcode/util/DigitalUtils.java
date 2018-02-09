package com.blankj.utilcode.util;

import java.util.regex.Pattern;

/**
 * Created by Joe on 2016/12/16.
 * Email lovejjfg@gmail.com
 */

public class DigitalUtils {

    static Pattern p = Pattern.compile("[0-9]*");

    public static boolean isDigital(String str) {
        return p.matcher(str).matches();
    }
}
