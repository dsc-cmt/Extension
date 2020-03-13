package com.cmt.extension.admin.service;

import com.cmt.extension.admin.model.BusinessException;
import com.cmt.extension.admin.model.type.RoleType;
import com.cmt.extension.admin.model.vo.NamespaceCreateVO;
import com.cmt.extension.core.configcenter.model.SpiConfigDTO;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.NamespaceGrayDelReleaseDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenAppNamespaceDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


/**
 * @author tuzhenxian
 * @date 19-10-9
 */
@Service
public class SpiConfigService {
    @Autowired
    private ApolloOpenApiClient client;
    @Value("${spi.registration}")
    private String appId;
    @Value("${env}")
    private String env;
    @Value("${cluster}")
    private String cluster;
    @Value("${administrator}")
    private String administrator;
    @Autowired
    private UserService userService;

    private static final Set<String> DEFAULT_NAMESPACES = Sets.newHashSet("FX.optimus", "FX.dubbo", "application");

    /**
     * 新增属性
     *
     * @param configDTO
     * @return
     */
    public void addConfig(SpiConfigDTO configDTO) {
        //新增属性
        client.createItem(appId, env, cluster, configDTO.getAppId(), buildOpenItemDTO(configDTO));
        //发布
        publish(configDTO.getAppId());
    }

    /**
     * 更新属性
     *
     * @param configDTO
     * @return
     */
    public void updateConfig(SpiConfigDTO configDTO) {
        //更新属性
        client.updateItem(appId, env, cluster, configDTO.getAppId(), buildOpenItemDTO(configDTO));
        //发布
        publish(configDTO.getAppId());
    }

    /**
     * 删除属性
     *
     * @param configDTO
     * @return
     */
    public void deleteConfig(SpiConfigDTO configDTO) {
        client.removeItem(appId, env, cluster, configDTO.getAppId(), configDTO.buildKey(), administrator);
        //发布
        publish(configDTO.getAppId());
    }

    /**
     * 新增namespace
     *
     * @param namespace
     * @return
     */
    public void addNamespace(String namespace) {
        List<String> namespaces = getAllNamespaces();
        if (namespaces.contains(namespace)) {
            throw BusinessException.fail("该应用已存在！");
        }
        client.createAppNamespace(buildNamespaceDTO(namespace));
        //发布
        publish(namespace);
    }

    /**
     * 获取所有namespace
     *
     * @param
     * @return
     */
    public List<String> getAllNamespaces() {
        List<OpenNamespaceDTO> namespaces = client.getNamespaces(appId, env, cluster);
        if (CollectionUtils.isEmpty(namespaces)) {
            return new ArrayList<>();
        }
        return namespaces.stream()
                .map(OpenNamespaceDTO::getNamespaceName)
                .filter(namespace -> !DEFAULT_NAMESPACES.contains(namespace))
                .collect(Collectors.toList());
    }

    /**
     * 获取所有有效namespace 包括visitor及admin
     *
     * @param mobile
     * @return
     */
    public List<String> getValidNamespaces(String mobile) {
        Map<String, List<String>> userApps = userService.getAuthorizedAppsByMobile(mobile);
        return userApps.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    /**
     * 根据手机号获取权限为admin的namespace
     *
     * @param mobile
     * @return
     */
    public List<String> getAuthNamespaces(String mobile) {
        Map<String, List<String>> userApps = userService.getAuthorizedAppsByMobile(mobile);
        if (CollectionUtils.isEmpty(userApps.get(RoleType.ADMIN.getDesc()))) {
            return new ArrayList<>();
        }
        return userApps.get(RoleType.ADMIN.getDesc());
    }

    /**
     * 发布属性
     *
     * @param namespace
     * @return
     */
    private void publish(String namespace) {
        NamespaceGrayDelReleaseDTO namespaceGrayDelReleaseDTO = new NamespaceGrayDelReleaseDTO();
        namespaceGrayDelReleaseDTO.setReleaseTitle(new Date() + " release");
        namespaceGrayDelReleaseDTO.setReleaseComment("");
        namespaceGrayDelReleaseDTO.setReleasedBy(administrator);
        //发布
        client.publishNamespace(appId, env, cluster, namespace, namespaceGrayDelReleaseDTO);
    }

    private OpenItemDTO buildOpenItemDTO(SpiConfigDTO configDTO) {
        OpenItemDTO openItemDTO = new OpenItemDTO();
        openItemDTO.setKey(configDTO.buildKey());
        openItemDTO.setValue(configDTO.buildValue());
        openItemDTO.setComment(configDTO.getComment());
        openItemDTO.setDataChangeCreatedBy(administrator);
        openItemDTO.setDataChangeLastModifiedBy(administrator);
        return openItemDTO;
    }


    private OpenAppNamespaceDTO buildNamespaceDTO(String namespace) {
        OpenAppNamespaceDTO dto = new OpenAppNamespaceDTO();
        dto.setAppId(appId);
        dto.setName(namespace);
        dto.setFormat("properties");
        dto.setPublic(false);
        dto.setDataChangeCreatedBy(administrator);
        return dto;
    }

    /**
     * 获取该namespace下的所有配置信息
     *
     * @param namespace
     * @return
     */
    public List<SpiConfigDTO> getConfigs(String namespace) {
        OpenNamespaceDTO dto = client.getNamespace(appId, env, cluster, namespace);
        if (CollectionUtils.isEmpty(dto.getItems())) {
            return new ArrayList<>();
        }
        return dto.getItems().stream().map(item -> SpiConfigDTO.buildConfigDTO(item.getKey(), item.getValue(), namespace)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 查询namespace信息
     *
     * @return
     */
    public List<NamespaceCreateVO> getNamespacesDetail() {
        List<OpenNamespaceDTO> namespaces = client.getNamespaces(appId, env, cluster);
        if (CollectionUtils.isEmpty(namespaces)) {
            return new ArrayList<>();
        }
        return namespaces.stream().filter(namespace -> !DEFAULT_NAMESPACES.contains(namespace.getNamespaceName()))
                .map(NamespaceCreateVO::buildByOpenNamespaceDTO)
                .collect(Collectors.toList());
    }

    /**
     * 根据手机号获取选项
     *
     * @param mobile
     * @return
     */
    public List<String> getValidOptions(String mobile) {
        if (userService.isSuperAdmin()) {
            return getAllNamespaces();
        }
        return getAuthNamespaces(mobile);
    }
}
