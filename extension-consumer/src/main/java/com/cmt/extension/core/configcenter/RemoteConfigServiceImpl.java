package com.cmt.extension.core.configcenter;

import com.alibaba.fastjson.JSONObject;
import com.cmt.extension.core.configcenter.model.Application;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author tuzhenxian
 * @date 20-7-24
 */
public class RemoteConfigServiceImpl implements ConfigService {
    private static final Logger log = LoggerFactory.getLogger(RemoteConfigServiceImpl.class);
    private static final String PORTAL_KEY = "spi.portal";
    private static final ScheduledExecutorService EXECUTOR;
    private static String portalUrl;
    private String appName;

    static {
        Properties props = new Properties();
        String portal;
        if ((portal = System.getProperty(PORTAL_KEY)) == null) {
            try (InputStream in = RemoteConfigServiceImpl.class.getClassLoader().getResourceAsStream("application.properties")) {
                props.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            portal = props.getProperty(PORTAL_KEY);
        }
        Assert.notNull(portal, "spi portal不可为空");
        portalUrl = portal + "/openApi/application";
        EXECUTOR = Executors.newScheduledThreadPool(1,new ThreadFactoryBuilder().setNameFormat("spi-sync-thread-%d").build());
    }

    @Override
    public Application init(String appName) {
        this.appName=appName;
        Application app=Application.emptyIfNull(getApplication(-1));
        log.info("加载远程spi配置,version:{}",app.getVersion());
        return app;
    }


    @Override
    public void periodicRefresh() {
        Application app=ConfigCenter.getInstance().getCachedApplication();
        Integer version=app.getVersion();
        EXECUTOR.scheduleAtFixedRate(()->{
            Application newApp=getApplication(version);
            app.update(newApp);
        },10,10, TimeUnit.SECONDS);
    }

    private Application getApplication(int version) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url=portalUrl + "?version="+version+"&appName=" + appName;
        HttpGet request = new HttpGet(url);
        CloseableHttpResponse response = null;
        Application application = null;
        try {
            log.debug("查询应用spi配置,version:{}",version);
            response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            application = JSONObject.parseObject(EntityUtils.toString(entity), Application.class);
        } catch (IOException e) {
            log.error("查询远程spi配置失败",e);
        }
        return application;
    }
}
