package com.cmt.extension.admin.controller;

import com.cmt.extension.admin.model.BusinessException;
import com.cmt.extension.admin.model.Constants;
import com.cmt.extension.admin.model.Result;
import com.cmt.extension.admin.model.vo.SpiConfigVO;
import com.cmt.extension.admin.model.vo.UserInfoVO;
import com.cmt.extension.admin.service.SpiConfigService;
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
public class SpiController {
    @Autowired
    private SpiConfigService spiConfigService;
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
        if (userService.isSuperAdmin(userInfoVO.getMobile())) {
            //获取所有的namespace
            return Result.success(spiConfigService.getValidOptions(userInfoVO.getMobile()));
        }
        return Result.success(spiConfigService.getValidNamespaces(userInfoVO.getMobile()));
    }

    @GetMapping("/rightfulness_options")
    public Result getValidOptions(HttpServletRequest request) {
        UserInfoVO userInfoVO = (UserInfoVO) request.getSession().getAttribute(Constants.USER_IDENTITY);
        return Result.success(spiConfigService.getValidOptions(userInfoVO.getMobile()));
    }

    /**
     * 获取一个namespace下所有配置信息
     *
     * @param namespace
     * @return
     */
    @GetMapping("/configs")
    public Result getConfigs(String namespace) {
        List<SpiConfigDTO> configList = spiConfigService.getConfigs(namespace);
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
        if (!userService.hasNamespaceAuth(configVO.getMobile(), configVO.getAppId())) {
            throw BusinessException.fail("没有权限，请联系管理员");
        }
        spiConfigService.deleteConfig(configVO.buildConfigDTO());
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
        if (!userService.hasNamespaceAuth(configVO.getMobile(), configVO.getAppId())) {
            throw BusinessException.fail("没有权限，请联系管理员");
        }
        spiConfigService.addConfig(configVO.buildConfigDTO());
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
        if (!userService.hasNamespaceAuth(configVO.getMobile(), configVO.getAppId())) {
            throw BusinessException.fail("没有权限，请联系管理员");
        }
        spiConfigService.updateConfig(configVO.buildConfigDTO());
        return Result.success();
    }
}
