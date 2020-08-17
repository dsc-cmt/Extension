package com.cmt.extension.admin.controller;

import com.cmt.extension.admin.service.AppService;

import io.github.cmt.extension.common.model.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tuzhenxian
 * @date 20-8-12
 */
@RestController
@RequestMapping("/openApi")
public class OpenServiceController {
    @Autowired
    private AppService appService;

    @GetMapping("/application")
    public Application getApplicationVersion(String appName, Integer version) {
        return appService.getApplication(appName, version);
    }
}
