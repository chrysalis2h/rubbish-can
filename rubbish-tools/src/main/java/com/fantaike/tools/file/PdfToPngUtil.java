package com.fantaike.tools.file;

import cn.hutool.log.Log;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/10/19.
 */
public class PdfToPngUtil {

    private static final Log logger = Log.get();

    public static List<String> pdf2Image(String PdfFilePath, String dstImgFolder, int dpi) {

        File file = new File(PdfFilePath);

        List<String> pngList = new ArrayList<String>();
        PDDocument pdDocument;

        try {

            int dot = file.getName().lastIndexOf('.');

            String imagePDFName = file.getName().substring(0, dot); // 获取图片文件名

            String imgFolderPath = dstImgFolder;


            pdDocument = PDDocument.load(file);

            PDFRenderer renderer = new PDFRenderer(pdDocument);

            /* dpi越大转换后越清晰，相对转换速度越慢 */

            File fileUrl = new File(PdfFilePath);

            PDDocument pdfReader = PDDocument.load(fileUrl);

            int pages = pdfReader.getNumberOfPages();

            StringBuffer imgFilePath = null;

            for (int i = 0; i < pages; i++) {

                String imgFilePathPrefix = imgFolderPath + imagePDFName;
                StringBuffer imgFilePathQD = new StringBuffer();
                imgFilePath = new StringBuffer();
                imgFilePath.append(imgFilePathPrefix);
                imgFilePathQD.append(imagePDFName);
                imgFilePath.append(String.valueOf(i + 1));
                imgFilePathQD.append(String.valueOf(i + 1));
                imgFilePath.append(".png");
                imgFilePathQD.append(".png");
                File dstFile = new File(imgFilePath.toString());
                BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                ImageIO.write(image, "png", dstFile);
                pngList.add(i, "/mdd-xapp-web/fileDownload/" + imgFilePathQD.toString());
            }

            System.out.println("PDF文档转PNG图片成功！");
            return pngList;


        } catch (IOException e) {

            logger.error("异常：", e);

        }
        return pngList;
    }

}
