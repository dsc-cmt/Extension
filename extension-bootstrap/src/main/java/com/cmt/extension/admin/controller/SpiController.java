package com.cmt.extension.admin.controller;

import com.cmt.extension.admin.model.Constants;
import com.cmt.extension.admin.model.type.SysFlag;
import com.cmt.extension.admin.model.BusinessException;
import com.cmt.extension.admin.model.Result;
import com.cmt.extension.admin.model.vo.SpiConfigVO;
import com.cmt.extension.admin.model.vo.UserVO;
import com.cmt.extension.admin.service.SpiConfigService;
import com.cmt.extension.admin.service.UserService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.cmt.extension.core.configcenter.model.SpiConfigDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tuzhenxian
 * @date 19-10-9
 */
@CrossOrigin(allowCredentials = "true", maxAge = 3600, origins = "*")
@RestController
@Slf4j
public class SpiController {
    @Autowired
    private SpiConfigService spiConfigService;
    @Autowired
    private UserService userService;

    /**
     * 获取一个namespace下所有配置信息
     *
     * @param namespace
     * @return
     */
    @GetMapping("/admin/config")
    public Result getConfigs(String namespace) {
        List<SpiConfigDTO> configList=spiConfigService.getConfigs(namespace);
        return Result.success(configList.stream().map(SpiConfigVO::buildByConfigDTO).collect(Collectors.toList()));
    }

    /**
     * 创建一条配置
     *
     * @param configVO
     * @return
     */
    @PostMapping("/admin/createConfig")
    public Result addConfig(@RequestBody @Validated(SpiConfigVO.AddOrUpdateValidate.class) SpiConfigVO configVO) {
        if (!userService.isAdmin(configVO.getMobile(), configVO.getAppId())) {
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
    @PostMapping("/admin/updateConfig")
    public Result updateConfig(@RequestBody @Validated(SpiConfigVO.AddOrUpdateValidate.class) SpiConfigVO configVO) {
        if (!userService.isAdmin(configVO.getMobile(), configVO.getAppId())) {
            throw BusinessException.fail("没有权限，请联系管理员");
        }
        spiConfigService.updateConfig(configVO.buildConfigDTO());
        return Result.success();
    }

    /**
     * 删除一条配置
     *
     * @param configVO
     * @return
     */
    @DeleteMapping("/admin/config")
    public Result deleteConfig(@Validated(SpiConfigVO.DeleteValidate.class) SpiConfigVO configVO) {
        if (!userService.isAdmin(configVO.getMobile(), configVO.getAppId())) {
            throw BusinessException.fail("没有权限，请联系管理员");
        }
        spiConfigService.deleteConfig(configVO.buildConfigDTO());
        return Result.success();
    }

    /**
     * 新增一个namespace(应用)
     *
     * @param namespace
     * @return
     */
    @GetMapping("/admin/addNamespace")
    public Result addNamespace(String namespace) {
        spiConfigService.addNamespace(namespace);
        return Result.success();
    }

    /**
     * 获取所有namespace（应用）名称
     *
     * @param
     * @return
     */
    @GetMapping("/admin/namespaces")
    public Result getNamespaces() {
        return Result.success(spiConfigService.getAllNamespaces());
    }

    /**
     * 获取用户拥有admin权限的所有namespace(应用)名称
     *
     * @return
     */
    @GetMapping("/admin/validNamespaces")
    public Result getValidNamespaces(HttpServletRequest request) {
        //todo 保存用户信息到session
        UserVO userVO = (UserVO)request.getSession().getAttribute(Constants.USER_IDENTITY);
        return Result.success(spiConfigService.getValidNamespaces(userVO.getUserMobile()));
    }

    @GetMapping("/admin/getValidOptions")
    public Result getValidOptions(HttpServletRequest request){
        //todo 保存用户信息到session
        UserVO userVO = (UserVO)request.getSession().getAttribute(Constants.USER_IDENTITY);
        return Result.success(spiConfigService.getValidOptions(userVO.getUserMobile()));
    }

    /**
     * 已创建应用信息
     *
     * @param
     * @return
     */
    @GetMapping("/admin/namespacesDetail")
    public Result getNamespacesDetail() {
        return Result.success(spiConfigService.getNamespacesDetail());
    }

    /**
     * 新增授权用户
     *
     * @param user
     * @return
     */
    @PostMapping("/admin/createAuthorizedUser")
    public Result createAuthorizedUser(@RequestBody UserVO user, HttpServletRequest request) {
        UserVO userVO = (UserVO)request.getSession().getAttribute(Constants.USER_IDENTITY);
        user.setCreator(userVO.getUserName());
        user.setModifier(userVO.getUserName());
        user.setModifierMobile(userVO.getUserMobile());
        userService.checkAuth(user.getModifierMobile(),user.getAuthorizedApps(),request);
        user.setDateCreate(new Date());
        user.setDateModified(new Date());
        return Result.success(userService.saveAuthorizedUser(user.buildUser(SysFlag.VALID.getCode())));
    }

    /**
     * 获取授权用户
     *
     * @param
     * @return
     */
    @GetMapping("/admin/authorizedUsers")
    public Result getAuthorizedUsers() {
        return Result.success(userService.getAuthorizedUsers());
    }

    /**
     * 更新授权用户
     *
     * @param user
     * @return
     */
    @PostMapping("/admin/updateAuthorizedUser")
    public Result updateAuthorizedUser(@RequestBody UserVO user, HttpServletRequest request) {
        UserVO userVO = (UserVO)request.getSession().getAttribute(Constants.USER_IDENTITY);
        user.setModifier(userVO.getUserName());
        user.setModifierMobile(userVO.getUserMobile());
        userService.checkAuth(user.getModifierMobile(),user.getAuthorizedApps(),request);
        user.setDateModified(new Date());
        return Result.success(userService.saveAuthorizedUser(user.buildUser(SysFlag.VALID.getCode())));
    }

    /**
     * 删除授权用户
     *
     * @param user
     * @return
     */
    @PostMapping("/admin/deleteAuthorizedUser")
    public Result deleteAuthorizedUser(@RequestBody UserVO user, HttpServletRequest request) {
        user.setModifier(user.getUserName());
        user.setModifierMobile(user.getUserMobile());
        userService.checkAuth(user.getModifierMobile(),user.getAuthorizedApps(),request);
        user.setDateModified(new Date());
        userService.deleteAuthorizedUser(user.getId());
        return Result.success();
    }

    /**
     * 获取已授权的所有应用
     *
     * @param mobile
     * @return
     */
    @GetMapping("/admin/authorizedApps")
    public Result getAuthorizedApps(String mobile) {
        return Result.success(userService.getAuthorizedAppsByMobile(mobile));
    }
}