package com.cmt.extension.admin.controller;

import com.cmt.extension.admin.model.Result;
import com.cmt.extension.admin.service.SpiConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xieyong
 * @date 2020/3/27
 * @Description:
 */
@CrossOrigin(allowCredentials = "true", maxAge = 3600, origins = "*")
@RestController
@Slf4j
public class ApplicationController {
    @Autowired
    private SpiConfigService spiConfigService;

    /**
     * 已创建应用信息
     *
     * @param
     * @return
     */
    @GetMapping("/applications")
    public Result getNamespacesDetail() {
        return Result.success(spiConfigService.getNamespacesDetail());
    }

    /**
     * 新增一个namespace(应用)
     *
     * @param namespace
     * @return
     */
    @PostMapping("/applications")
    public Result addNamespace(@RequestBody String namespace) {
        spiConfigService.addNamespace(namespace);
        return Result.success();
    }

}
