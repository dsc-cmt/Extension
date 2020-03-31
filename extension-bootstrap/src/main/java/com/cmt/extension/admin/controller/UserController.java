package com.cmt.extension.admin.controller;

import com.cmt.extension.admin.model.Constants;
import com.cmt.extension.admin.model.Result;
import com.cmt.extension.admin.model.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xieyong
 * @date 2020/3/26
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/api")
public class UserController {

    @GetMapping("/users")
    public Result<UserInfoVO> getUserInfo(HttpServletRequest request){
        UserInfoVO userInfoVO = (UserInfoVO)request.getSession().getAttribute(Constants.USER_IDENTITY);
        return Result.success(userInfoVO);
    }
}
