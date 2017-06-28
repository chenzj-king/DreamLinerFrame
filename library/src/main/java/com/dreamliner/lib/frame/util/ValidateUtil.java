package com.dreamliner.lib.frame.util;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author chenzj
 * @Title: ValidateUtil
 * @Description: 类的描述 - 校验工具类
 * @date 2017/6/27 15:42
 * @email admin@chenzhongjin.cn
 */
public class ValidateUtil {

    public final static int PHONE_NUM = 0;
    public final static int CHINESE = 1;
    public final static int EMAIL = 2;
    public final static int PASS_WORD = 3;

    private static Pattern mEmailPattern;
    private static Pattern mPhonePattern;
    private static Pattern mPasswordPattern;
    private static Pattern mChinesePattern;

    private static boolean isEmail(String email) {
        if (null == mEmailPattern)
            initEmailPattern();
        return mEmailPattern.matcher(email).matches();
    }

    private static void initEmailPattern() {
        mEmailPattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    }

    private static boolean isPhoneNumber(String phoneNumber) {
        if (null == mPhonePattern)
            initPhonePatter();
        return mPhonePattern.matcher(phoneNumber).matches();
    }

    private static void initPhonePatter() {
        mPhonePattern = Pattern.compile("^1\\d{10}$");
        /*
        yidongPattern = Pattern.compile("^1((34|35|36|37|38|39|47|50|51|52|57|58|59|78|82|83|84|87|88)[0-9]|705)\\d{3,7}$");
        liantongPattern = Pattern.compile("^1((30|31|32|45|55|56|76|85|86)[0-9]|709)\\d{3,7}$");
        dianxinPattern = Pattern.compile("^1((33|53|80|81|89|77|73)[0-9]|700)\\d{3,7}$");
        */
    }

    private static boolean isPassword(String password) {
        if (mPasswordPattern == null)
            initPasswordPatter();
        return mPasswordPattern.matcher(password).matches();
    }

    private static void initPasswordPatter() {
        mPasswordPattern = Pattern.compile("^[\\@A-Za-z0-9\\!\\#\\$\\%\\^\\&\\*\\.\\~]{6,12}$");
    }

    private static boolean isChinese(String str) {
        if (null == mChinesePattern)
            initChinese();
        return mChinesePattern.matcher(str).matches();
    }

    private static void initChinese() {
        mChinesePattern = Pattern.compile("[\u4e00-\u9fa5]+");
    }

    public static boolean isValidate(Object obj) {
        return obj != null;
    }

    public static boolean isValidate(Object[] obj) {
        return obj != null && obj.length > 0;
    }

    public static boolean isValidate(Collection<?> collection) {
        return collection != null && collection.size() > 0;
    }

    public static boolean isValidate(Map<?, ?> map) {
        return map != null && map.size() > 0;
    }

    public static boolean isValidate(int type, String str) {
        switch (type) {
            case PHONE_NUM:
                return isPhoneNumber(str);
            case CHINESE:
                return isChinese(str);
            case EMAIL:
                return isEmail(str);
            case PASS_WORD:
                return isPassword(str);
        }
        return false;
    }

}
