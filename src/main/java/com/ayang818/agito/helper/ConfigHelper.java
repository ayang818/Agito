package com.ayang818.agito.helper;

import com.ayang818.agito.enums.ConfigConstant;
import com.ayang818.agito.util.PropsUtil;

import java.util.Properties;

/**
 * @ClassName ConfigHelper
 * @Dessription TODO
 * @Author 杨丰畅
 * @Date 2019/10/22 18:15
 **/
public class ConfigHelper {
    private static final Properties CONFIG_PROPS = PropsUtil.loadConfig(ConfigConstant.CONFIG_FILE);

    public static String getjdbcurl() {
        return (String) CONFIG_PROPS.get(ConfigConstant.JDBC_URL);
    }

    public static String getjdbcdriver() {
        return (String) CONFIG_PROPS.get(ConfigConstant.JDBC_DRIVER);
    }

    public static String getjdbcusername() {
        return (String) CONFIG_PROPS.get(ConfigConstant.JDBC_USERNAME);
    }

    public static String getjdbcpassword() {
        return (String) CONFIG_PROPS.get(ConfigConstant.JDBC_PASSWORD);
    }

    public static String getBasePackageName() {
        return (String) CONFIG_PROPS.get(ConfigConstant.APP_BASE_PACKAGE);
    }

    public static String getClassPath() {
        return (String) CONFIG_PROPS.get(ConfigConstant.CLASS_PATH);
    }

    public static String getAppHTMLPath() {
        return (String) CONFIG_PROPS.get(ConfigConstant.HTML_PATH);
    }

    public static String getAppAssertPath() {
        return (String) CONFIG_PROPS.get(ConfigConstant.ASSERT_PATH);
    }
}
