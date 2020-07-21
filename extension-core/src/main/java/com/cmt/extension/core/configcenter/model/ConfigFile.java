package com.cmt.extension.core.configcenter.model;

import java.util.List;
import org.springframework.util.Assert;

/**
 * @author tuzhenxian
 * @date 20-7-20
 */
public class ConfigFile {
    private List<Application> applications;

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public Application getApplication(String appName) {
        Assert.notNull(appName, "应用名称不可为空");
        if (applications == null) {
            return null;
        }
        return applications.stream()
                .filter(a -> appName.equals(a.getAppName()))
                .findAny()
                .orElseGet(Application::new);
    }
}
