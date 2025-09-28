package com.yq.util;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

public class CreateImage {
//
//	/**
//	 * 获取指定视频的帧并保存为图片至指定目录 [url=home.php?mod=space&uid=952169]@Param[/url] videofile
//	 * 源视频文件路径
//	 * 
//	 * @param framefile 截取帧的图片存放路径
//	 * @throws Exception
//	 */
//	public static void fetchFrame(String videofile, String framefile) throws Exception {
//		File targetFile = new File(framefile);
//		FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videofile);
//		ff.start();
//		int lenght = ff.getLengthInFrames();
//		int i = 0;
//		Frame f = null;
//		while (i < lenght) {
//			// 过滤前5帧，避免出现全黑的图片，依自己情况而定
//			f = ff.grabFrame();
//			if ((i > 5) && (f.image != null)) {
//				break;
//			}
//			i++;
//		}
//		opencv_core.IplImage img = f.image;
//		int owidth = img.width();
//		int oheight = img.height();
//		// 对截取的帧进行等比例缩放
//		int width = 320;
//		int height = (int) (((double) width / owidth) * oheight);
//		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
//		bi.getGraphics().drawImage(f.image.getBufferedImage().getScaledInstance(width, height, Image.SCALE_SMOOTH), 0,
//				0, null);
//		ImageIO.write(bi, "jpg", targetFile);
//		// ff.flush();
//		ff.stop();
//	}

	/**
	 * 
	 * 获取视频第一帧
	 * 
	 * @param videoPath 视频地址
	 * 
	 * @param imgPath   生成图片的名字(包含全路径)
	 * 
	 * @throws Exception
	 * 
	 */

	public static void getVideoPicture(String videoPath, String imgPath) throws Exception {
		File imgFile = new File(imgPath);
		// 判断保存的文件的文件夹是否存在，不存在创建。
		if (!imgFile.getParentFile().exists()) {
			imgFile.getParentFile().mkdirs();
		}
		File videoFile = new File(videoPath);
		if (videoFile.exists()) {
			// 实例化“截取视频首帧”对象
			FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
			ff.start();
			int ftp = ff.getLengthInFrames();
			int flag = 0;
			Frame frame = null;
			while (flag <= ftp) {
				// 获取帧
				frame = ff.grabImage();
				// 过滤前1帧，避免出现全黑图片
				if ((flag > 1) && (frame != null)) {
					break;
				}
				flag++;
			}
			ImageIO.write(frameToBufferedImage(frame), "jpg", imgFile);
			ff.close();
			ff.stop();
		} else {
		}
	}

	/**
	 * 
	 * 帧转为流
	 * 
	 * @param frame
	 * 
	 * @return
	 * 
	 */

	private static RenderedImage frameToBufferedImage(Frame frame) {

		// 创建BufferedImage对象

		Java2DFrameConverter converter = new Java2DFrameConverter();

		int owidth = frame.imageWidth;
		int oheight = frame.imageHeight;
		// 对截取的帧进行等比例缩放
		int width = 320;
		int height = (int) (((double) width / owidth) * oheight);
		BufferedImage bufferedImage = converter.getBufferedImage(frame);
		BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		tag.getGraphics().drawImage(bufferedImage, 0, 0, width, height, null); // 绘制缩小后的图
		return tag;
	}

	public static void main(String[] args) {
		try {
//			CreateImage.fetchFrame(
//					"D:\\data\\video\\kmGlQ6TpNVOpqjfAsz-t-sKJXSNAEtKKTCYY7KPicVGa7iwfxxY8Jy8me05ejxvU.mp4",
//					"D:\\data\\video\\test5.jpg");
			
			String videoPath = "D:\\data\\video\\kmGlQ6TpNVOpqjfAsz-t-sKJXSNAEtKKTCYY7KPicVGa7iwfxxY8Jy8me05ejxvU.mp4";

			String imgPath = "D:\\data\\video\\test8.jpg";

			getVideoPicture(videoPath,imgPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
