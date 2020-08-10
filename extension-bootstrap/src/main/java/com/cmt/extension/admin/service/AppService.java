package com.cmt.extension.admin.service;

import static java.util.stream.Collectors.toList;

import com.cmt.extension.admin.model.BusinessException;
import com.cmt.extension.admin.model.Converter;
import com.cmt.extension.admin.model.dto.AppDTO;
import com.cmt.extension.admin.model.dto.AppView;
import com.cmt.extension.admin.model.dto.NewSpiDTO;
import com.cmt.extension.admin.model.dto.SpiDTO;
import com.cmt.extension.admin.model.entity.AppEntity;
import com.cmt.extension.admin.repository.AppRepository;
import com.cmt.extension.core.configcenter.model.Application;
import com.cmt.extension.core.configcenter.model.SpiConfigDTO;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 配置服务
 *
 * @author yonghuang
 */
@Service
public class AppService {
    @Autowired
    private AppRepository appRepository;

    /**
     * 获取应用列表
     *
     * @return
     */
    public List<AppEntity> getAllApps() {
        return appRepository.findAll();
    }

    public AppEntity getApplication(String appName){
        if(StringUtils.isEmpty(appName)){
            throw BusinessException.fail("应用名称不可为空");
        }
        return appRepository.findByAppName(appName).orElse(null);
    }

    /**
     * 新增应用
     *
     * @param appName
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long addApp(String appName) {
        if(StringUtils.isEmpty(appName)){
            throw BusinessException.fail("应用名称不可为空");
        }
        appRepository.findByAppName(appName).ifPresent(a->{throw BusinessException.fail("应用已存在");});
        AppEntity app = new AppEntity(appName);
        appRepository.save(app);
        return app.getId();
    }

    /**
     * 新增配置
     *
     * @param configDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void addConfig(SpiConfigDTO configDTO) {
        AppEntity app = appRepository.findByAppName(configDTO.getAppName()).orElseThrow(() -> new BusinessException("应用不存在"));
        app.addExtension(configDTO);
        appRepository.save(app);
    }

    /**
     * 更新配置
     *
     * @param configDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateConfig(SpiConfigDTO configDTO) {
        AppEntity app = appRepository.findByAppName(configDTO.getAppName()).orElseThrow(() -> new BusinessException("应用不存在"));
        app.updateExtension(configDTO);
        appRepository.save(app);
    }

    /**
     * 删除配置
     *
     * @param configDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteConfig(SpiConfigDTO configDTO) {
        AppEntity app = appRepository.findByAppName(configDTO.getAppName()).orElseThrow(() -> new BusinessException("应用不存在"));
        app.deleteExtension(configDTO);
        appRepository.save(app);
    }

    public void addSpi(NewSpiDTO newSpi) {
        AppEntity app = appRepository.findByAppName(newSpi.getAppName()).orElseThrow(() -> new BusinessException("应用不存在"));
        if(app.isSpiExist(newSpi.getSpiInterface())){
            throw BusinessException.fail("扩展点已存在");
        }
        app.addSpi(newSpi.getSpiInterface(), newSpi.getDesc());
        app.setDateModified(new Date());
        appRepository.save(app);
    }

    public Application getApplication(String appName, Integer version) {
        AppEntity app = appRepository.findByAppName(appName).orElseThrow(() -> new BusinessException("应用不存在"));
        if (app.getVersion() > version) {
            return app.build();
        }
        return null;
    }

    public List<SpiDTO> getSpis(String appName) {
        AppEntity app = appRepository.findByAppName(appName).orElseThrow(() -> new BusinessException("应用不存在"));
        return Converter.INSTANCE.map2dto(app.getSpis());
    }

    public List<SpiConfigDTO> getConfigs(String appName, String spiInterface) {
        Assert.notNull(appName,"appName不可为空");
        Assert.notNull(spiInterface,"spiInterface不可为空");
        AppEntity app = appRepository.findByAppName(appName).orElseThrow(() -> new RuntimeException("应用不存在"));
        return app.getSpis()
                .stream()
                .filter(s -> s.getSpiInterface().equals(spiInterface))
                .filter(s -> s.getExtensions() != null)
                .flatMap(s -> Converter.INSTANCE.mapConfigs(s.getExtensions()).stream())
                .collect(toList());
    }

    public SpiConfigDTO getConfig(String appName, String spiInterface, String bizCode) {
        Assert.notNull(bizCode,"bizCode不可为空");
        return getConfigs(appName,spiInterface).stream()
                .filter(c->bizCode.equals(c.getBizCode()))
                .findAny()
                .orElseGet(null);
    }
}
