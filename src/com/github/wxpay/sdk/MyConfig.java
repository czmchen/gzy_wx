package com.github.wxpay.sdk;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

public class MyConfig extends WXPayConfig {

	private static MyConfig instance = new MyConfig();
	private Logger log = Logger.getLogger(this.getClass());
	private byte[] certData;

	private MyConfig() {
		this.getConfig();
	}

	public static MyConfig getInstance() {
		return instance;
	}

	public void getConfig() {
		String certPath = "D:/ganzhuyoucert/apiclient_cert.p12";// MyConfig.class.getResource("/")+"apiclient_cert.p12";
		InputStream certStream = null;
		try {
			File file = new File(certPath);
			certStream = new FileInputStream(file);
			this.certData = new byte[(int) file.length()];
			certStream.read(this.certData);
		} catch (Exception e) {
			log.error(e);
		} finally {
			try {
				if (certStream != null) {
					certStream.close();
				}
			} catch (IOException e) {
			}
		}
	}

	public String getAppID() {
		return "wx0ae96c7d8a5d7e50";
	}

	public String getMchID() {
		return "1574760351";
	}

	public String getKey() {
		return "ganzhuyou678905678sjcxl88076vFkA";
	}

	public InputStream getCertStream() {
		ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
		return certBis;
	}

	public int getHttpConnectTimeoutMs() {
		return 8000;
	}

	public int getHttpReadTimeoutMs() {
		return 10000;
	}

	@Override
	IWXPayDomain getWXPayDomain() {
		IWXPayDomain iwxPayDomain = new IWXPayDomain() {
			@Override
			public void report(String domain, long elapsedTimeMillis, Exception ex) {

			}

			@Override
			public DomainInfo getDomain(WXPayConfig config) {
				return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
			}
		};
		return iwxPayDomain;
	}

}