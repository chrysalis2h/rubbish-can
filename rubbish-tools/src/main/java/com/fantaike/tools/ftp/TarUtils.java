package com.fantaike.tools.ftp;

import cn.hutool.core.io.IoUtil;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.*;

/**
 * @Date: 2019/10/30 10:31
 * @Description: Tar包工具类
 * @system name: 新一代消费金融系统
 * @copyright: 长安新生（深圳）金融投资有限公司
 */
public class TarUtils {
    private TarArchiveOutputStream tarArchiveOutputStream;
    private File source;
    private File dist;
    private boolean deleteSource;

    /**
     * 将指定目录下的文件打成tar包
     *
     * @param srcDir       要压缩的目录
     * @param distDir      输出目录
     * @param tarName      tar包的名称
     * @param deleteSource 压缩后是否删除源文件
     */
    public TarUtils(String srcDir, String distDir, String tarName, boolean deleteSource) {
        source = new File(srcDir);
        File file = new File(distDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        dist = new File(distDir, tarName);
        this.deleteSource = deleteSource;
        if (source.exists()) {
            try {
                tarArchiveOutputStream = new TarArchiveOutputStream(new FileOutputStream(dist));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void build() {
        action(source);
        if (tarArchiveOutputStream != null) {
            try {
                tarArchiveOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void action(File file) {
        if (tarArchiveOutputStream == null) {
            return;
        }
        if (file.isFile()) {
            append(tarArchiveOutputStream, file);
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    action(f);
                }
            }
        }
    }

    private void append(TarArchiveOutputStream tarArchiveOutputStream, File file) {
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            TarArchiveEntry entry = new TarArchiveEntry(file);
            entry.setSize(file.length());
            entry.setName(file.getAbsolutePath().substring(source.getAbsolutePath().length() + 1));
            tarArchiveOutputStream.putArchiveEntry(entry);
            IoUtil.copy(is, tarArchiveOutputStream);
            tarArchiveOutputStream.flush();
            tarArchiveOutputStream.closeArchiveEntry();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(is);
            if (deleteSource) {
                if (!file.delete()) {
                }
            }
        }
    }
}
