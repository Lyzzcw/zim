package lyzzcw.work.zim.registry.nacos.helper;

import java.util.Arrays;
import java.util.Properties;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/8/11 13:36
 * Description: URL 解析
 */
public class NacosUrlHelper {

    public static final String NAMESPACE = "namespace";
    public static final String SERVER_ADDR = "serverAddr";

    public static Properties url2Properties (String url) {
        Properties properties = new Properties();
        String[] parts = url.split("\\?");
        if(parts != null && parts.length > 0){
            properties.setProperty(SERVER_ADDR,parts[0]);
            if(parts.length > 1){
                String[] parameters = parts[1].split("&");
                Arrays.stream(parameters).forEach(item -> {
                    String[] values = item.split("=");
                    if(values[0].equals(NAMESPACE)){
                        properties.setProperty(NAMESPACE,values[1]);
                    }
                });
            }
        }else{
            throw new IllegalArgumentException("error setting properties url: " + url);
        }
        return properties;
    }

    /**
     * 拼接字符串(nacos不支持#)
     * @param serviceName 服务名称
     * @param serviceVersion 服务版本号
     * @param group 服务分组
     * @return 服务名称_服务版本号_服务分组
     */
    public static String buildNacosServiceKey(String serviceName, String serviceVersion, String group) {
//        return String.join("_", serviceName, serviceVersion, group);
        return serviceName;
    }
}
