package com.jph.xibaibai.utils;

import android.text.TextUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jph on 2015/8/25.
 */
public class StringUtil {

    public static <T> String getStringByList(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }

        StringBuilder str = new StringBuilder("");
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                str.append(",");
            }
            str.append(list.get(i));
        }

        return str.toString();
    }

    /**
     * 生成随机code
     * @param count 位数
     * @return
     */
    public static String createRandomCode(int count) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < count; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }

    /**
     * 判断是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        if ("null".equals(str)) {
            return true;
        }
        if (str == null) {
            return true;
        }
        return false;
    }
    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (isNull(str))
            return false;
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }
}
