package com.fantaike.tools.utils;

import cn.hutool.log.Log;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;

/**
 * @author ：YanZiheng：
 * @Description ：Apollo配置中心
 * @date ：2018-9-13
 */
public class ApolloUtil {

    private static final Log logger = Log.get();

    //取不到配置的默认值
    private static final String DEFAULT_VALUE = "";
    //部门
    private static final String DEPARTMENT = "TEST1.";
    //接口的key
    private static final String HOST_CONFIG = "host.port";

    private static Config config;

    /**
     * @author ：YanZiheng：
     * @Description ：获取默认namespace的配置
     * @date ：2018-9-13
     */
    public static String getConfig(String key) {
        config = ConfigService.getAppConfig();
        String result = config.getProperty(key, DEFAULT_VALUE);
        logger.info("[getConfig]key={},value={}", key, result);
        return result;
    }


    /**
     * @author ：YanZiheng：
     * @Description ：获取其他namespace的配置
     * @date ：2018-9-13
     */
    public static String getConfig(String namespace, String key) {
        config = ConfigService.getConfig(namespace);
        String result = config.getProperty(key, DEFAULT_VALUE);
        logger.info("[getConfig]namespace={},key={},value=" + result, namespace, key);
        return result;
    }

    /**
     * @author ：YanZiheng：
     * @Description ：获取为本系统提供服务的接口地址
     * @date ：2018-10-24
     */
    public static String getApiConfig(String project, String apiName) {
        config = ConfigService.getConfig(DEPARTMENT + project);
        String interfaceHost = config.getProperty(HOST_CONFIG, DEFAULT_VALUE);
        String interfacePath = config.getProperty(apiName, DEFAULT_VALUE);
        String result = interfaceHost + interfacePath;
        logger.info("[getApiConfig]project={},apiName={},value=" + result, project, apiName);
        return result;
    }

}