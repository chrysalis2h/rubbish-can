package com.fantaike.tools.download;

/**
 * Created by kevin on 2016/8/24.
 */

import cn.hutool.log.Log;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownLoadPicUtil {
    public final static String REQ_TYPE_POST = "POST";
    public final static String REQ_TYPE_DELETE = "DELETE";
    public final static String REQ_TYPE_GET = "GET";
    private static final Log logger = Log.get();

    /**
     * @description:将输入流存在本地
     * @author: liyanzhao
     * @param: [localFile, picUrl]
     * @return: void
     * @date: 2016/11/25 16:29
     */
    public static void saveImageToDisk(String localFile, String picUrl) {

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;

        int len = 0;
        try {
            URL url = new URL(picUrl);
            if (url != null) {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                // 设置连接网络的超时时间
                httpURLConnection.setConnectTimeout(3000);
                // 打开输入流
                httpURLConnection.setDoInput(true);
                // 使用GET方法请求
                httpURLConnection.setRequestMethod("GET");
                // 获取结果响应码
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == 200) {
                    inputStream = httpURLConnection.getInputStream();
                }
            }
            byte[] data = new byte[1024];
            fileOutputStream = new FileOutputStream(localFile);
            while ((len = inputStream.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }
        } catch (Exception e) {
            logger.error("saveImageToDisk 保存照片错误：path:" + localFile);
            logger.error("异常：", e);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    logger.error("异常：", e);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("异常：", e);
                }
            }

            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    /**
     * @description: 从本地获取照片的byte值
     * @author: liyanzhao
     * @param: [localFile] 照片的地址
     * @return: byte[]
     * @date: 2016/11/25 16:26
     */
    public static byte[] getPicBinaryData(String localFile) {

        byte[] data = null;
        InputStream in = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(localFile);//"D:\\sfz.jpg"
            data = new byte[in.available()];
            in.read(data);
        } catch (IOException e) {
            logger.error("异常：", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("异常：", e);
                }
            }
        }
        return data;
    }

    /**
     * @description: 删除文件夹
     * @author: liyanzhao
     * @param: [path] 删除路径
     * @return: void
     * @date: 2016/11/24 19:15
     */
    public static void recurDelete(File path) {

        if (!path.exists()) {
            return;
        }
        if (path.isFile()) {
            path.delete();
            return;
        }
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            recurDelete(files[i]);
        }
        path.delete();
    }

    /**
     * @description: 通过 GET 请求调用 url 获取结果
     * @author: liyanzhao
     * @param: [inUrl 请求地址, params 请求参数]
     * @return: java.lang.String
     * @date: 2016/11/25 16:27
     */
    public static String readByGet(String inUrl, String situToken, String params) {

        StringBuffer sbf = new StringBuffer();
        String strRead = null;

        HttpURLConnection connection = null;
        OutputStreamWriter writer = null;
        InputStream is = null;
        BufferedReader reader = null;
        // 模拟浏览器
        // 连接 URL 地址
        try {
            URL url = new URL(inUrl);
            // 根据拼凑的 URL ，打开连接， URL.openConnection 函数会根据 URL 的类型 ,
            // 返回不同的 URLConnection 子类的对象，这里 URL 是一个 http ，因此实际返 回的是
            // HttpURLConnection
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接访问方法及超时参数 connection.setRequestMethod("GET");
            connection.setRequestMethod("POST");// 提交模式
            //是否允许输入输出
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("X-Auth-Token", situToken);

            // 函数中才会真正发到服务器
            connection.connect();

            writer = new OutputStreamWriter(connection.getOutputStream());
            //发送参数
            writer.write(params);
            //清理当前编辑器的左右缓冲区，并使缓冲区数据写入基础流
            writer.flush();

            // 取得输入流，并使用 Reader 读取
            is = connection.getInputStream();
            // 读取数据编 码处理
            reader = new BufferedReader(new InputStreamReader(is,
                    StandardCharsets.UTF_8));
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }

        } catch (IOException e) {
            logger.error("异常：", e);
        } finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("异常：", e);
                }
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("异常：", e);
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.error("异常：", e);
                }
            }

            // 断开连接
            if (connection != null) {
                connection.disconnect();
            }

        }

        return sbf.toString();
    }

    public static String readByGetJson(String inUrl, String token, String params) {
        return readByGetJson(inUrl, token, params, "", "");
    }

    public static String readByGetJson(String inUrl, String params, String accessId, String reqType) {
        return readByGetJson(inUrl, "", params, accessId, reqType);
    }

    public static String readByGetJson(String inUrl, String token, String params, String accessId, String reqType) {

        StringBuffer sbf = new StringBuffer();
        String strRead = null;

        HttpURLConnection connection = null;
        OutputStreamWriter writer = null;
        InputStream is = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(inUrl);
            connection = (HttpURLConnection) url.openConnection();
            if (REQ_TYPE_DELETE.equals(reqType)) {
                connection.setRequestMethod(REQ_TYPE_DELETE);
            } else if (REQ_TYPE_GET.equals(reqType)) {
                connection.setRequestMethod(REQ_TYPE_GET);
            } else {
                connection.setRequestMethod(REQ_TYPE_POST);
            }
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            if (!"".equals(token)) {
                connection.setRequestProperty("X-Auth-Token", token);
            }
            if (!"".equals(accessId)) {
                connection.setRequestProperty("x-access-id", accessId);
            }
            connection.connect();
            writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
            writer.write(params);
            writer.flush();
            is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            params = null;
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("异常：", e);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("异常：", e);
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.error("异常：", e);
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return sbf.toString();
    }

    /**
     * @description: 图片转换成base64
     * @author: liyanzhao
     * @param: [data] 照片的byte值
     * @return: java.lang.String
     * @date: 2016/11/25 16:28
     */
    public static String imageToBase64(byte[] data) {

        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

    /**
     * @description: 替换所有换行
     * @author: liyanzhao
     * @param: [str]
     * @return: java.lang.String
     * @date: 2016/11/25 16:28
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
            dest = dest.replaceAll("\\+", "%2B");
        }
        return dest;
    }

    /**
     * @description: 替换所有换行
     * @author: liyanzhao
     * @param: [str]
     * @return: java.lang.String
     * @date: 2016/11/25 16:28
     */
    public static String replaceBlankJson(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n|");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * @description: 创建目录
     * @author: liyanzhao
     * @param: [dir]
     * @return: void
     * @date: 2016/11/29 19:37
     */
    public static String mkdirs(String basedir, String dir) {
        String path = basedir + "ZhxtFileUpLoad/" + dir;
        File d = new File(path);
        int index = 1;
        while (d.exists()) {
            path = basedir + "ZhxtFileUpLoad/" + dir + "other" + (index++);
            d = new File(path);
        }
        if (!d.exists() && !d.isDirectory()) {
            d.mkdirs();
        }
        return path;
    }


    /**
     * @description: 获取照片的流
     * @author: liyanzhao
     * @param: [url]
     * @return: java.io.InputStream
     * @date: 2017/2/17 14:35
     */
    public static byte[] getByte(URL url) throws Exception {
        BufferedImage bi = ImageIO.read(url);
        return getByte(bi);
    }

    public static byte[] getByte(BufferedImage bi) throws Exception {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageOutputStream imOut = null;
        try {
            imOut = ImageIO.createImageOutputStream(bs);
            ImageIO.write(bi, "jpg", imOut);
        } catch (IOException e) {
            logger.error("异常：", e);
        } finally {
            try {
                if (imOut != null) {
                    imOut.close();
                    imOut = null;
                }
            } catch (Exception e) {
                logger.error("异常：", e);
            }
            try {
                if (bs != null) {
                    bs.close();
                }
            } catch (Exception e) {
                logger.error("异常：", e);
            }
            try {
                if (bi != null) {
                    bi.flush();
                    bi = null;
                }
            } catch (Exception e) {
                logger.error("异常：", e);
            }
        }
        return bs.toByteArray();
    }

    /**
     * @description: 获取照片的流
     * @author: liyanzhao
     * @param: [url]
     * @return: java.io.InputStream
     * @date: 2017/2/17 14:35
     */
    public static InputStream getBi(URL url) throws Exception {
        BufferedImage bi = ImageIO.read(url);
        return getBi(bi);
    }

    public static String getBase64(URL url) throws Exception {
        byte[] bytes = getByte(url);
        return imageToBase64(bytes);
    }

    public static String getBase64(BufferedImage bi) throws Exception {
        byte[] bytes = getByte(bi);
        return imageToBase64(bytes);
    }

    /**
     * @description: 获取照片的流
     * @author: liyanzhao
     * @param: [url]
     * @return: java.io.InputStream
     * @date: 2017/2/17 14:35
     */
    public static InputStream getBi(BufferedImage bi) throws Exception {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageOutputStream imOut = null;
        ByteArrayInputStream inputStream = null;
        try {
            imOut = ImageIO.createImageOutputStream(bs);
            ImageIO.write(bi, "jpg", imOut);
            inputStream = new ByteArrayInputStream(bs.toByteArray());
        } catch (IOException e) {
            logger.error("异常：", e);
        } finally {
            try {
                if (imOut != null) {
                    imOut.close();
                }
            } catch (Exception e) {
                logger.error("异常：", e);
            }
            try {
                if (bs != null) {
                    bs.close();
                }
            } catch (Exception e) {
                logger.error("异常：", e);
            }
        }
        return inputStream;
    }

    public static byte[] readFileByBytes(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
            BufferedInputStream in = null;

            try {
                in = new BufferedInputStream(new FileInputStream(file));
                short bufSize = 1024;
                byte[] buffer = new byte[bufSize];
                boolean len = false;
                int len1;
                while (-1 != (len1 = in.read(buffer, 0, bufSize))) {
                    bos.write(buffer, 0, len1);
                }

                byte[] var7 = bos.toByteArray();
                return var7;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException var14) {
                    var14.printStackTrace();
                }
                try {
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException var14) {
                    var14.printStackTrace();
                }

            }
        }
    }

    public static String reduceImg(URL url, int widthdist, int heightdist) {
        try {
            Image src = ImageIO.read(url);
            return reduceImg(src, widthdist, heightdist);
        } catch (Exception e) {
            logger.error("异常：", e);
            return null;
        }
    }

    public static BufferedImage reduceImg2Buff(Image src, int widthdist, int heightdist) {
        try {
            int sW = ((BufferedImage) src).getWidth();
            int sH = ((BufferedImage) src).getHeight();
            int tmp = heightdist;
            if (sW > sH) {
                heightdist = widthdist;
                widthdist = tmp;
            }
            BufferedImage tag = new BufferedImage(widthdist, heightdist, BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist, Image.SCALE_SMOOTH), 0, 0, null);
            return tag;
        } catch (Exception e) {
            logger.error("异常：", e);
            return null;
        }
    }

    public static String reduceImg(Image src, int widthdist, int heightdist) {
        try {
            BufferedImage tag = reduceImg2Buff(src, widthdist, heightdist);
            return replaceBlankJson(getBase64(tag));
        } catch (Exception e) {
            logger.error("异常：", e);
            return null;
        }
    }

    public static String video2Base64(URL url) {
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            byte[] bytes = readStreamAsByteArray(inputStream);
            String imageBase64String = DownLoadPicUtil.replaceBlankJson(DownLoadPicUtil.imageToBase64(bytes));
            bytes = null;
            return imageBase64String;
        } catch (Exception e) {
            logger.error("异常：", e);
            logger.info("未找到对应视频文件");
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                logger.error("异常：", e);
            }

        }
    }

    public static byte[] readStreamAsByteArray(InputStream in) {
        if (in == null) {
            return new byte[0];
        } else {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len1;
            try {
                while ((len1 = in.read(buffer)) != -1) {
                    output.write(buffer, 0, len1);
                }
            } catch (Exception e) {
                logger.error("异常：", e);
            } finally {
                try {
                    if (output != null) {
                        // // TODO: 2017/11/14  什么情况下fulash 什么情况下close
                        output.flush();
                        output.close();
                    }
                } catch (Exception e) {
                    logger.error("异常：", e);
                }
                buffer = null;
            }
            return output.toByteArray();
        }
    }


    public static String readTxtByUrl(URL url) {
        InputStream inputStream = null;
        InputStreamReader bre = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = url.openStream();
            bre = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(bre);
            String str = "";
            String target = "";
            while ((str = bufferedReader.readLine()) != null) {
                target = target + str;
            }
            return target;
        } catch (Exception e) {
            logger.error("异常：", e);
            logger.info("下载文件异常");
            return null;
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e) {
                logger.error("异常：", e);
            }
            try {
                if (bre != null) {
                    bre.close();
                }
            } catch (Exception e) {
                logger.error("异常：", e);
            }

            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                logger.error("异常：", e);
            }

        }
    }

    public static void saveBase64ToImg(String base64, String filePath) {
        OutputStream os = null;
        try {
            base64 = base64.replaceAll("data:(.*);base64,", "");
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(base64);
            os = new FileOutputStream(filePath);
            os.write(b);
            os.flush();
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {
                logger.error("异常：", e);
            }
        }
    }

    public static String readTxt(String fileName) throws IOException {
        try (InputStreamReader bre = new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(bre)) {
            String str = "";
            String target = "";
            while ((str = bufferedReader.readLine()) != null) {
                target = target + str;
            }
            bre.close();
            bufferedReader.close();
            return target;
        }
    }
}

