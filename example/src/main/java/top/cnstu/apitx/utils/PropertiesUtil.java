package top.cnstu.apitx.utils;

import java.io.IOException;
import java.util.Properties;

/**
 *
 * Created by yore on 2018-03-21 16:26
 */
public class PropertiesUtil {

    // Topology Config
    public static Properties myProp;
    static {
        myProp = new Properties();
        try {
            myProp.load(PropertiesUtil.class.getResourceAsStream("/my.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回my.properties的配置对象
     * @author yore
     * @return Properties my.properties的配置对象
     * @Date 2017/12/26 14:22
     */
    public static Properties getMyProperties(){
        return myProp;
    }


    /**
     * 返回my.properties的配置对象key对应的字符串值
     * @author yore
     * @param key key值
     * @return String key对应的字符串值
     * @Date 2017/12/26 14:22
     */
    public static String getPropValue(String key){
        return getMyProperties().getProperty(key);
    }


    /**
     * 返回my.properties的配置对象key对应的整形值
     * @author yore
     * @param key key值
     * @return String key对应的整形值
     * @Date 2017/12/26 14:22
     */
    public static Integer getPropInteger(String key){
        return Integer.parseInt(getPropValue(key));
    }

    /**
     * 返回my.properties的配置对象key对应的布尔值
     * @author yore
     * @param key key值
     * @return String key对应的布尔值
     * @Date 2017/12/26 14:22
     */
    public static Boolean getPropBoolean(String key){
        return Boolean.parseBoolean(getPropValue(key));
    }


}
