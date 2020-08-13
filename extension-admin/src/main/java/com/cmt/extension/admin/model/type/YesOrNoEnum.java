package com.cmt.extension.admin.model.type;

import lombok.Getter;

/**
 * @author tuzhenxian
 * @date 19-11-19
 */
@Getter
public enum YesOrNoEnum {
    YES(1, "是"),
    NO(0, "否"),
    ;
    private Integer code;
    private String desc;

    YesOrNoEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(Integer code){
        for(YesOrNoEnum yesOrNo:YesOrNoEnum.values()){
            if(yesOrNo.code.equals(code)){
                return yesOrNo.desc;
            }
        }
        return null;
    }

    public static boolean getBooleanByCode(Integer code){
        return Integer.valueOf(1).equals(code);
    }
}
