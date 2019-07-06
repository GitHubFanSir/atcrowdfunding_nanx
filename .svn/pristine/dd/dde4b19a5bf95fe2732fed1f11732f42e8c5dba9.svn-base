package com.atnanx.atcrowdfunding.core.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * Created by geely
 */
public class MD5Util {

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

   /* private static char toHex(int nibble) {
        return hexDigit[(nibble & 0xF)];
    }*/

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            //首先256默认是int，
            //将整形n范围提到[0,255], b[-128,0]+255 是[127,255]，+[0,127]的正值，所以是[0,255]
            n += 256;

        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * 返回大写MD5
     *
     * @param origin
     * @param charsetname
     * @return
     */
    private static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                //digest 摘要
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString.toUpperCase();
    }

    public static String MD5EncodeUtf8(String origin) {
        origin = origin + PropertiesUtil.getProperty("password.salt", "");//key值取不到value时才用默认值
        return MD5Encode(origin, "utf-8");
    }


    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};


    public static void main(String[] args) {
        String encodeUtf8 = MD5EncodeUtf8("123456");
        try {
            String string = byteArrayToHexString("123456".getBytes("utf-8"));
            System.out.println(string);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(encodeUtf8);
    }
}
