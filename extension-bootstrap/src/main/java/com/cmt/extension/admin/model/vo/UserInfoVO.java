package com.cmt.extension.admin.model.vo;

import com.cmt.extension.admin.model.entity.User;
import com.cmt.extension.admin.model.type.RoleType;
import lombok.Builder;
import lombok.Data;
/**
 * @author xieyong
 * @date 2020/3/25
 * @Description:
 */
@Data
@Builder
public class UserInfoVO {
    private static final String DEFAULT_AVATAR = "https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png";;

    /**用户ID**/
    private Long userId;

    /**用户名称**/
    private String userName;

    /**用户头像**/
    private String avatar;

    /**用户权限
     * @see RoleType
     * **/
    private String currentAuthority;

    private String mobile;

    public static UserInfoVO buildUserDTO(User user){
        return UserInfoVO.builder()
                .userId(user.getId())
                .userName(user.getUserName())
                .currentAuthority(user.getRole())
                .avatar(DEFAULT_AVATAR)
                .mobile(user.getUserMobile())
                .build();
    }
}
