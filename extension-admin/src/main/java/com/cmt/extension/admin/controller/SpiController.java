package com.cmt.extension.admin.controller;

import com.cmt.extension.admin.model.Result;
import com.cmt.extension.admin.model.dto.DeleteSpiDTO;
import com.cmt.extension.admin.model.dto.NewSpiDTO;
import com.cmt.extension.admin.model.vo.SpiConfigVO;
import com.cmt.extension.admin.service.AppService;
import io.github.cmt.extension.common.model.SpiConfigDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    private AppService appService;

    @GetMapping("/config")
    public Result getConfig(String appName, String spiInterface,String bizCode) {
        SpiConfigDTO config = appService.getConfig(appName, spiInterface,bizCode);
        return Result.success(SpiConfigVO.buildByConfigDTO(config));
    }

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
    @GetMapping("/deleteConfig")
    public Result deleteConfig(@Validated(SpiConfigVO.DeleteValidate.class) SpiConfigVO configVO) {
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
    public Result addConfig(SpiConfigVO configVO, HttpServletResponse res) throws IOException {
        appService.addConfig(configVO.buildConfigDTO());
        res.sendRedirect("/appList");
        return Result.success();
    }

    /**
     * 更新一条配置
     *
     * @param configVO
     * @return
     */
    @PostMapping("/updateConfigs")
    public Result updateConfig(@Validated(SpiConfigVO.AddOrUpdateValidate.class) SpiConfigVO configVO, HttpServletResponse res) throws IOException {
        appService.updateConfig(configVO.buildConfigDTO());
        res.sendRedirect("/appList");
        return Result.success();
    }

    @PostMapping("/spis")
    public Result addSpi(@Validated NewSpiDTO newSpi) {
        appService.addSpi(newSpi);
        return Result.success();
    }

    @GetMapping("/deleteSpi")
    public Result deleteSpi(@Validated DeleteSpiDTO deleteSpi){
        appService.deleteSpi(deleteSpi);
        return Result.success();
    }
}
