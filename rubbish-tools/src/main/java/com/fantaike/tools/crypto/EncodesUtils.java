package com.fantaike.tools.crypto;

import cn.hutool.log.Log;
import com.fantaike.tools.exception.ExceptionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 *   @ClassName: EncodesUtils
 *   @Description: 推荐直接使用 HexUtil 和 Base64
 *   @Author: FROM MDD-PC
 *   @Date: 2019\12\27 0027 15:53
 *   @Version: v1.0 文件初始创建
 */
@Deprecated
public class EncodesUtils {
    private static final Log logger = Log.get();

    private static final String DEFAULT_URL_ENCODING = "UTF-8";
    private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    public static String encodeHex(byte[] input) {
//        return Hex.encodeHexString(input);
        return "";
    }

    public static byte[] decodeHex(String input) {
        return "".getBytes();
        /*try {
            return Hex.decodeHex(input.toCharArray());
        } catch (DecoderException e) {
            throw ExceptionUtils.unchecked(e);
        }*/
    }

    public static String encodeBase64(byte[] input) {
//        return Base64.encodeBase64String(input);
        return null;
    }

    public static String encodeUrlSafeBase64(byte[] input) {
//        return Base64.encodeBase64URLSafeString(input);
        return null;
    }

    public static byte[] decodeBase64(String input) {
//        return Base64.decodeBase64(input);
        return null;
    }

    public static String encodeBase62(byte[] input) {
        char[] chars = new char[input.length];
        for (int i = 0; i < input.length; i++) {
            chars[i] = BASE62[((input[i] & 0xFF) % BASE62.length)];
        }
        return new String(chars);
    }

    public static String escapeHtml(String html) {
//        return StringEscapeUtils.escapeHtml4(html);
        return null;
    }

    public static String unescapeHtml(String htmlEscaped) {
//        return StringEscapeUtils.unescapeHtml4(htmlEscaped);
        return null;
    }

    public static String escapeXml(String xml) {
//        return StringEscapeUtils.escapeXml(xml);
        return null;
    }

    public static String unescapeXml(String xmlEscaped) {
//        return StringEscapeUtils.unescapeXml(xmlEscaped);
        return null;
    }

    public static String urlEncode(String part) {
        try {
            return URLEncoder.encode(part, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw ExceptionUtils.unchecked(e);
        }
    }

    public static String urlDecode(String part) {
        try {
            return URLDecoder.decode(part, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw ExceptionUtils.unchecked(e);
        }
    }
}
