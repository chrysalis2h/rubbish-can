/**
 * 软件著作权：长安新生（深圳）金融投资有限公司
 * 系统名称：马达贷
 */
package com.fantaike.tools.ftp;

import cn.hutool.log.Log;
import com.fantaike.tools.utils.ApolloUtil;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

/**
 * Created by wangqi on 2016/4/26.
 */
public class FtpFiles {
    @SuppressWarnings("unused")
    private static final Log logger = Log.get();

    public static int uploadfile(String localPath, String remotePath) {
        FtpService ftp = new FtpService();
        //读取配置文件
        ftp.setFtpServerAddress(ApolloUtil.getConfig("param", "ycmsFtp.ip"));
        ftp.setPort(ApolloUtil.getConfig("param", "ycmsFtp.port"));
        ftp.setUser(ApolloUtil.getConfig("param", "ycmsFtp.user"));
        ftp.setPassword(ApolloUtil.getConfig("param", "ycmsFtp.pwd"));
        FTPClient fc = ftp.initConnection();
        try {
            fc.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.uploadFile(fc, localPath, remotePath);
        } catch (IOException e) {
            logger.error("异常：", e);
        } catch (Exception e) {
            logger.error("异常：", e);
        }
        return 1;
    }
}
