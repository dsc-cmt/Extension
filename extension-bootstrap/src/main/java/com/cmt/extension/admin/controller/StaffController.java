package com.cmt.extension.admin.controller;

import com.cmt.extension.admin.model.BusinessException;
import com.cmt.extension.admin.model.Constants;
import com.cmt.extension.admin.model.Result;
import com.cmt.extension.admin.model.entity.User;
import com.cmt.extension.admin.model.type.SysFlag;
import com.cmt.extension.admin.model.vo.UserInfoVO;
import com.cmt.extension.admin.model.vo.UserVO;
import com.cmt.extension.admin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author xieyong
 * @date 2020/3/27
 * @Description:
 */
@CrossOrigin(allowCredentials = "true", maxAge = 3600, origins = "*")
@RestController
@Slf4j
public class StaffController {

    @Autowired
    private UserService userService;

    /**
     * 获取授权用户
     *
     * @param
     * @return
     */
    @GetMapping("/authorization_users")
    public Result<List<UserVO>> getAuthorizedUsers() {
        return Result.success(userService.getAuthorizedUsers());
    }

    /**
     * 新增授权用户
     *
     * @param user
     * @return
     */
    @PostMapping("/authorization_users")
    public Result createAuthorizedUser(@RequestBody UserVO user, HttpServletRequest request) {
        UserInfoVO userInfoVO = (UserInfoVO) request.getSession().getAttribute(Constants.USER_IDENTITY);
        user.setCreator(userInfoVO.getUserName());
        user.setModifier(userInfoVO.getUserName());
        user.setModifierMobile(userInfoVO.getMobile());
        userService.checkAuth(user.getModifierMobile(), user.getAuthorizedApps());
        User oldUser = userService.getValidUserByMobile(user.getUserMobile());
        if (oldUser != null) {
            throw new BusinessException("手机号已存在");
        }
        user.setDateCreate(new Date());
        user.setDateModified(new Date());
        userService.saveAuthorizedUser(user.buildUser(SysFlag.VALID.getCode()));
        return Result.success();
    }

    /**
     * 更新授权用户
     *
     * @param user
     * @return
     */
    @PatchMapping("/authorization_users")
    public Result updateAuthorizedUser(@RequestBody UserVO user, HttpServletRequest request) {
        UserInfoVO userInfoVO = (UserInfoVO) request.getSession().getAttribute(Constants.USER_IDENTITY);
        user.setModifier(userInfoVO.getUserName());
        user.setModifierMobile(userInfoVO.getMobile());
        userService.checkAuth(user.getModifierMobile(), user.getAuthorizedApps());
        User oldUser = userService.getValidUserByMobile(user.getUserMobile());
        if (oldUser != null && !oldUser.getId().equals(user.getId())) {
            throw new BusinessException("手机号已存在");
        }
        user.setDateModified(new Date());
        userService.saveAuthorizedUser(user.buildUser(SysFlag.VALID.getCode()));
        return Result.success();
    }

    /**
     * 删除授权用户
     *
     * @param user
     * @return
     */
    @DeleteMapping("/authorization_users")
    public Result deleteAuthorizedUser(@RequestBody UserVO user, HttpServletRequest request) {
        UserInfoVO userInfoVO = (UserInfoVO) request.getSession().getAttribute(Constants.USER_IDENTITY);
        user.setModifier(userInfoVO.getUserName());
        user.setModifierMobile(userInfoVO.getMobile());
        userService.checkAuth(user.getModifierMobile(), user.getAuthorizedApps());
        user.setDateModified(new Date());
        userService.deleteAuthorizedUser(user.getId());
        return Result.success();
    }
}
