package com.soft1851.enums;

/**
 * @author crq
 */

public enum Sex {
    man(0,"男"),
    woman(1,"女");

    public final Integer type;
    public final String value;
    Sex(Integer type,String value){
        this.type = type;
        this.value = value;
    }
}
