package lyzzcw.work.zim.registry.api.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author lzy
 * @version 1.0.0
 * @description 服务元数据，注册到注册中心的元数据信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceMeta implements Serializable {
    private String id;
    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务版本号
     */
    private String serviceVersion;

    /**
     * 服务地址
     */
    private String serviceAddr;

    /**
     * 服务端口
     */
    private int servicePort;

    /**
     * 服务分组
     */
    private String serviceGroup;

    /**
     * 服务权重
     */
    private int weight;
}
