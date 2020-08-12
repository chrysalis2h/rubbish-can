package com.fantaike.tools.file;

import cn.hutool.log.Log;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 此类中封装一些常用的文件操作。
 * 所有方法都是静态方法，不需要生成此类的实例，
 * 为避免生成此类的实例，构造方法被申明为private类型的。
 *
 * @author Luxin.xiao
 * @date 2018/8/27 14:35
 */

public class FileUtil {
    public final static String BUSIMG_FILENAME_SEPARATOR = "|";
    private static final Log logger = Log.get();
    private static File file;
    private static boolean flag;

    /**
     * 私有构造方法，防止类的实例化，因为工具类不需要实例化。
     */
    private FileUtil() {

    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     * 对外
     *
     * @param sPath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static boolean deleteFolder(String sPath) throws Exception {
        flag = false;
        file = new File(sPath);
        // 判断目录或文件是否存在,不存在返回 false
        if (!file.exists()) {
            return flag;
        } else {
            // 判断是否为文件,为文件时调用删除文件方法
            if (file.isFile()) {
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除单个文件
     * 对内
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private static boolean deleteFile(String sPath) throws Exception {
        flag = false;
        file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     * 对内
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) throws Exception {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            return false;
        }
        //删除当前目录
        return dirFile.delete();
    }

    /**
     * @description: 获得当前目录下，筛选的文件名称
     * @param: [path, isFileFlag, targetType] 文件路径，是否是文件，筛选的文件类型
     * @return: java.util.List<java.lang.String>
     * @author: Qu.ZeHu
     * @date: 2018/8/31 13:14
     */
    public static List<String> getDirectoryOrFile(String path, boolean isFileFlag, String targetType) throws Exception {
        // 获得指定文件对象
        File file = new File(path);
        // 定义返回的文件名称数组
        List<String> fileArr = new ArrayList<>();
        if (!isFileFlag && !StringUtils.isEmpty(targetType)) {
            logger.info("传入的参数异常");
            throw new Exception();
        }
        if (!StringUtils.isEmpty(targetType)) {
            // 获得该文件夹内的所有文件
            File[] array = file.listFiles();
            for (int i = 0; i < array.length; i++) {
                if (targetType.indexOf("|") != -1) {
                    String[] targetTypes = targetType.split("|");
                    for (String type : targetTypes) {
                        doAddFileName(array[i], isFileFlag, type, fileArr);
                    }
                } else {
                    doAddFileName(array[i], isFileFlag, targetType, fileArr);
                }
            }
        } else {
            doAddFileName(path, isFileFlag, fileArr);
        }
        return fileArr;
    }

    /**
     * @description: 获得当前目录下的子文件夹名称
     * @param: [path, isFileFlag] 输入的目录路径, 是文件（true），是目录（false）
     * @return: java.lang.String 返回的所有子文件夹名称字符串数组
     * @author: Qu.ZeHu
     * @date: 2018/8/29 13:44
     */
    private static void doAddFileName(String path, boolean isFileFlag, List<String> fileArr) throws Exception {
        // 获得指定文件对象
        File file = new File(path);
        // 获得该文件夹内的所有文件
        File[] array = file.listFiles();
        for (int i = 0; i < array.length; i++) {
            if (isFileFlag) {
                // 如果是文件
                if (array[i].isFile()) {
                    // 输出文件名字
                    logger.info("文件名称：" + array[i].getName());
                    fileArr.add(array[i].getName());
                }
            } else {
                //如果是目录
                if (array[i].isDirectory()) {
                    // 输出文件名字
                    logger.info("目录名称：" + array[i].getName());
                    fileArr.add(array[i].getName());
                }
            }
        }
    }

    private static void doAddFileName(File file, boolean isFileFlag, String targetType, List<String> fileArr) {
        String fileName = file.getName();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (isFileFlag) {
            // 如果是文件
            if (file.isFile() && fileType.equals(targetType)) {
                // 输出文件名字
                logger.info("文件名称：" + file.getName());
                fileArr.add(file.getName());
            }
        } else {
            //如果是目录
            if (file.isDirectory()) {
                // 输出文件名字
                logger.info("目录名称：" + file.getName());
                fileArr.add(file.getName());
            }
        }
    }

    /**
     * @description: 获得某一目录下所有的文件的文件名，以“|”分割
     * @param: [path]
     * @return: java.lang.String
     * @author: Qu.ZeHu
     * @date: 2018/8/29 16:43
     */

    public static String getFileNameStr(String path) throws Exception {
        // 获得指定文件对象
        File file = new File(path);
        // 获得该文件夹内的所有文件
        File[] array = file.listFiles();
        StringBuffer fileName = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            //如果是文件
            if (array[i].isFile()) {
                // 只输出文件名字
                logger.info("文件名称：" + array[i].getName());
                if (i + 1 == array.length) {
                    fileName.append(array[i].getName());
                } else {
                    fileName.append(array[i].getName()).append(FileUtil.BUSIMG_FILENAME_SEPARATOR);
                }
            }
        }
        return fileName.toString();
    }

    /**
     * 读取文件
     *
     * @param filePath
     * @param fileName
     * @param type     类型：file文件/.check文件
     * @return
     */
    public static String readFile(String filePath, String fileName, String type) throws Exception {
        String filePathAndName = filePath + "/" + fileName;
        InputStreamReader bre = null;
        BufferedReader bufferedReader = null;
        String strs;
        StringBuffer data = new StringBuffer();
        int line = 0;
        try {
            bre = new InputStreamReader(new FileInputStream(filePathAndName), StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(bre);
            // 批扣文件从第二行开始获取数据，循环读取下一行
            if (type.equals("file")) {
                while ((strs = bufferedReader.readLine()) != null) {
                    line++;
                    if (line == 1) {
                        continue;
                    }
                    //每行用分号分割
                    data.append(strs).append(";");
                }
            } else {
                while ((strs = bufferedReader.readLine()) != null) {
                    //每行用分号分割
                    data.append(strs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("微众银行，读取文件失败！");
        } finally {
            try {
                if (bre != null) {
                    bre.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return data.toString();
    }

    /**
     * @Description: 某个文件夹下的数量
     * @Author: chengjiali
     * @Date: 16:23 2019/8/6 0006
     */

    public static int getFileNum(String filePath) {
        File f = new File(filePath);
        File[] fl = null;
        int iNum = 0;
        //判断是不是目录
        if (f.isDirectory()) {
            fl = f.listFiles();
            for (int i = 0; i < fl.length; i++) {
                File f2 = fl[i];
                if (f2.isFile()) {
                    iNum = iNum + 1;
                }
            }

            System.out.println("The num of file is " + iNum);
        } else {
            System.out.println(filePath + " is file.");
        }
        return iNum;
    }

    /**
     * @param filePathAndName 文件全路径 example：/ynht/out/loan/2019/07/11/loan20190716.txt
     * @Author: xulijie
     * @Description: 读取文件内容返回List
     * @Return: java.util.List<java.lang.String>
     * @Exception:
     * @Date: 2019/7/18 14:59
     */
    public static List<String> readFileFromOne(String filePathAndName) throws Exception {
        List<String> list = new ArrayList<>();
        String str;
        InputStreamReader bre = null;
        BufferedReader br = null;
        try {
            // 获取ftp上的文件
            logger.info("开始读取文件...");
            bre = new InputStreamReader(new FileInputStream(filePathAndName), StandardCharsets.UTF_8);
            // 转为字节流
            br = new BufferedReader(bre);
            while ((str = br.readLine()) != null) {
                list.add(str);
            }
            br.close();
        } catch (IOException e) {
            logger.error("读取文件【" + filePathAndName + "】IO异常！");
            e.printStackTrace();
        } finally {
            try {
                if (bre != null) {
                    bre.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                logger.error("IO关闭异常！" + e);
            }
        }
        return list;
    }
}
