package com.cmt.extension.admin.model.vo;

import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;

import java.text.SimpleDateFormat;
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


    public static NamespaceCreateVO buildByOpenNamespaceDTO(OpenNamespaceDTO dto){
        NamespaceCreateVO vo=new NamespaceCreateVO();
        vo.setNamespace(dto.getNamespaceName());
        vo.setCreator("管理员");
        vo.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dto.getDataChangeCreatedTime()));
        return vo;
    }
}
