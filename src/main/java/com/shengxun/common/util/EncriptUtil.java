package com.shengxun.common.util;

import java.security.MessageDigest;
import java.util.Base64;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EncriptUtil {

    public static String md5(String input) {
        byte[] digestBytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            digestBytes = md.digest();
        } catch (Exception e) {
            log.warn("MD5 error.", e);
        }
        int i;
        String output = null;
        StringBuffer outputBuffer = new StringBuffer(64);
        for (int offset = 0, length = digestBytes.length; offset < length; offset++) {
            i = digestBytes[offset];
            if (i < 0) {
                i += 256;
            }
            outputBuffer.append(String.format("%2s", Integer.toHexString(i)).replace(' ', '0'));
        }
        output = outputBuffer.toString();
        return output;
    }
    
    public static String base64(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    public static String encriptByMd5(String timeStamp,String appId,String appSecret){
        StringBuilder sb = new StringBuilder(64);
        sb.append("timestamp=").append(timeStamp).append("&").append("appid=").append(appId).append("&")
        .append("secret=").append(appSecret);
        byte[] digestBytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sb.toString().getBytes("UTF-8"));
            digestBytes = md.digest();
        } catch (Exception e) {
            log.warn("MD5 error.", e);
        }
        return bytesToHex(digestBytes);
    }
    private static String bytesToHex(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }
    
    public static void main(String[] args){
        System.out.println(encriptByMd5("1535365188","hdkadhjsdaAASdajk2312","asdkajsdkanffsnkj@471&$"));
    }
}
