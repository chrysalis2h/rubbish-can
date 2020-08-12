package com.fantaike.tools.crypto;

import cn.hutool.log.Log;
import com.fantaike.tools.utils.DataFormater;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 * 功能描述：
 * 2014年1月10日
 * 推荐直接使用 MD5
 * @author 郑翔
 */
@Deprecated
public class MD5Utils {
	private static final Log log = Log.get();
	protected static MessageDigest messagedigest = null;

	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsaex) {
			log.error("初始化失败，MessageDigest不支持MD5Util，原因是：" + nsaex.getMessage(), nsaex);
		}
	}

	/**
	 * 适用于上G大的文件
	 *
	 * @param file
	 * @return 加密的密文
	 * @throws IOException
	 */
	public static String encrypt(File file) throws IOException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			FileChannel ch = in.getChannel();
			MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			messagedigest.update(byteBuffer);
		} finally {
			in.close();
		}
		return DataFormater.byte2hex(messagedigest.digest());
	}

	/**
	 * MD5加密
	 *
	 * @param s 明文
	 * @return 密文
	 */
	public static String encrypt(String s) {
		String ret = "";
		ret = encrypt(s.getBytes(StandardCharsets.UTF_8));
		return ret;
	}

	/**
	 * MD5加密
	 *
	 * @param bytes 明文
	 * @return 密文
	 */
	public static String encrypt(byte[] bytes) {
		messagedigest.update(bytes);
		return DataFormater.byte2hex(messagedigest.digest());
	}

	/**
	 * 密码校验
	 *
	 * @param password  明文密码
	 * @param md5PwdStr 密文
	 * @return 相等：true，不相等：false
	 */
	public static boolean checkPassword(String password, String md5PwdStr) {
		String s = encrypt(password);
		return s.equals(md5PwdStr);
	}
}
