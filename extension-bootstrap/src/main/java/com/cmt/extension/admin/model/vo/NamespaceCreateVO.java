package com.cmt.extension.admin.model.vo;

import lombok.Data;

/**
 * @author tuzhenxian
 * @date 19-10-28
 */
@Data
public class NamespaceCreateVO {
    private String namespace;
    private String creator;
    private String createTime;


    public static NamespaceCreateVO buildByOpenNamespaceDTO(String namespace) {
        NamespaceCreateVO vo = new NamespaceCreateVO();
        vo.setNamespace(namespace);
        vo.setCreator("管理员");
        return vo;
    }
}
