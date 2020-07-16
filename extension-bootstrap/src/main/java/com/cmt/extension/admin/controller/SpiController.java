package com.cmt.extension.admin.controller;

import com.cmt.extension.admin.model.BusinessException;
import com.cmt.extension.admin.model.Constants;
import com.cmt.extension.admin.model.Result;
import com.cmt.extension.admin.model.vo.SpiConfigVO;
import com.cmt.extension.admin.model.vo.UserInfoVO;
import com.cmt.extension.admin.service.ConfigService;
import com.cmt.extension.admin.service.UserService;
import com.cmt.extension.core.configcenter.model.SpiConfigDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tuzhenxian
 * @date 19-10-9
 */
@RestController
@Slf4j
@RequestMapping("/api")
public class SpiController {
    @Autowired
    private ConfigService configService;

    @Autowired
    private UserService userService;

    /**
     * 获取用户拥有admin权限的所有namespace(应用)名称
     *
     * @return
     */
    @GetMapping("/rightfulness_namespaces")
    public Result getValidNamespaces(HttpServletRequest request) {
        UserInfoVO userInfoVO = (UserInfoVO) request.getSession().getAttribute(Constants.USER_IDENTITY);
        String mobile = userInfoVO.getMobile();
        if (userService.isSuperAdmin(mobile)) {
            //获取所有的namespace
            return Result.success(configService.getAllApps());
        }
        return Result.success(userService.getAuthorizedAppsByMobile(mobile));
    }

    @GetMapping("/rightfulness_options")
    public Result getValidOptions(HttpServletRequest request) {
        UserInfoVO userInfoVO = (UserInfoVO) request.getSession().getAttribute(Constants.USER_IDENTITY);
        String mobile = userInfoVO.getMobile();
        if (userService.isSuperAdmin(mobile)) {
            //获取所有的namespace
            return Result.success(configService.getAllApps());
        }
        return Result.success(userService.getAuthorizedAppsByMobile(mobile));
    }

    /**
     * 获取一个namespace下所有配置信息
     *
     * @param appName
     * @return
     */
    @GetMapping("/configs")
    public Result getConfigs(String appName) {
        List<SpiConfigDTO> configList = configService.getConfigs(appName);
        return Result.success(configList.stream().map(SpiConfigVO::buildByConfigDTO).collect(Collectors.toList()));
    }

    /**
     * 删除一条配置
     *
     * @param configVO
     * @return
     */
    @DeleteMapping("/configs")
    public Result deleteConfig(@Validated(SpiConfigVO.DeleteValidate.class) @RequestBody SpiConfigVO configVO) {
        if (!userService.hasNamespaceAuth(configVO.getMobile(), configVO.getAppName())) {
            throw BusinessException.fail("没有权限，请联系管理员");
        }
        configService.deleteConfig(configVO.buildConfigDTO());
        return Result.success();
    }

    /**
     * 创建一条配置
     *
     * @param configVO
     * @return
     */
    @PostMapping("/configs")
    public Result addConfig(@RequestBody @Validated(SpiConfigVO.AddOrUpdateValidate.class) SpiConfigVO configVO) {
        if (!userService.hasNamespaceAuth(configVO.getMobile(), configVO.getAppName())) {
            throw BusinessException.fail("没有权限，请联系管理员");
        }
        configService.addConfig(configVO.buildConfigDTO());
        return Result.success();
    }

    /**
     * 更新一条配置
     *
     * @param configVO
     * @return
     */
    @PatchMapping("/configs")
    public Result updateConfig(@RequestBody @Validated(SpiConfigVO.AddOrUpdateValidate.class) SpiConfigVO configVO) {
        if (!userService.hasNamespaceAuth(configVO.getMobile(), configVO.getAppName())) {
            throw BusinessException.fail("没有权限，请联系管理员");
        }
        configService.updateConfig(configVO.buildConfigDTO());
        return Result.success();
    }
}
