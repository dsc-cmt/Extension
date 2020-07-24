package com.cmt.extension.core.configcenter.model;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tuzhenxian
 * @date 20-7-20
 */
public class Spi {
    private String spiInterface;
    private List<Extension> extensions;
    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getSpiInterface() {
        return spiInterface;
    }

    public void setSpiInterface(String spiInterface) {
        this.spiInterface = spiInterface;
    }

    public List<Extension> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<Extension> extensions) {
        this.extensions = extensions;
    }

    public List<SpiConfigDTO> getConfigs(String appName) {
        List<SpiConfigDTO> configs = new ArrayList<>();
        if (extensions == null) {
            return configs;
        }
        return extensions.stream()
                .map(e -> e.build(appName, spiInterface))
                .collect(toList());
    }
}
