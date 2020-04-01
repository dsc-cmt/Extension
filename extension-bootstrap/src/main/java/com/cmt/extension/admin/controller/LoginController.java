package com.cmt.extension.admin.controller;

import com.cmt.extension.admin.model.Constants;
import com.cmt.extension.admin.model.Result;
import com.cmt.extension.admin.model.dto.LoginInfoDTO;
import com.cmt.extension.admin.model.vo.UserInfoVO;
import com.cmt.extension.admin.model.entity.User;
import com.cmt.extension.admin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xieyong
 * @date 2020/3/25
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 账号登录
     * @param request
     * @param mobile
     * @param password
     * @return
     */
    @GetMapping("/account/action/login")
    public Result<LoginInfoDTO> login(HttpServletRequest request, String mobile, String password){
        User user = userService.login(mobile, password);
        UserInfoVO userInfoVO = UserInfoVO.buildUserDTO(user);
        request.getSession().setAttribute(Constants.USER_IDENTITY, userInfoVO);
        return  Result.success(LoginInfoDTO.buildLoginInfoDTO(user));
    }

    @GetMapping("/account/action/logout")
    public Result logout(HttpServletRequest request){
        request.getSession().removeAttribute(Constants.USER_IDENTITY);
        return  Result.success();
    }


}
