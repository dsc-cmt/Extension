package com.cmt.extension.core.configcenter;

import static com.cmt.extension.core.configcenter.model.SpiChangeType.matchZooKeeperType;

import com.cmt.extension.core.common.SpiException;
import com.cmt.extension.core.configcenter.model.SpiChangeType;
import com.cmt.extension.core.configcenter.model.SpiConfigChangeDTO;
import com.cmt.extension.core.configcenter.model.SpiConfigDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.springframework.util.Assert;

/**
 * @author tuzhenxian
 * @date 20-6-28
 */
@Slf4j
public class ZooKeeperConfigServiceImpl implements ConfigService {
    private static final String ROOT = "/spi-admin";
    private static final String SPILITOR = "/";
    private static final String CHARSET = "UTF-8";

    private CuratorZookeeperClient zkClient;
    private String namespace;

    private static String getRootPath(String namespace) {
        return ROOT + SPILITOR + namespace;
    }

    @Override
    public Map<String, SpiConfigDTO> syncConfig(String namespace) {
        Assert.notNull(namespace, "namespace不可为空");
        zkClient = CuratorZookeeperClient.instance();
        this.namespace = namespace;
        Map<String, String> configs = zkClient.getChildrenContent(getRootPath(namespace));
        Map<String, SpiConfigDTO> map = emptyIfNull(configs)
                .entrySet()
                .stream()
                .map(entry -> SpiConfigDTO.buildConfigDTO(entry.getKey(), entry.getValue(), namespace))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(SpiConfigDTO::buildKey, c -> c));
        log.info("[spi]缓存 {} spi扩展点信息共{}条", namespace, map.size());
        return map;
    }

    @Override
    public void onChange(Consumer<SpiConfigChangeDTO> consumer) {
        Assert.notNull(namespace, "namespace不可为空");
        String path = getRootPath(namespace);
        if (!zkClient.checkExists(path)) {
            log.error("spi配置中心没有配置相应的路径:{}", namespace);
            throw SpiException.fail("spi配置中心没有配置相应的路径:" + namespace);
        }
        //监听namespace下节点变更事件
        PathChildrenCache cache = new PathChildrenCache(zkClient.getClient(), path, true);
        cache.getListenable().addListener((c, event) -> {
            SpiChangeType type = matchZooKeeperType(event.getType());
            if (type == null) {
                return;
            }
            ChildData data = event.getData();
            String key = data.getPath().substring(data.getPath().lastIndexOf(SPILITOR) + 1);
            String value = new String(data.getData(), CHARSET);
            log.debug("spi配置变更,key:{},value:{},type:{}", key, value, type.name());
            consumer.accept(new SpiConfigChangeDTO(SpiConfigDTO.buildConfigDTO(key, value, namespace), type));
        });
        try {
            cache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        } catch (Exception e) {
            log.error("PathChildrenCache 启动失败");
            throw SpiException.fail("PathChildrenCache 启动失败");
        }
    }

    private Map<String, String> emptyIfNull(Map<String, String> configs) {
        if (configs == null) return new HashMap<>();
        return configs;
    }
}
