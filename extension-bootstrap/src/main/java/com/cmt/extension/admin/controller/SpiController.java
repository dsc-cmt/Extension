package com.cmt.extension.admin.controller;

import com.cmt.extension.admin.model.BusinessException;
import com.cmt.extension.admin.model.Result;
import com.cmt.extension.admin.model.dto.NewSpiDTO;
import com.cmt.extension.admin.model.vo.SpiConfigVO;
import com.cmt.extension.admin.service.AppService;
import com.cmt.extension.admin.service.UserService;
import com.cmt.extension.core.configcenter.model.SpiConfigDTO;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tuzhenxian
 * @date 19-10-9
 */
@RestController
@Slf4j
@RequestMapping("/api")
public class SpiController {
    @Autowired
    private AppService appService;

    @Autowired
    private UserService userService;

    /**
     * 获取一个namespace下所有配置信息
     *
     * @param appName
     * @return
     */
    @GetMapping("/configs")
    public Result getConfigs(String appName, String spiInterface) {
        List<SpiConfigDTO> configList = appService.getConfigs(appName, spiInterface);
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
        appService.deleteConfig(configVO.buildConfigDTO());
        return Result.success();
    }

    /**
     * 创建一条配置
     *
     * @param configVO
     * @return
     */
    @PostMapping("/configs")
    public Result addConfig(SpiConfigVO configVO) {
        appService.addConfig(configVO.buildConfigDTO());
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
        appService.updateConfig(configVO.buildConfigDTO());
        return Result.success();
    }

    @PostMapping("/spis")
    public Result addSpi(@Validated NewSpiDTO newSpi) {
        appService.addSpi(newSpi);
        return Result.success();
    }
}
