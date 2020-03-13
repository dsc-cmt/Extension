package com.cmt.extension.core.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import com.cmt.extension.core.common.SpiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author tuzhenxian
 * @date 19-11-25
 */
@Slf4j
public class EnvUtils {
    private static final String SERVER_PROPERTIES_LINUX = "/opt/settings/server.properties";
    private static final String SERVER_PROPERTIES_WINDOWS = "C:/opt/settings/server.properties";

    private static String env;
    private static Properties serverProperties;

    //读取server.properties文件
    static {
        serverProperties=new Properties();
        InputStream in= null;
        try {
            in = new FileInputStream(getPathByOS());
            serverProperties.load(in);
        } catch (Exception e) {
            //不存在文件 属性为空 不抛出异常
        }
    }

    public static String getEnv() {
        // 1. Try to get environment from JVM system property
        env = System.getProperty("env");
        if (StringUtils.isNotBlank(env)) {
            env = env.trim();
            log.info("[spi]Environment is set to [{}] by JVM system property 'env'.", env);
            return env;
        }

        // 2. Try to get environment from OS environment variable
        env = System.getenv("ENV");
        if (StringUtils.isNotBlank(env)) {
            env = env.trim();
            log.info("[spi]Environment is set to [{}] by OS env variable 'ENV'.", env);
            return env;
        }

        // 3. Try to get environment from file "server.properties"
        env = serverProperties.getProperty("env");
        if (StringUtils.isNotBlank(env)) {
            env = env.trim();
            log.info("[spi]Environment is set to [{}] by property 'env' in server.properties.", env);
            return env;
        }

        // 4. environment is null throws Exception.
        throw SpiException.fail("[spi]未设置env变量,配置方式(1) JVM system property 'env', (2) OS env variable 'ENV' nor (3) property 'env' from the properties InputStream.");
    }


    private static String getPathByOS() {
        if(isWindows()){
            return SERVER_PROPERTIES_WINDOWS;
        }
        return SERVER_PROPERTIES_LINUX;
    }

    private static boolean isWindows() {
        String osName = System.getProperty("os.name");
        if (StringUtils.isBlank(osName)) {
            return false;
        }
        return osName.startsWith("Windows");
    }
}
