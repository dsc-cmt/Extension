package com.cmt.extension.core.configcenter;

import static java.util.stream.Collectors.toMap;

import com.cmt.extension.core.configcenter.model.ConfigFile;
import com.cmt.extension.core.configcenter.model.SpiConfigDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tuzhenxian
 * @date 20-7-20
 */
public class LocalConfigServiceImpl implements ConfigService {
    private static final String CONFIG_FILE = "spi.yml";

    @Override
    public Map<String, SpiConfigDTO> loadConfig(String appName) {
        ConfigFile config = null;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            config = mapper.readValue(in, ConfigFile.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return build(appName, config);
    }

    private Map<String, SpiConfigDTO> build(String appName, ConfigFile config) {
        if (config == null || config.getApplications() == null) {
            return new HashMap<>();
        }
        return config.getApplication(appName)
                .getConfigs()
                .stream()
                .collect(toMap(SpiConfigDTO::buildKey, c -> c));
    }
}
