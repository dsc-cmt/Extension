package com.cmt.extension.admin.service;

import static java.util.stream.Collectors.toList;

import com.cmt.extension.admin.model.vo.NamespaceCreateVO;
import com.cmt.extension.core.configcenter.CuratorZookeeperClient;
import com.cmt.extension.core.configcenter.model.SpiConfigDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author tuzhenxian
 * @date 19-10-9
 */
@Service
public class SpiConfigService {
    private static final String ROOT = "/spi-admin";
    @Autowired
    private UserService userService;
    private CuratorZookeeperClient client = CuratorZookeeperClient.instance();

    /**
     * 新增属性
     *
     * @param configDTO
     * @return
     */
    public void addConfig(SpiConfigDTO configDTO) {
        client.createPersistent(getPath(configDTO.getAppId(), configDTO.buildKey()), configDTO.buildValue());
    }

    private String getPath(String appId, String key) {
        return ROOT + "/" + appId + "/" + key;
    }

    /**
     * 更新属性
     *
     * @param configDTO
     * @return
     */
    public void updateConfig(SpiConfigDTO configDTO) {
        client.createPersistent(getPath(configDTO.getAppId(), configDTO.buildKey()), configDTO.buildValue());
    }

    /**
     * 删除属性
     *
     * @param configDTO
     * @return
     */
    public void deleteConfig(SpiConfigDTO configDTO) {
        client.deletePath(getPath(configDTO.getAppId(), configDTO.buildKey()));
    }

    /**
     * 新增namespace
     *
     * @param namespace
     * @return
     */
    public void addNamespace(String namespace) {
        client.createPersistent(ROOT + "/" + namespace);
    }

    /**
     * 获取所有namespace
     *
     * @param
     * @return
     */
    public List<String> getAllNamespaces() {
        return client.getChildren(ROOT);
    }

    /**
     * 获取所有有权限namespace
     *
     * @param mobile
     * @return
     */
    public List<String> getValidNamespaces(String mobile) {
        return userService.getAuthorizedAppsByMobile(mobile);
    }


    /**
     * 获取该namespace下的所有配置信息
     *
     * @param namespace
     * @return
     */
    public List<SpiConfigDTO> getConfigs(String namespace) {
        Map<String, String> configs = client.getChildrenContent(ROOT + "/" + namespace);
        return configs.entrySet()
                .stream()
                .map(e -> SpiConfigDTO.buildConfigDTO(e.getKey(), e.getValue(), namespace))
                .filter(Objects::nonNull)
                .collect(toList());
    }

    /**
     * 查询namespace信息
     *
     * @return
     */
    public List<NamespaceCreateVO> getNamespacesDetail() {
        List<String> namespaces = client.getChildren(ROOT);

        return emptyIfNull(namespaces).stream()
                .map(NamespaceCreateVO::buildByOpenNamespaceDTO)
                .collect(toList());
    }

    private List<String> emptyIfNull(List<String> list) {
        if (list == null) return new ArrayList<>();
        return list;
    }

    /**
     * 根据手机号获取选项
     *
     * @param mobile
     * @return
     */
    public List<String> getValidOptions(String mobile) {
        //判断是否是管理员
        if (userService.isSuperAdmin(mobile)) {
            return getAllNamespaces();
        }
        //TODO 查询此员工下的所有namespace
        return null;
    }
}
