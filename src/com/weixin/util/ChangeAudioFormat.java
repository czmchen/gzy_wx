package com.weixin.util;

import java.io.File;

import org.apache.log4j.Logger;

import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

public class ChangeAudioFormat {

	private final static Logger logger = Logger.getLogger(ChangeAudioFormat.class);

	public static void main(String[] args) throws Exception {
		String path1 = "D:/data/upload/voice/c53ae0be-7aa1-4d87-825c-e0ab4f1a6c84.wav";
		String path2 = "D:/data/upload/voice/c53ae0be-7aa1-4d87-825c-e0ab4f1a6c84.amr";
		changeToAMR(path1, path2);

		/*
		 * File file = new File(path2); FileInputStream fis = new FileInputStream(file);
		 * BufferedInputStream stream = new BufferedInputStream(fis); Player player =
		 * new Player(stream); player.play();
		 */
	}

	public static void changeToMp3(String sourcePath, String targetPath) throws Exception {

		try {
			File source = new File(sourcePath);
			File target = new File(targetPath);

			// Audio Attributes
			AudioAttributes audio = new AudioAttributes();
			audio.setCodec("libmp3lame");
			audio.setBitRate(128000);
			audio.setChannels(2);
			audio.setSamplingRate(44100);

			// Encoding attributes
			EncodingAttributes attrs = new EncodingAttributes();
			attrs.setOutputFormat("mp3");
			attrs.setAudioAttributes(audio);

			// Encode
			Encoder encoder = new Encoder();
			encoder.encode(new MultimediaObject(source), target, attrs);

		} catch (Exception e) {
			logger.error(e);
		}

		/*
		 * 
		 * File source = new File(sourcePath); File target = new File(targetPath);
		 * AudioAttributes audio = new AudioAttributes(); Encoder encoder = new
		 * Encoder();
		 * 
		 * audio.setCodec("libmp3lame"); audio.setBitRate(new Integer(128000)); // 比特率
		 * audio.setChannels(new Integer(2)); // 声音频道 audio.setSamplingRate(new
		 * Integer(44100)); // 节录率 EncodingAttributes attrs = new EncodingAttributes();
		 * attrs.setFormat("mp3"); attrs.setAudioAttributes(audio);
		 * 
		 * try { encoder.encode(source, target, attrs); } catch (Exception e) {
		 * logger.error(e); throw e; }
		 */
	}

	public static void changeToAMR(String sourcePath, String targetPath) {
		File source = new File(sourcePath);
		File target = new File(targetPath);
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libopencore_amrnb");
		audio.setBitRate(new Integer(4750));
		audio.setChannels(new Integer(1));
		audio.setSamplingRate(new Integer(8000));
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setOutputFormat("amr");
		attrs.setAudioAttributes(audio);
		Encoder encoder = new Encoder();

		try {
			encoder.encode(new MultimediaObject(source), target, attrs);
		} catch (Exception e) {
			logger.error(e);
		}
	}
}