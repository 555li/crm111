package com.bjpowernode.crm.commons.utils;

import java.util.UUID;

public class UUIDutils {
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
