package com.fantaike.tools.http;

import cn.hutool.log.Log;
import com.fantaike.tools.exception.BusinessException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @ClassName(类名) : HttpUtil
 * @Description(描述) : http请求调用工具类
 * @author(作者) ：曹轩
 * @date (开发日期)      ：2016-6-2 上午9:12:44
 */
public class HttpUtil {

    private static Log logger = Log.get();

    /**
     * @param url   发送接口url
     * @param param json字符串
     * @return 接口返回字符串
     * @description 通用发送json字符串Post请求方法
     */
    @SuppressWarnings("all")
    public static String senJsonPost(String url, String param) throws BusinessException {
        URL sendUrl;
        HttpURLConnection connect;
        //创建具有指定文件和字符集且不带自动刷行新的新 PrintWriter
        PrintWriter writer = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            //取得url对象
            sendUrl = new URL(url);
            //获得打开url链接资源的 URLConnection对象
            connect = (HttpURLConnection) sendUrl.openConnection();
            //设置请求属性
            connect.setRequestProperty("accept", "*/*");
            //设置长连接
            connect.setRequestProperty("connection", "Keep-Alive");
            //设置本次发送的文本格式
            connect.setRequestProperty("Content-Type", "application/json");
            //设置为POST
            connect.setRequestMethod("POST");
            //允许写入
            connect.setDoOutput(true);
            //允许输出
            connect.setDoInput(true);
            //设置连接超时，读取超时
            connect.setConnectTimeout(1000 * 60);
            connect.setReadTimeout(1000 * 60);
            //不使用缓存
            connect.setUseCaches(false);
            //设置本次链接可以重定向
            connect.setInstanceFollowRedirects(true);
            //打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）
            connect.connect();

            //创建输出流 通过UTF-8编码之后的connect的输出流
            writer = new PrintWriter(new OutputStreamWriter(connect.getOutputStream(), "UTF-8"));
            writer.write(param);
            //强制把缓冲区的数据写入到文件并清空缓冲区
            writer.flush();

            //定义缓冲区输入流获得url的相应信息 从URL连接中用输入流获取
            in = new BufferedReader(new InputStreamReader(connect.getInputStream(), "UTF-8"));
            String line;
            //从缓冲输入流中读取数据
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            logger.error("post请求出现错误！", e);
            throw BusinessException.getException("发送POST请求出现异常！");
        }
        //判断 输出流和缓冲输入流是否关闭,未关闭的话关闭
        finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("post请求出现错误！", e);
                throw BusinessException.getException("发送POST请求出现异常！");
            }
        }
        return result.toString();
    }

    /**
     * Get请求
     * 传递的参数是拼接的路径
     *
     * @param url
     * @param param
     * @return
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        URL realUrl = null;
        try {
            if (!"".equals(param)) {
                String urlNameString = url + "?" + param;
                realUrl = new URL(urlNameString);
            } else {
                realUrl = new URL(url);
            }
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //设置连接超时，读取超时
            connection.setConnectTimeout(1000 * 60);
            connection.setReadTimeout(1000 * 60);
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            logger.error("异常：", e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Delete 请求,备注：适用于拼接路径
     *
     * @param url
     * @param param
     * @return
     */
    public static String delete(String url, String someParam) {
        String jsonStr = null;
        HttpDelete httpDelete = new HttpDelete(url + "/" + someParam);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            CloseableHttpResponse response = httpClient.execute(httpDelete);
            HttpEntity entity = response.getEntity();
            jsonStr = EntityUtils.toString(entity, "UTF-8");
            response.close();
            httpClient.close();
        } catch (Exception e) {
            logger.error("delete请求出现错误！", e);
        }
        return jsonStr;
    }

    public static String senPost(String url, String param) throws BusinessException {
        PrintWriter writer = null;
        URL send_url;
        BufferedReader in = null;
        String result = "";
        try {
            send_url = new URL(url);
            //打开连接
            URLConnection connect = send_url.openConnection();
            //设置请求属性
            connect.setRequestProperty("accept", "*/*");
            connect.setRequestProperty("connection", "Keep-Alive");
            // 发送POST请求必须设置如下两行
            connect.setDoOutput(true);
            connect.setDoInput(true);
            //设置连接超时，读取超时
            connect.setConnectTimeout(1000 * 60);
            connect.setReadTimeout(1000 * 60);
            //创建输出流(UTF-8)
            writer = new PrintWriter(new OutputStreamWriter(connect.getOutputStream(), StandardCharsets.UTF_8));
            writer.print(param);
            writer.flush();
            //定义BufferedReader获取Url响应信息
            in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("post请求出现错误！", e);
            throw BusinessException.getException("发送POST请求出现异常！");
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("post请求出现错误！", e);
            }
        }
        return result;
    }

    public static String sendPost(String url, String param) throws BusinessException {
        PrintWriter writer = null;
        URL send_url;
        BufferedReader in = null;
        String result = "";
        try {
            send_url = new URL(url);
            //打开连接
            URLConnection connect = send_url.openConnection();
            //设置请求属性
            connect.setRequestProperty("accept", "*/*");
            connect.setRequestProperty("connection", "Keep-Alive");
            connect.setRequestProperty("content-type", "text/html");
            // 发送POST请求必须设置如下两行
            connect.setDoOutput(true);
            connect.setDoInput(true);
            //设置连接超时，读取超时
            connect.setConnectTimeout(1000 * 60);
            connect.setReadTimeout(1000 * 60);
            //创建输出流(UTF-8)
            writer = new PrintWriter(new OutputStreamWriter(connect.getOutputStream(), StandardCharsets.UTF_8));
            writer.print(param);
            writer.flush();
            //定义BufferedReader获取Url响应信息
            in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("post请求出现错误！", e);
            throw BusinessException.getException("发送POST请求出现异常！");
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("post请求出现错误！", e);
            }
        }
        return result;
    }


    public static String getBodyMessage(HttpServletRequest request) {
        BufferedReader reader = null;
        String line = "";
        String xmlString = null;
        try {
            reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));

            StringBuffer inputString = new StringBuffer();

            while ((line = reader.readLine()) != null) {
                inputString.append(line);
            }
            xmlString = inputString.toString();
            reader.close();

        } catch (IOException e) {

        }
        return xmlString;
    }

    public static String httpURLConnection(String url, String param) throws BusinessException {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(1000 * 60);
            conn.setReadTimeout(1000 * 60);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8));
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            logger.error("异常：", e);
            throw BusinessException.getException("发送 POST 请求出现异常！" + e);
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
