package com.fantaike.tools.ftp;


import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;


/**
 * Ftp 服务类，对Apache的commons.net.ftp进行了包装<br>
 * 依赖库文件：commons-net-1.4.1.jar
 */
public class FtpService {
	/**
	 * FTP 服务器地址
	 */
	private String ftpServerAddress = null;
	/**
	 * FTP 服务端口
	 */
	private int port = 21;
	/**
	 * FTP 用户名
	 */
	private String user = null;
	/**
	 * FTP 密码
	 */
	private String password = null;
	/**
	 * FTP 数据传输超时时间
	 */
	private int timeout = 0;
	/**
	 * 异常：登录失败
	 */


	/**
	 * 初始化FTP连接，并进行用户登录
	 *
	 * @return FTPClient
	 */
	public FTPClient initConnection() {
		FTPClient ftp = new FTPClient();
		try {
			// 连接到FTP
			ftp.connect(ftpServerAddress, port);
			int reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
			}
			// 登录
			if (!ftp.login(user, password)) {
			}
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			// 传输模式使用passive
			ftp.enterLocalPassiveMode();
			// 设置数据传输超时时间
			ftp.setDataTimeout(timeout);
		} catch (IOException ioe) {
		}
		return ftp;
	}

	/**
	 * 设置传输方式
	 *
	 * @param ftp
	 * @param binaryFile true:二进制/false:ASCII
	 */
	public void setTransferMode(FTPClient ftp, boolean binaryFile) {
		try {
			if (binaryFile) {
				ftp.setFileType(FTP.BINARY_FILE_TYPE);
			} else {
				ftp.setFileType(FTP.ASCII_FILE_TYPE);
			}
		} catch (IOException ex) {
		}
	}

	/**
	 * 在当前工作目录下建立多级目录结构
	 *
	 * @param ftp
	 * @param dir
	 * @throws IOException
	 */
	public void makeMultiDirectory(FTPClient ftp, String dir) {
		try {
			StringBuffer fullDirectory = new StringBuffer();
			StringTokenizer toke = new StringTokenizer(dir, "/");
			while (toke.hasMoreElements()) {
				String currentDirectory = (String) toke.nextElement();
				fullDirectory.append(currentDirectory);
				ftp.makeDirectory(fullDirectory.toString());
				if (toke.hasMoreElements()) {
					fullDirectory.append('/');
				}
			}
		} catch (IOException ex) {
		}
	}


	/**
	 * 上传文件到FTP服务器
	 *
	 * @param ftp
	 * @param localFilePathName
	 * @param remoteFilePathName
	 * @throws IOException
	 */
	public void uploadFile(FTPClient ftp, String localFilePathName, String remoteFilePathName) {
		try(InputStream input = new FileInputStream(localFilePathName)){
			boolean result = ftp.storeFile(remoteFilePathName, input);
			if (!result) {
				// 文件上传失败
				System.out.println("文件上传失败");
			}
		}catch (Exception e){

		}
	}


	public void setFtpServerAddress(String ftpServerAddress) {
		this.ftpServerAddress = ftpServerAddress;
	}

	/**
	 * Method setUser.
	 *
	 * @param user String
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Method setPassword.
	 *
	 * @param password String
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Method setTimeout.
	 *
	 * @param timeout String
	 */
	public void setTimeout(String timeout) {
		try {
			this.timeout = Integer.parseInt(timeout);
		} catch (NumberFormatException ex) {
			// 默认超时时间500毫秒
			this.timeout = 500;
		}
	}

	/**
	 * Method setPort.
	 *
	 * @param port String
	 */
	public void setPort(String port) {
		try {
			this.port = Integer.parseInt(port);
		} catch (NumberFormatException ex) {
			// 默认端口21
			this.port = 21;
		}
	}
}
