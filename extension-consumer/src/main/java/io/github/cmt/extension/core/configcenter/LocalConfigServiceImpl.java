package io.github.cmt.extension.core.configcenter;

import io.github.cmt.extension.common.model.Application;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;

/**
 * LocalConfigServiceImpl读取本地配置
 *
 * @author tuzhenxian
 * @date 20-7-20
 */
@Slf4j
public class LocalConfigServiceImpl implements ConfigService {
    private static final String CONFIG_FILE = "spi.yml";

    @Override
    public Application init(String appName) {
        Application application = null;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            application = mapper.readValue(in, Application.class);
        } catch (IOException e) {
            log.error("Load local config file error,please check spi.yml ", e);
            throw new RuntimeException(e);
        }
        if (application == null) {
            return Application.empty();
        }
        application.setVersion(0);
        return application;
    }
}
