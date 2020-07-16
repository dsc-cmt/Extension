package com.cmt.extension.admin.controller;

import com.cmt.extension.admin.model.Constants;
import com.cmt.extension.admin.model.Result;
import com.cmt.extension.admin.model.vo.UserInfoVO;
import com.cmt.extension.admin.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    private ConfigService configService;

    /**
     * 已创建应用信息
     *
     * @param
     * @return
     */
    @GetMapping("/applications")
    public Result getApplications() {
        return Result.success(configService.getAllApps());
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
        configService.addApp(appName, userInfoVO.getUserId());
        return Result.success();
    }

}
