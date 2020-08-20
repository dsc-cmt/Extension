package io.github.cmt.extension.common.model;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tuzhenxian
 * @date 20-7-20
 */
@Slf4j
public class Application {
    private String appName;
    private List<Spi> spis;
    private Integer version;

    private Consumer<SpiConfigChangeEvent> consumer;

    public static Application empty() {
        Application application = new Application();
        application.setVersion(-1);
        return application;
    }

    public static Application emptyIfNull(Application application) {
        return application == null ? empty() : application;
    }

    public void update(Application newApp) {
        if (newApp == null || newApp.getVersion() <= this.version) return;
        SpiConfigChangeEvent event = SpiConfigChangeEvent.generateEvent(this, newApp);
        try{
            consumer.accept(event);
        }
        catch (Exception e){
            log.error("发布spi配置变更事件失败",e);
        }
        this.spis = newApp.getSpis();
        this.version = newApp.getVersion();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public List<Spi> getSpis() {
        return spis;
    }

    public void setSpis(List<Spi> spis) {
        this.spis = spis;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<SpiConfigDTO> buildConfigs() {
        List<SpiConfigDTO> configs = new ArrayList<>();
        if (spis == null) {
            return configs;
        }
        return spis.stream()
                .map(spi -> spi.getConfigs(appName))
                .flatMap(Collection::stream)
                .collect(toList());
    }

    public Map<String, SpiConfigDTO> buildConfigMap() {
        return buildConfigs()
                .stream()
                .collect(toMap(SpiConfigDTO::buildKey, c -> c));
    }

    public void setConsumer(Consumer<SpiConfigChangeEvent> consumer) {
        this.consumer = consumer;
    }
}
