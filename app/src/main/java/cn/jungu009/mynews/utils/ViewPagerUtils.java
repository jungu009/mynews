package cn.jungu009.mynews.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jungu009 on 2016/10/9.
 *
 */

public final class ViewPagerUtils {

    private static final String ADDR = "http://v.juhe.cn/toutiao/index?type=";
    private static final String KEY = "&key=bb12536c113e279e1f70735e54019196";
    public static final String TOPURL = ADDR + KEY;
    public static final String SHEHUIURL = ADDR + "shehui" + KEY;
    public static final String GUONEIURL = ADDR + "guonei" + KEY;
    public static final String GUOJIURL = ADDR + "guoji" + KEY;
    public static final String YULEURL = ADDR + "yule" + KEY;
    public static final String TIYUURL = ADDR + "tiyu" + KEY;
    public static final String JUNSHIURL = ADDR + "junshi" + KEY;
    public static final String KEJIURL = ADDR + "keji" + KEY;
    public static final String CAIJINGURL = ADDR + "caijing" + KEY;
    public static final String SHISHANGURL = ADDR + "shishang" + KEY;
    public static final String FAVOURIT = "favourit";

    private ViewPagerUtils() { }

    public static String whichUrl(String text) {
        if("头条".equals(text)) {
           return TOPURL;
        } else if("社会".equals(text)) {
            return SHEHUIURL;
        } else if("国际".equals(text)) {
            return GUOJIURL;
        } else if("国内".equals(text)) {
            return GUONEIURL;
        } else if("娱乐".equals(text)) {
            return YULEURL;
        } else if("体育".equals(text)) {
            return TIYUURL;
        } else if("军事".equals(text)) {
            return JUNSHIURL;
        } else if("科技".equals(text)) {
            return KEJIURL;
        } else if("财经".equals(text)) {
            return CAIJINGURL;
        } else if("时尚".equals(text)) {
            return SHISHANGURL;
        }
        return null;
    }

}
