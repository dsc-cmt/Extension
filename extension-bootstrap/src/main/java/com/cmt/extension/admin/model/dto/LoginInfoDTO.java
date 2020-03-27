package com.cmt.extension.admin.model.dto;

import com.cmt.extension.admin.model.entity.User;
import com.cmt.extension.admin.model.type.LoginStatusEnum;
import lombok.Builder;
import lombok.Data;

/**
 * @author xieyong
 * @date 2020/3/26
 * @Description: 登录信息
 */
@Data
@Builder
public class LoginInfoDTO {
    /**
     * 登录状态
     */
    private String status;

    /**
     * 当前的权限类型
     */
    private String currentAuthority;

    public static LoginInfoDTO buildLoginInfoDTO(User user){
        return LoginInfoDTO.builder()
                .currentAuthority(user.getRole())
                .status(LoginStatusEnum.OK.getDesc())
                .build();
    }
}
