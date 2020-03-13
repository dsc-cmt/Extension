package com.cmt.extension.admin.service;

import com.cmt.extension.admin.model.BusinessException;
import com.cmt.extension.admin.model.Constants;
import com.cmt.extension.admin.model.entity.User;
import com.cmt.extension.admin.model.type.RoleType;
import com.cmt.extension.admin.model.type.SysFlag;
import com.cmt.extension.admin.model.vo.UserVO;
import com.cmt.extension.admin.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tuzhenxian
 * @date 19-10-25
 */
@Service
public class UserService {
    private static final String ADMIN_CODE="NEW_SPI_APP";

    @Autowired
    UserRepository userRepository;

    public User saveAuthorizedUser(User user) {
        return userRepository.save(user);
    }

    public List<UserVO> getAuthorizedUsers() {
        return userRepository.findBySysFlag(SysFlag.VALID.getCode()).stream()
                .map(UserVO::buildByUser).collect(Collectors.toList());
    }

    public Map<String, List<String>> getAuthorizedAppsByMobile(String mobile) {
        List<User> users = userRepository.findByUserMobileAndSysFlag(mobile, SysFlag.VALID.getCode());
        Map<String, List<String>> userApps = new HashMap<>();
        for (User user : users) {
            List<String> list=new ArrayList<>();
            list.add(user.getAuthorizedApps());
            userApps.merge(user.getRole(), list,(o,n)->{o.addAll(n);return o;});
        }
        return userApps;
    }

    /**
     * 判断用户是否具有该namespace的admin权限
     *
     * @param mobile
     * @param namespace
     * @return
     */
    public boolean isAdmin(String mobile, String namespace) {
        if(StringUtils.isAnyEmpty(mobile,namespace)){
            throw BusinessException.fail("没有权限");
        }
        List<User> users = userRepository.findByUserMobileAndSysFlag(mobile, SysFlag.VALID.getCode());
        List<String> namespaces = users.stream()
                .filter(u -> RoleType.ADMIN.getDesc().equals(u.getRole()))
                .map(User::getAuthorizedApps)
                .map(s -> s.split(","))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
        return namespaces.contains(namespace);
    }

    public void deleteAuthorizedUser(Long id) {
        Optional<User> user=userRepository.findById(id);
        User u = user.orElseThrow(()->BusinessException.fail("根据ID未找到user,id:"+id));
        u.setSysFlag(SysFlag.INVALID.getCode());
        userRepository.save(u);
    }

    public void checkAuth(String modifierMobile, String app, HttpServletRequest request){
        //应用管理员
        if(isAdmin(modifierMobile,app)){
            return;
        }
        //todo 判断是否超级管理员
        /*if(AuthNHolder.hasResAccess(ADMIN_CODE)){
            return;
        }*/
        throw BusinessException.fail("您没有应用管理权限,应用："+app);
    }

    public boolean isSuperAdmin(){
        return true;
        //todo 判断是否超级管理员
        //return AuthNHolder.hasResAccess(ADMIN_CODE);
    }
}
