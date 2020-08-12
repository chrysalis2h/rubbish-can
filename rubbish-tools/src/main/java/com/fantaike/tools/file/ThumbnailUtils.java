package com.fantaike.tools.file;

import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;

/**
 * @Date: 2019/10/28 17:07
 * @Description: 图像处理工具类
 * @system name: 新一代消费金融系统
 * @copyright: 长安新生（深圳）金融投资有限公司
 */
public class ThumbnailUtils {

    /**
     * @param oldFilePath
     * @param newFilePath
     * @Description: 缩小图片
     * @Return: java.io.File
     * @Exception:
     * @Date: 2019/10/29 11:11
     */
    public static File makeThumbnail(String oldFilePath, String newFilePath, long maxSize) throws IOException {
        File oldFile = new File(oldFilePath);
        long fileSize = oldFile.length();
        File resultFile = oldFile;
        String fileUri = newFilePath.substring(0, newFilePath.lastIndexOf("."));
        File newFile = new File(fileUri);
        File newExistFile = new File(newFilePath);
        // 格式转换成jpg
        String lastFileName = oldFile.getName().substring(oldFile.getName().lastIndexOf(".")).toLowerCase();
        if (".pdf".equals(lastFileName)) {
            return resultFile;
        }
        if (!".jpg".equals(lastFileName)) {
            Thumbnails.of(oldFile).scale(1f).outputFormat("jpg").outputQuality(0.8f).toFile(newFile);
            /*fileSize = newExistFile.length();
            resultFile = newExistFile;*/
            resultFile = new File(newFile + ".jpg");
            fileSize = resultFile.length();
            oldFile.delete();
            oldFile = resultFile;
            // TODO 删除之前的png
        }
        // 处理文件大小到500K以下
        if (fileSize >= maxSize) {
            // 第一次将图片尺寸变小，高度固定480
            Thumbnails.of(oldFile).height(768).outputFormat("jpg").outputQuality(0.8f).toFile(newFile);
            fileSize = newExistFile.length();
            // 一般这时图片就在200K以下了，以防万一，循环压缩质量，直到500KB以下
            while (fileSize > maxSize) {
                Thumbnails.of(newExistFile).scale(1D).outputFormat("jpg").outputQuality(0.8f).toFile(newFile);
                fileSize = newExistFile.length();
            }
            resultFile = newExistFile;
        }
        return resultFile;
    }

    public static void main(String[] args) throws IOException {
        ThumbnailUtils.makeThumbnail("D:\\test\\100.JPG", "D:\\test\\100.jpg", 500 * 1024);
    }

}
