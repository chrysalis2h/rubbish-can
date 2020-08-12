package com.fantaike.tools.third;

import javax.xml.bind.DatatypeConverter;
import java.io.FileNotFoundException;
import java.security.*;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 * <p>Description: 滴滴提供RSA工具类
 * <p>Version:v1.0
 * <p>Copyright ©2015 madadai.com All Rights Reserved. </p>
 * <p>Company:caxins</p>
 * <p>Author:liyanzhao
 * <p>Date: 16:29 2017/9/13
 */
public class DidiRsaUtils {

    public static final String[] SignTypes = {"RSA", "DSA", "MD5"};
    String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAObxF1QCFFYv1ihGK9hCBKwJWxPFrhRDKy9d4m82fF2NcpM/5lqXT9+xFjvXiN4aUfuYcwqoisgrlo6NscWc/sITYW+nv1qW3HjiCznlXC5lnVXs49jC/9WFl3yqJpBR2eUfU+iu2nPAFsN4MwKyRKCWesKuiCuFnlyuShACmfJ9AgMBAAECgYBEyQppuzTTduwSktP8Va2OkDrQaNCwuFDkitImzdO9CaRWt3GlCeyAc/5rVIcvoCdLCN07D3hGprTxQMLyWRCHrLZQDY7UaPA59NpWOWQEakfKUo9Ech4ujCV70Tk1a/RBwic2nvr0FLksjIDlsEbFUxol/5RufWaMi8lGyqqPAQJBAP/LS2NYFq3yYe3WfeQ5Vzd0KPaRsMuMJTCEeQP7VFqKslDcStmNTZSwm3idvPKu7zE5XBWWV6mtX0dLicRe/wMCQQDnIK0HkMl5OIfHdGEYtpgKv5FWgolP9+B1i8Z/s/XQJyZXlRaeGAU6wMnq18MBT1MMtij85k/ZJbzzt8acbdB/AkANRjaGVEbxBfgRCKWF13fwN8X8bbkYBxDTALzq+Pr6q+HvEk+MFKonAjc/PihVC8D78/cUY7Xu50FfiMsAdtGTAkBnJB/TJnuiSuXczkSeQK2s7jwvfOVF+HoEOrIEPwzNLQthPjjvqAx9YZc547s0FHAdVhUIdsbfQoYFxNcu65qFAkByF+uNl8elkLcX42Hva0ioF0vXOMowfVC7zKShYf1SvGF82KCS9Vu0Tr490JFvPl3RPwppT1ODl0aSAyDniT4u";

    public static String genSign(String signType, PrivateKey privateKey, byte[] data)
            throws Exception {
        if (!(isValidSignType(signType))) {
            return null;
        }

        Signature signature = Signature.getInstance("SHA1With" + signType);
        signature.initSign(privateKey);
        signature.update(data);
        return DatatypeConverter.printBase64Binary(signature.sign());
    }

    public static boolean verifySign(String signType, PublicKey publicKey, byte[] data, String signStr) {
        if (!(isValidSignType(signType)))
            return false;

        try {
            Signature signature = Signature.getInstance("SHA1With" + signType);
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(DatatypeConverter.parseBase64Binary(signStr));
        } catch (InvalidKeyException e) {
            return false;
        } catch (SignatureException e) {
            return false;
        } catch (Exception e) {
        }
        return false;
    }

       /* public static PrivateKey getPrivateKeyFromFile(String signType, String signFile)
                throws IOException
        {
            String keyStr = null;
            try {
                keyStr = loadKeyFile(signFile);
            } catch (Exception e) {
                return null;
            }
            return getPrivateKeyFromString(signType, keyStr);
        }*/

    public static boolean isValidSignType(String signType) {
        return Arrays.asList(SignTypes).contains(signType);
    }

       /* public static PublicKey getPublicKeyFromFile(String signType, String signFile)
                throws IOException
        {
            String keyStr = null;
            try {
                keyStr = loadKeyFile(signFile);
            } catch (Exception e) {
                return null;
            }
            return getPublicKeyFromString(signType, keyStr);
        }*/

    public static PrivateKey getPrivateKeyFromString(String signType, String keyStr) {
        byte[] keyBytes;
        try {
            keyBytes = DatatypeConverter.parseBase64Binary(keyStr);
            KeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(signType);
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
        }
        return null;
    }

    public static PublicKey getPublicKeyFromString(String signType, String keyStr) {
        try {
            byte[] keyBytes = DatatypeConverter.parseBase64Binary(keyStr);
            KeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(signType);
            return keyFactory.generatePublic(keySpec);
        } catch (Exception keyBytes) {
        }
        return null;
    }

    private static String loadKeyFile(String fileName) throws FileNotFoundException {

        return "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAObxF1QCFFYv1ihGK9hCBKwJWxPFrhRDKy9d4m82fF2NcpM/5lqXT9+xFjvXiN4aUfuYcwqoisgrlo6NscWc/sITYW+nv1qW3HjiCznlXC5lnVXs49jC/9WFl3yqJpBR2eUfU+iu2nPAFsN4MwKyRKCWesKuiCuFnlyuShACmfJ9AgMBAAECgYBEyQppuzTTduwSktP8Va2OkDrQaNCwuFDkitImzdO9CaRWt3GlCeyAc/5rVIcvoCdLCN07D3hGprTxQMLyWRCHrLZQDY7UaPA59NpWOWQEakfKUo9Ech4ujCV70Tk1a/RBwic2nvr0FLksjIDlsEbFUxol/5RufWaMi8lGyqqPAQJBAP/LS2NYFq3yYe3WfeQ5Vzd0KPaRsMuMJTCEeQP7VFqKslDcStmNTZSwm3idvPKu7zE5XBWWV6mtX0dLicRe/wMCQQDnIK0HkMl5OIfHdGEYtpgKv5FWgolP9+B1i8Z/s/XQJyZXlRaeGAU6wMnq18MBT1MMtij85k/ZJbzzt8acbdB/AkANRjaGVEbxBfgRCKWF13fwN8X8bbkYBxDTALzq+Pr6q+HvEk+MFKonAjc/PihVC8D78/cUY7Xu50FfiMsAdtGTAkBnJB/TJnuiSuXczkSeQK2s7jwvfOVF+HoEOrIEPwzNLQthPjjvqAx9YZc547s0FHAdVhUIdsbfQoYFxNcu65qFAkByF+uNl8elkLcX42Hva0ioF0vXOMowfVC7zKShYf1SvGF82KCS9Vu0Tr490JFvPl3RPwppT1ODl0aSAyDniT4u";
    }


}
