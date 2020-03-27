package com.cmt.extension.admin.service;

import com.cmt.extension.admin.model.BusinessException;
import com.cmt.extension.admin.model.Constants;
import com.cmt.extension.admin.model.entity.User;
import com.cmt.extension.admin.model.type.RoleType;
import com.cmt.extension.admin.model.type.SysFlag;
import com.cmt.extension.admin.model.vo.UserVO;
import com.cmt.extension.admin.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tuzhenxian
 * @date 19-10-25
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User login(String mobile, String password){
        User user = userRepository.findByUserMobileAndPassword(mobile, password);
        if(Objects.isNull(user)){
            BusinessException.fail("账号或密码错误");
        }
        if(SysFlag.INVALID.equals(user.getSysFlag())){
            BusinessException.fail("用户已被冻结");
        }
        return user;
    }

    public User saveAuthorizedUser(User user) {
        return userRepository.save(user);
    }

    public List<UserVO> getAuthorizedUsers() {
        return userRepository.findBySysFlag(SysFlag.VALID.getCode()).stream()
                .map(UserVO::buildByUser).collect(Collectors.toList());
    }

    public List<String> getAuthorizedAppsByMobile(String mobile) {
        User users = userRepository.findByUserMobileAndSysFlag(mobile, SysFlag.VALID.getCode());
        return users != null && users.getAuthorizedApps() != null ? Splitter.on(",").omitEmptyStrings().splitToList(users.getAuthorizedApps()) : null;
    }

    /**
     * 判断用户是否具有该namespace的权限
     *
     * @param mobile
     * @param namespace
     * @return
     */
    public boolean hasNamespaceAuth(String mobile, String namespace) {
        if(StringUtils.isAnyEmpty(mobile,namespace)) {
            throw BusinessException.fail("没有权限");
        }
        User users = userRepository.findByUserMobileAndSysFlag(mobile, SysFlag.VALID.getCode());
        if(users == null || StringUtils.isBlank(users.getAuthorizedApps())){
            throw BusinessException.fail("没有权限");
        }
        List<String> authNamespace = Splitter.on(",").trimResults().splitToList(users.getAuthorizedApps());
        return authNamespace.contains(namespace);
    }

    public void deleteAuthorizedUser(Long id) {
        Optional<User> user=userRepository.findById(id);
        User u = user.orElseThrow(()->BusinessException.fail("根据ID未找到user,id:"+id));
        u.setSysFlag(SysFlag.INVALID.getCode());
        userRepository.save(u);
    }

    public void checkAuth(String modifierMobile, String app){
        //判断是否是超级管理员
        if(isSuperAdmin(modifierMobile)){
            return;
        }
        throw BusinessException.fail("您没有应用管理权限,应用："+app);
    }

    public boolean isSuperAdmin(String mobile){
        User user =  userRepository.findByUserMobileAndSysFlag(mobile,SysFlag.VALID.getCode());
        return RoleType.ADMIN.getDesc().equals(user.getRole());

    }
    public User getValidUserByMobile(String mobile){
        return userRepository.findByUserMobileAndSysFlag(mobile, SysFlag.VALID.getCode());
    }
}
