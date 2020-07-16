package com.cmt.extension.admin.service;

import com.cmt.extension.admin.model.Converter;
import com.cmt.extension.admin.model.dto.AppDTO;
import com.cmt.extension.admin.model.dto.AppView;
import com.cmt.extension.admin.model.entity.AppEntity;
import com.cmt.extension.admin.model.entity.SpiEntity;
import com.cmt.extension.admin.repository.AppRepository;
import com.cmt.extension.admin.repository.SpiRepository;
import com.cmt.extension.core.configcenter.model.SpiConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 配置服务
 *
 * @author yonghuang
 */
@Service
public class ConfigService {
    @Autowired
    private AppRepository appRepository;

    @Autowired
    private SpiRepository spiRepository;

    /**
     * 获取应用列表
     * @return
     */
    public List<AppDTO> getAllApps() {
        List<AppView> list = appRepository.findAllApps();
        return Converter.INSTANCE.map(list);
    }

    /**
     * 新增应用
     * @param appName
     * @param creatorId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long addApp(String appName, Long creatorId) {
        AppEntity appEntity = new AppEntity(appName, creatorId);
        appRepository.save(appEntity);

        return appEntity.getId();
    }

    /**
     * 获取该app下的所有配置信息
     *
     * @param appName
     * @return
     */
    public List<SpiConfigDTO> getConfigs(String appName) {
        AppEntity appEntity = appRepository.findByAppName(appName).orElseThrow(() -> new RuntimeException("没用找到应用"));
        List<SpiEntity> spiList = spiRepository.findAllByAppId(appEntity.getId()).orElse(new ArrayList<>());

        return spiList.stream().map(spi -> {
            SpiConfigDTO dto = Converter.INSTANCE.map(spi);
            dto.setAppName(appEntity.getAppName());

            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 新增配置
     *
     * @param configDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long addConfig(SpiConfigDTO configDTO) {
        SpiEntity spiEntity = spiRepository.findByAppIdAndSpiInterface(configDTO.getAppId(), configDTO.getSpiInterface())
                .orElse(SpiEntity.create(configDTO));

        spiRepository.save(spiEntity);
        return spiEntity.getId();
    }

    /**
     * 更新配置
     *
     * @param configDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateConfig(SpiConfigDTO configDTO) {
        SpiEntity spiEntity = spiRepository.findByAppIdAndSpiInterface(configDTO.getAppId(), configDTO.getSpiInterface())
                .orElseThrow(() -> new RuntimeException("没用找到应用"));

        spiEntity.updateExtension(configDTO);
        spiRepository.save(spiEntity);
    }

    /**
     * 删除配置
     *
     * @param configDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteConfig(SpiConfigDTO configDTO) {
        SpiEntity spiEntity = spiRepository.findByAppIdAndSpiInterface(configDTO.getAppId(), configDTO.getSpiInterface())
                .orElseThrow(() -> new RuntimeException("没用找到应用"));

        spiEntity.deleteExtension(configDTO.getExtensionId());
        spiRepository.save(spiEntity);
    }
}
