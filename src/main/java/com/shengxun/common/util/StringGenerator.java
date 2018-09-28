package com.shengxun.common.util;

import java.util.UUID;

public class StringGenerator {

    public static String get32UUID() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }
}
