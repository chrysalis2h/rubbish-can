package com.fantaike.tools.http;

import javax.net.ssl.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.security.*;

/**
 * https通讯类
 */
public class HttpClient {

    /**
     * 证书库路径和密码
     * clientKeyStoreFile：私钥库路径
     * clientKeyStorePwd：私钥库存储密码
     * clientKeyPwd：私钥密码
     * clientTrustKeyStoreFile：公钥库路径
     * clientTrustKeyStorePwd：公钥库存储密码
     */
    private  String clientKeyStoreFile;
    private  String clientKeyStorePwd;
    private  String clientKeyPwd;
    private  String clientTrustKeyStoreFile;
    private  String clientTrustKeyStorePwd;

    /**
     * builder构造器模式内部类
     */
    public static class Builder {
        private String clientKeyStoreFile;
        private String clientKeyStorePwd;
        private String clientKeyPwd;
        private String clientTrustKeyStoreFile;
        private String clientTrustKeyStorePwd;

        public Builder() {
        }

        public Builder clientKeyStoreFile(String clientKeyStoreFile) {
            this.clientKeyStoreFile = clientKeyStoreFile;
            return this;
        }

        public Builder clientKeyStorePwd(String clientKeyStorePwd) {
            this.clientKeyStorePwd = clientKeyStorePwd;
            return this;
        }

        public Builder clientKeyPwd(String clientKeyPwd) {
            this.clientKeyPwd = clientKeyPwd;
            return this;
        }

        public Builder clientTrustKeyStoreFile(String clientTrustKeyStoreFile) {
            this.clientTrustKeyStoreFile = clientTrustKeyStoreFile;
            return this;
        }

        public Builder clientTrustKeyStorePwd(String clientTrustKeyStorePwd) {
            this.clientTrustKeyStorePwd = clientTrustKeyStorePwd;
            return this;
        }

        public HttpClient build(){
            return new HttpClient(this);
        }
    }

    /**
     * builder模式构造器
     * @param builder
     */
    private HttpClient(Builder builder){
        clientKeyStoreFile = builder.clientKeyStoreFile;
        clientKeyPwd = builder.clientKeyPwd;
        clientKeyStorePwd = builder.clientKeyStorePwd;
        clientTrustKeyStoreFile = builder.clientTrustKeyStoreFile;
        clientTrustKeyStorePwd = builder.clientTrustKeyStorePwd;
    }

    /**
     * 输出
     * @param in
     * @param out
     * @throws IOException
     */
    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[512];
        int n = -1;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        in.close();
        out.close();
    }

    /**
     * 中原银行证书加载
     *
     * @return
     */
    public  SSLSocketFactory getSslSocketFactoryForzhuanyBank() throws Exception{

        try {
            KeyStore clientKeyStore = KeyStore.getInstance("JKS");
            clientKeyStore.load(new FileInputStream(clientKeyStoreFile), clientKeyStorePwd.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory
                    .getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, clientKeyPwd.toCharArray());

            KeyStore clientTrustKeyStore = KeyStore.getInstance("JKS");
            clientTrustKeyStore.load(new FileInputStream(clientTrustKeyStoreFile),
                    clientTrustKeyStorePwd.toCharArray());
            TrustManagerFactory trustManagerFactory = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(clientTrustKeyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 带证书得https请求
     *
     * @param requrl
     * @param content
     * @param sslSocketFactory
     * @return
     */
    public static String sendPOST(String requrl, String content, SSLSocketFactory sslSocketFactory) throws Exception {

        try {
            byte[] input = content.getBytes();
            URL url = new URI(requrl).toURL();
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            // 如果用http注释掉这一行
            connection.setSSLSocketFactory(sslSocketFactory);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            });
            connection.setDoOutput(input != null);
            if (input != null) {
                OutputStream out = connection.getOutputStream();
                out.write(input);
                out.flush();
                out.close();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            copy(in, baos);
            System.out.println("status:" + connection.getResponseCode());
            System.out.println("data:" + baos.toString());
            return new String(baos.toByteArray(), "UTF-8");//编码后的报文
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
