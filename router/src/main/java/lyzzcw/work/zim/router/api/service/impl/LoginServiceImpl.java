package lyzzcw.work.zim.router.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.zim.router.api.domain.dto.UserDTO;
import lyzzcw.work.zim.router.api.service.LoginService;
import lyzzcw.work.zim.router.infrastructure.entity.ImUser;
import lyzzcw.work.zim.router.infrastructure.mapper.ImUserMapper;
import lyzzcw.work.zim.router.loadbalance.NacosServiceService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.time.LocalDateTime;

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
    @Resource
    private ImUserMapper imUserMapper;

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

    @Override
    public ImUser register(UserDTO userDTO) {
        ImUser exist = imUserMapper.selectByUserName(userDTO.getUserName());
        Assert.isNull(exist, "用户已存在");
        ImUser imUser = new ImUser();
        imUser.setNickName(userDTO.getNickName());
        imUser.setUserName(userDTO.getUserName());
        imUser.setPassword(userDTO.getPassword());
        imUser.setSex(userDTO.getSex());
        imUser.setType((short)1);
        imUser.setCreatedTime(LocalDateTime.now());
        imUserMapper.insert(imUser);
        return imUser;
    }
}
