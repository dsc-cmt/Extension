package com.cmt.extension.core.configcenter.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tuzhenxian
 * @date 20-7-20
 */
public class Application {
    private String appName;
    private List<Spi> spis;

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

    public List<SpiConfigDTO> getConfigs() {
        List<SpiConfigDTO> configs = new ArrayList<>();
        if (spis == null) {
            return configs;
        }
        return spis.stream()
                .map(spi->spi.getConfigs(appName))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
