package com.soft1851.enums;

/**
 * 枚举类
 * @author crq
 */
public enum  UserStatus {
    //用户状态枚举
    INACTIVE(0,"未激活"),
    ACTIVE(1,"已激活"),
    FROZEN(2,"已冻结");
    public final Integer type;
    public final String value;
    UserStatus(Integer type,String value){
        this.type = type;
        this.value = value;
    }

    public static boolean isUserStatusValid(Integer tempStatus) {
        if (tempStatus != null){
            if (tempStatus.equals(INACTIVE.type) || tempStatus.equals(ACTIVE.type) || tempStatus.equals(FROZEN.type)){
                return true;
            }
        }
        return false;
    }
}
