package com.yq.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.weixin.util.PropertiesUtils;

public class Pdf2JpgUtil {

	public static void main(String args[]) {
		convert("20200918-07-922.pdf");
	}

	public static List<String> convert(String pdfFile) {
		PDDocument doc = null;
		ByteArrayOutputStream os = null;
		InputStream stream = null;

		List<String> pdf2jpgData = new ArrayList<String>();
		OutputStream out = null;

		/*
		 * InputStream is = null; OutputStream responseOut = null;
		 */
		try {
			long start = System.currentTimeMillis();

			String tempPath = PropertiesUtils.read("customer.file.location") + "/upload/";// 获取上传文件临时保存路径
			// pdf路径
			stream = new FileInputStream(new File(tempPath + pdfFile));

			// 获取文件名
			String fileName = pdfFile.substring(0, pdfFile.lastIndexOf("."));
			// 加载解析PDF文件
			doc = PDDocument.load(stream);
			PDFRenderer pdfRenderer = new PDFRenderer(doc);
			PDPageTree pages = doc.getPages();
			int pageCount = pages.getCount();
			for (int i = 0; i < pageCount; i++) {
				BufferedImage bim = pdfRenderer.renderImageWithDPI(i, 200);
				os = new ByteArrayOutputStream();
				ImageIO.write(bim, "jpg", os);
				byte[] datas = os.toByteArray();
				
				String file2DeskName = fileName + "_" + i + ".jpg";
				// jpg文件转出路径
				out = new FileOutputStream(tempPath + file2DeskName);
				out.write(datas);

				// 通过response输出流
				/*
				 * is = new ByteArrayInputStream(datas); byte[] buffer = new byte[1024]; int len
				 * = 0; while ((len = is.read(buffer)) > 0) { responseOut.write(buffer, 0, len);
				 * } responseOut.flush();
				 */
				pdf2jpgData.add(file2DeskName);
			}
			long end = System.currentTimeMillis();
			long time = (end - start);
			System.out.println("pdf转jpg耗时： " + time);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (doc != null) {
				try {
					doc.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			/*
			 * if (is != null) { try { is.close(); } catch (Exception e) {
			 * e.printStackTrace(); } } if (responseOut != null) { try {
			 * responseOut.close(); } catch (Exception e) { e.printStackTrace(); } }
			 */
		}
		return pdf2jpgData;

	}
}