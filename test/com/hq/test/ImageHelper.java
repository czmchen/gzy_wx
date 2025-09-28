package com.hq.test;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.weixin.util.PropertiesUtils;

/**
 * 图片工具类，完成图片的截取 所有方法返回值均未boolean型
 */
public class ImageHelper {
	private final static Logger logger = Logger.getLogger(ImageHelper.class);

	/**
	 * 实现图像的等比缩放
	 * 
	 * @param source
	 * @param targetW
	 * @param targetH
	 * @return
	 */
	private static BufferedImage resize(BufferedImage source, int targetW, int targetH) {
		// targetW，targetH分别表示目标长和宽
		int type = source.getType();
		BufferedImage target = null;
		double sx = (double) targetW / source.getWidth();
		double sy = (double) targetH / source.getHeight();
		// 这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放
		// 则将下面的if else语句注释即可
		if (sx < sy) {
			sx = sy;
			targetW = (int) (sx * source.getWidth());
		} else {
			sy = sx;
			targetH = (int) (sy * source.getHeight());
		}
		if (type == BufferedImage.TYPE_CUSTOM) { // handmade
			ColorModel cm = source.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else
			target = new BufferedImage(targetW, targetH, type);
		Graphics2D g = target.createGraphics();
		// smoother than exlax:
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
		g.dispose();
		return target;
	}

	/**
	 * 实现图像的等比缩放和缩放后的截取, 处理成功返回true, 否则返回false
	 * 
	 * @param inFilePath  要截取文件的路径
	 * @param outFilePath 截取后输出的路径
	 * @param width       要截取宽度
	 * @param hight       要截取的高度
	 * @throws Exception
	 */
	public static boolean compress(String inFilePath, String outFilePath, int width, int hight) {
		boolean ret = false;
		File file = new File(inFilePath);
		File saveFile = new File(outFilePath);
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			ret = compress(in, saveFile, width, hight);
		} catch (FileNotFoundException e) {
			logger.error(e);
			ret = false;
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}

		return ret;
	}

	/**
	 * 实现图像的等比缩放和缩放后的截取, 处理成功返回true, 否则返回false
	 * 
	 * @param in          要截取文件流
	 * @param outFilePath 截取后输出的路径
	 * @param width       要截取宽度
	 * @param hight       要截取的高度
	 * @throws Exception
	 */
	public static boolean compress(InputStream in, File saveFile, int width, int hight) {
//     boolean ret = false;
		BufferedImage srcImage = null;
		try {
			srcImage = ImageIO.read(in);
		} catch (IOException e) {
			logger.error(e);
			return false;
		}

		if (width > 0 || hight > 0) {
			// 原图的大小
			int sw = srcImage.getWidth();
			int sh = srcImage.getHeight();
			// 如果原图像的大小小于要缩放的图像大小，直接将要缩放的图像复制过去
			if (sw > width && sh > hight) {
				srcImage = resize(srcImage, width, hight);
			} else {
				String fileName = saveFile.getName();
				String formatName = fileName.substring(fileName.lastIndexOf('.') + 1);
				try {
					ImageIO.write(srcImage, formatName, saveFile);
				} catch (IOException e) {
					logger.error(e);
					return false;
				}
				return true;
			}
		}
		// 缩放后的图像的宽和高
		int w = srcImage.getWidth();
		int h = srcImage.getHeight();
		// 如果缩放后的图像和要求的图像宽度一样，就对缩放的图像的高度进行截取
		if (w == width) {
			// 计算X轴坐标
			int x = 0;
			int y = h / 2 - hight / 2;
			try {
				saveSubImage(srcImage, new Rectangle(x, y, width, hight), saveFile);
			} catch (IOException e) {
				logger.error(e);
				return false;
			}
		}
		// 否则如果是缩放后的图像的高度和要求的图像高度一样，就对缩放后的图像的宽度进行截取
		else if (h == hight) {
			// 计算X轴坐标
			int x = w / 2 - width / 2;
			int y = 0;
			try {
				saveSubImage(srcImage, new Rectangle(x, y, width, hight), saveFile);
			} catch (IOException e) {
				logger.error(e);
				return false;
			}
		}

		return true;
	}

	/**
	 * 实现图像的等比缩放和缩放后的截取, 处理成功返回true, 否则返回false
	 * 
	 * @param in         图片输入流
	 * @param saveFile   压缩后的图片输出流
	 * @param proportion 压缩比
	 * @throws Exception
	 */
	public static boolean compress(InputStream in, File saveFile, int proportion) {
		if (null == in || null == saveFile || proportion < 1) {// 检查参数有效性
			// LoggerUtil.error(ImageHelper.class, "--invalid parameter, do nothing!");
			return false;
		}

		BufferedImage srcImage = null;
		try {
			srcImage = ImageIO.read(in);
		} catch (IOException e) {
			logger.error(e);
			return false;
		}
		// 原图的大小
		int width = srcImage.getWidth() / proportion;
		int hight = srcImage.getHeight() / proportion;

		srcImage = resize(srcImage, width, hight);

		// 缩放后的图像的宽和高
		int w = srcImage.getWidth();
		int h = srcImage.getHeight();
		// 如果缩放后的图像和要求的图像宽度一样，就对缩放的图像的高度进行截取
		if (w == width) {
			// 计算X轴坐标
			int x = 0;
			int y = h / 2 - hight / 2;
			try {
				saveSubImage(srcImage, new Rectangle(x, y, width, hight), saveFile);
			} catch (IOException e) {
				logger.error(e);
				return false;
			}
		}
		// 否则如果是缩放后的图像的高度和要求的图像高度一样，就对缩放后的图像的宽度进行截取
		else if (h == hight) {
			// 计算X轴坐标
			int x = w / 2 - width / 2;
			int y = 0;
			try {
				saveSubImage(srcImage, new Rectangle(x, y, width, hight), saveFile);
			} catch (IOException e) {
				logger.error(e);
				return false;
			}
		}

		return true;
	}

	/**
	 * 实现缩放后的截图
	 * 
	 * @param image          缩放后的图像
	 * @param subImageBounds 要截取的子图的范围
	 * @param subImageFile   要保存的文件
	 * @throws IOException
	 */
	private static void saveSubImage(BufferedImage image, Rectangle subImageBounds, File subImageFile)
			throws IOException {
		if (subImageBounds.x < 0 || subImageBounds.y < 0 || subImageBounds.width - subImageBounds.x > image.getWidth()
				|| subImageBounds.height - subImageBounds.y > image.getHeight()) {
			// LoggerUtil.error(ImageHelper.class, "Bad subimage bounds");
			return;
		}
		BufferedImage subImage = image.getSubimage(subImageBounds.x, subImageBounds.y, subImageBounds.width,
				subImageBounds.height);
		String fileName = subImageFile.getName();
		String formatName = fileName.substring(fileName.lastIndexOf('.') + 1);
		ImageIO.write(subImage, formatName, subImageFile);
	}

	public static void processImg(final String fileFullName, final String abbrImgFullName, final int maxPixel)
			throws Exception {
		String fullFilePath = PropertiesUtils.read("customer.file.location") + "/images/";
		BufferedImage bi = ImageIO.read(new File(fullFilePath + fileFullName));

		int width = bi.getWidth();
		int height = bi.getHeight();
		int processWidth = width;
		int processHeight = height;
		int maxCurrentPixel = height;
		if (width > height) {
			maxCurrentPixel = width;
		}
		if (maxCurrentPixel > maxPixel) {
			processWidth = (int) (((double) maxPixel / (double) maxCurrentPixel) * width);
			processHeight = (int) (((double) maxPixel / (double) maxCurrentPixel) * height);
		}

		compress((fullFilePath + fileFullName), (fullFilePath + abbrImgFullName), processWidth, processHeight);
	}
	
	/**
     * 移动文件
     * @param filePath 文件路径 -- 从哪里移动
     * @param destPath 目标路径 -- 移动到哪里
     */
    public static void moveFile(String filePath, String destPath){
        saveAsFile(filePath, destPath);//拷贝
        deleteFile(filePath);//删除
    }

	/**
	 * 
	 * 移动文件
	 * 
	 * @param file     文件对象 -- 从那里移动
	 * 
	 * @param destPath 目标路径 -- 移动到哪里
	 * 
	 */

	public static void moveFile(File file, String destPath) {
		try {
			saveAsFile(file, destPath); // 拷贝
			deleteFile(file);// 删除
		} catch (FileNotFoundException e) {
			logger.error(e);
		}

	}

	// TODO -- 删除

	/**
	 * 
	 * 删除文件 --传递文件对象
	 * @param file 文件对象
	 * @throws FileNotFoundException 文件找不到
	 * 
	 */

	public static void deleteFile(File file) throws FileNotFoundException {
		if (file.exists()) {// 判断文件是否存在
			if (file.isFile()) {// 判断是否是文件
				file.delete();// 删除文件
			} else if (file.isDirectory()) {// 否则如果它是一个目录
				File[] files = file.listFiles();// 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) {// 遍历目录下所有的文件
					deleteFile(files[i]);// 把每个文件用这个方法进行迭代
				}
				file.delete();// 删除文件夹
			}
		} else {
			throw new FileNotFoundException();
		}
	}

	/**
	 * 
	 * 删除文件 -- 传递文件路径
	 * 
	 * @param filePath 文件路径
	 * 
	 * @throws FileNotFoundException 文件找不到
	 * 
	 */

	public static void deleteFile(String filePath) {
		try {
			deleteFile(new File(filePath));
		} catch (FileNotFoundException e) {
			logger.error(e);
		}
	}

	// TODO -- 拷贝

	/**
	 * 
	 * 保存文件 -- 可以是文件，也可以是一个文件夹
	 * 
	 * @param filePath 文件路径 --从哪里拷贝
	 * 
	 * @param destPath 要保存的路径 -- 拷贝到哪里
	 * 
	 *                 <p>
	 *                 </p>
	 * 
	 *                 <p>
	 *                 例如：saveAsFile("D:\\fisp_core_2019-02-28.xsd",
	 *                 "d:\\11\\fisp_core_2019-02-28.xsd");
	 *                 </p>
	 * 
	 *                 <p>
	 *                 生成D:\11\fisp_core_2019-02-28.xsd,D:\\fisp_core_2019-02-28.xsd不会删除，相当于拷贝
	 *                 </p>
	 * 
	 *                 <p>
	 *                 例如：saveAsFile("d:\\11", "d:\\122");
	 *                 </p>
	 * 
	 *                 <p>
	 *                 生成D:\122\fisp_core_2019-02-28.xsd和D:\122\fisp_core_2019-02-28.xsd1,11文件夹下有两个文件
	 *                 </p>
	 * 
	 */

	public static void saveAsFile(String filePath, String destPath) {
		File file = new File(filePath);
		saveAsFile(file, destPath);
	}

	/**
	 * 
	 * 保存文件 -- 可以是文件，也可以是一个文件夹
	 * 
	 * @param file     文件对象 --从哪里拷贝
	 * 
	 * @param destPath 要保存的文件路径 -- 拷贝到哪里
	 * 
	 */

	public static void saveAsFile(File file, String destPath) {
		if (file.isDirectory()) {
			// 文件夹
			File[] files = file.listFiles();// 声明目录下所有的文件 files[];
			for (File filei : files) {// 遍历目录下所有的文件
				saveAsFile(filei, destPath + File.separator + filei.getName());// 把每个文件用这个方法进行迭代
			}
		} else {
			// 文件
			FileInputStream in = null;
			try {
				in = new FileInputStream(file);
				saveAsFile(in, destPath);
			} catch (FileNotFoundException e) {
				logger.error(e);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						logger.error(e);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * 保存文件 --只能处理单个文件，不能是文件夹
	 * @param in       文件输入流对象
	 * @param destPath 要保存的路径，含有文件后缀名
	 */

	public static void saveAsFile(InputStream in, String destPath) {
		FileOutputStream out = null;
		BufferedOutputStream Bout = null;
		try {
			byte[] buf = new byte[1024];
			File file = new File(destPath);
			if (!file.exists()) {
				(new File(file.getParent())).mkdirs();
			}
			out = new FileOutputStream(file);
			Bout = new BufferedOutputStream(out);
			int b;
			while ((b = in.read(buf)) != -1) {
				Bout.write(buf, 0, b);
			}
		} catch (FileNotFoundException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} finally {
			if (Bout != null) {
				try {
					Bout.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		/**
		 * saveSubImage 截图类的使用 srcImage 为BufferedImage对象 Rectangle 为需要截图的长方形坐标 saveFile
		 * 需要保存的路径及名称
		 **/
		// 需要截图的长方形坐标
		/*
		 * Rectangle rect =new Rectangle(); rect.x=40; rect.y=40; rect.height=160;
		 * rect.width=160;
		 * 
		 * InputStream in = null; //需要保存的路径及名称 File saveFile = new
		 * File("d:\\ioc\\files\\aaa2.jpg"); //需要进行处理的图片的路径 in = new FileInputStream(new
		 * File("d:\\ioc\\files\\aaa.jpg")); BufferedImage srcImage = null;
		 * //将输入的数据转为BufferedImage对象 srcImage = ImageIO.read(in);
		 * 
		 * ImageHelper img=new ImageHelper(); img.saveSubImage(srcImage, rect,
		 * saveFile);
		 */
//
//		/**
//		 * compress 图片缩放类的使用(缩略图) srcImage 为InputStream对象 Rectangle 为需要截图的长方形坐标
//		 * proportion 为压缩比例
//		 **/
//		InputStream in = null;
//		// 缩放后需要保存的路径
//		
//		String fullFilePath = PropertiesUtils.read("customer.file.location") + "/images/";
//		File saveFile = new File(fullFilePath+"123_new.png");
//		try {
//			// 原图片的路径
//			in = new FileInputStream(new File(fullFilePath+"123.png"));
//			if (compress(in, saveFile, 2)) {
//				System.out.println("图片压缩十倍！");
//			}
//		} catch (Exception e) {
//			logger.error(e);
//		} finally {
//			in.close();
//		}

//		processImg("O4-MIBgjvUc9S03MZsNQ25JFVsUfR8XcaPfcyoXC5Y2Nr4gr82rTudxRCLrtPF7f.jpg","O4-MIBgjvUc9S03MZsNQ25JFVsUfR8XcaPfcyoXC5Y2Nr4gr82rTudxRCLrtPF7f_abbr.jpg", 300);
		moveFile("D:\\data\\upload\\images\\2724014e-e2e5-41b4-9376-a8dd079491cb.jpg","D:\\data\\images\\123.jpg");
	}
}