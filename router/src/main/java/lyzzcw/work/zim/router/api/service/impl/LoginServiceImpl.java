package lyzzcw.work.zim.router.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.zim.router.api.service.LoginService;
import lyzzcw.work.zim.router.loadbalance.NacosServiceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/2 9:14
 * Description: No Description
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Resource
    private NacosServiceService nacosServiceService;

    @Override
    public String discovery() {
        try {
            String url =  nacosServiceService.discovery();
            log.info("Discovery server url --- {}", url);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
