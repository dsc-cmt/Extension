package com.cmt.extension.admin.controller;

import javax.servlet.http.HttpServletRequest;

import com.cmt.extension.admin.model.Constants;
import com.cmt.extension.admin.model.Result;
import com.cmt.extension.admin.model.vo.UserInfoVO;
import com.cmt.extension.admin.service.AppService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xieyong
 * @date 2020/3/27
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/api")
public class ApplicationController {
    @Autowired
    private AppService appService;

    /**
     * 已创建应用信息
     *
     * @param
     * @return
     */
    @GetMapping("/applications")
    public Result getApplications() {
        return Result.success(appService.getAllApps());
    }

    /**
     * 新增一个namespace(应用)
     *
     * @param appName
     * @return
     */
    @PostMapping("/applications")
    public Result addAppName(@RequestBody String appName, HttpServletRequest request) {
        UserInfoVO userInfoVO = (UserInfoVO) request.getSession().getAttribute(Constants.USER_IDENTITY);
        appService.addApp(appName, userInfoVO.getUserId());
        return Result.success();
    }

    @GetMapping("/application")
    public Result getApplicationVersion(String appName, Integer version) {
        appService.getApplication(appName, version);
        return Result.success();
    }

    @GetMapping("/spis")
    public Result getSpis(String appName){
        return Result.success(appService.getSpis(appName));
    }
}
