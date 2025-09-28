package com.weixin.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpClientManager {
	private final static Logger logger = Logger.getLogger(HttpClientManager.class);

	private final static Object syncLock = new Object();
	
	private static final String IE8_USERAGENT = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Win64; x64; Trident/4.0; .NET CLR 2.0.50727; SLCC2; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; InfoPath.3)";
	private static final String APPLICATION_JSON = "application/json";
	private static final String CONTENT_TYPE_TEXT_JSON = "text/json";
	public static final String CONTENTTYPE_JSON = "application/json; charset=UTF-8";
	public static final String CONTENTTYPE_HTML = "text/html; charset=UTF-8";
	public static final String CHROME_USERAGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36";

	private static final String UTF8 =  "UTF-8";
	static final int timeOut = 10 * 1000;
	
	private static Map<String, CloseableHttpClient> HTTP_CLIENT_DATA = new HashMap<String, CloseableHttpClient>();

	/**
	 * 获取HttpClient对象
	 * 
	 * @return
	 * @author SHANHY
	 * @create 2015年12月18日
	 */
	public static CloseableHttpClient getHttpClient(String url) {
		String hostname = url.split("/")[2];
		int port = 80;
		if (hostname.contains(":")) {
			String[] arr = hostname.split(":");
			hostname = arr[0];
			port = Integer.parseInt(arr[1]);
		}
		String keyString = hostname+":"+port;
		if (HTTP_CLIENT_DATA.get(keyString)==null) {
			synchronized (syncLock) {
				HTTP_CLIENT_DATA.put(keyString,createHttpClient(200, 40, 100, hostname, port));
			}
		}
		return HTTP_CLIENT_DATA.get(keyString);
	}

	/**
	 * 创建HttpClient对象
	 * 
	 * @return
	 * @author SHANHY
	 * @create 2015年12月18日
	 */
	public static CloseableHttpClient createHttpClient(int maxTotal, int maxPerRoute, int maxRoute, String hostname,
			int port) {
		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", plainsf).register("https", sslsf).build();
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
		// 将最大连接数增加
		cm.setMaxTotal(maxTotal);
		// 将每个路由基础的连接增加
		cm.setDefaultMaxPerRoute(maxPerRoute);
		HttpHost httpHost = new HttpHost(hostname, port);
		// 将目标主机的最大连接数增加
		cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

		// 请求重试处理
		HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				if (executionCount >= 5) {// 如果已经重试了5次，就放弃
					return false;
				}
				if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
					return true;
				}
				if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
					return false;
				}
				if (exception instanceof InterruptedIOException) {// 超时
					return false;
				}
				if (exception instanceof UnknownHostException) {// 目标服务器不可达
					return false;
				}
				if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
					return false;
				}
				if (exception instanceof SSLException) {// SSL握手异常
					return false;
				}

				HttpClientContext clientContext = HttpClientContext.adapt(context);
				HttpRequest request = clientContext.getRequest();
				// 如果请求是幂等的，就再次尝试
				if (!(request instanceof HttpEntityEnclosingRequest)) {
					return true;
				}
				return false;
			}
		};
		// 配置请求的超时设置
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeOut)
				.setConnectTimeout(timeOut).setSocketTimeout(timeOut).build();

		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
				.setDefaultRequestConfig(requestConfig).setUserAgent(CHROME_USERAGENT)
				.setRetryHandler(httpRequestRetryHandler).build();
		return httpClient;
	}

	private static void setPostParams(HttpPost httpost, Map<String, String> params) {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key).toString()));
		}
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, UTF8));
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		}
	}

	/**
	 * 
	 * @Title: postForm
	 * @Description: TODO Post Form表单
	 * @param @param  url
	 * @param @param  paramMap
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String postForm(final String url, final Map<String, String> paramMap) {
		CloseableHttpResponse response = null;
		HttpPost httpPost = new HttpPost(url);
		String resultString = null;
		try {
			setPostParams(httpPost, paramMap);
			response = getHttpClient(url).execute(httpPost, HttpClientContext.create());
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = response.getEntity();
				resultString = (entity != null ? EntityUtils.toString(entity, UTF8) : null);
			} else {
				throw new ClientProtocolException("Unexpected response status: " + status);
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
				httpPost.abort();
				httpPost = null;
			}
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());// 释放连接
					response.close();
					response = null;
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		return resultString;
	}

	/**
	 * 
	 * @Title: postForm
	 * @Description: TODO Post Form表单
	 * @param @param  url
	 * @param @param  paramMap
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @author 陈周敏
	 * @throws
	 */
	public static String postJSON(final String url, final String jsonData) {
		CloseableHttpResponse response = null;
		HttpPost httpPost = new HttpPost(url);
		String resultString = null;
		try {
			httpPost.addHeader(HTTP.CONTENT_TYPE, CONTENTTYPE_JSON);

			StringEntity se = new StringEntity(jsonData);// 建立一个NameValuePair数组，用于存储欲传送的参数
			se.setContentType(CONTENT_TYPE_TEXT_JSON);
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
			httpPost.setEntity(new StringEntity(jsonData, Consts.UTF_8)); // 设置表单提交编码为UTF-8
			response = getHttpClient(url).execute(httpPost, HttpClientContext.create());
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {// 成功解析结果
				HttpEntity entity = response.getEntity();
				resultString = (entity != null ? EntityUtils.toString(entity, UTF8) : null);
			} else {
				throw new ClientProtocolException("Unexpected response status: " + status);
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
				httpPost.abort();
				httpPost = null;
			}
			if (null != response) {// 回收链接到连接池
				try {
					EntityUtils.consume(response.getEntity());
					response.close();
					response = null;
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		return resultString;
	}

	/**
	 * 
	 * @author
	 * @date 2017年4月21日 下午3:25:05
	 * @description 提交JSON数据
	 * @param url
	 * @param jsonObject
	 * @return
	 */
	public String postInputStream(final String url, InputStream in) {
		HttpPost httpPost = new HttpPost(url);
		InputStreamEntity inputEntry = new InputStreamEntity(in);
		CloseableHttpResponse response = null;
		String resultString = null;
		try {
			// 设置表单提交编码为UTF-8
			httpPost.setEntity(inputEntry);
			response = getHttpClient(url).execute(httpPost, HttpClientContext.create());
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {// 成功解析结果
				HttpEntity entity = response.getEntity();
				resultString = (entity != null ? EntityUtils.toString(entity, UTF8) : null);
			} else {
				throw new ClientProtocolException("Unexpected response status: " + status);
			}
		} catch (Exception e) {
			httpPost.releaseConnection();
			httpPost.abort();
			throw new RuntimeException(e);
		} finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
				httpPost.abort();
				httpPost = null;
			}
			if (null != response) {// 回收链接到连接池
				try {
					EntityUtils.consume(response.getEntity());
					response.close();
					response = null;
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		return resultString;
	}

	/**
	 * 描述：根据给定的url下载信息
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String loadUrl(final String url) {
		if (StringUtil.isRealEmpty(url)) {
			return null; // 如果url为空或者null，则返回src空值
		}
		CloseableHttpResponse response = null;
		HttpGet httpGet = new HttpGet(url);
		String resultString = null;
		try {
			response = getHttpClient(url).execute(httpGet, HttpClientContext.create());
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {// 成功解析结果
				HttpEntity entity = response.getEntity();
				resultString = (entity != null ? EntityUtils.toString(entity, UTF8) : null);
			} else {
				throw new ClientProtocolException("Unexpected response status: " + status);
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			if (httpGet != null) {
				httpGet.releaseConnection();
				httpGet.abort();
				httpGet = null;
			}
			if (null != response) {// 回收链接到连接池
				try {
					EntityUtils.consume(response.getEntity());
					response.close();
					response = null;
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		return resultString;
	}

	public static String upload(final String localFile, final String targetURL) throws Exception {
		HttpPost httpPost = null;
		CloseableHttpResponse response = null;
		String resultString = null;

		try {
			httpPost = new HttpPost(targetURL);// 把一个普通参数和文件上传给下面这个地址 是一个servlet
			FileBody bin = new FileBody(new File(localFile));// 把文件转换成流对象FileBody
			HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("file", bin).build();
			httpPost.setEntity(reqEntity);

			response = getHttpClient(targetURL).execute(httpPost, HttpClientContext.create());
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {// 成功解析结果
				HttpEntity entity = response.getEntity();
				resultString = (entity != null ? EntityUtils.toString(entity, UTF8) : null);
			} else {
				throw new ClientProtocolException("Unexpected response status: " + status);
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
				httpPost.abort();
				httpPost = null;
			}
			if (null != response) {// 回收链接到连接池
				try {
					EntityUtils.consume(response.getEntity());
					response.close();
					response = null;
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		return resultString;
	}

	public static void downloadImg(String strUrl, String fileName) throws Exception {
		HttpGet httpGet = new HttpGet(strUrl);
		httpGet.setHeader("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
		httpGet.setHeader("Accept-Encoding", "gzip, deflate");
		httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
		httpGet.setHeader("User-Agent", IE8_USERAGENT);

		String fullFilePath = PropertiesUtils.read("customer.file.location") + "/images/";
		File file = new File(fullFilePath);
		FileOutputStream fos = null;
		CloseableHttpResponse response = null;
		try {
			// 客户端开始向指定的网址发送请求
			response = getHttpClient(strUrl).execute(httpGet, HttpClientContext.create());
			
			InputStream inputStream = response.getEntity().getContent();
			if (!file.exists()) {
				file.mkdirs();
			}

			fos = new FileOutputStream(fullFilePath + fileName);
			byte[] data = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(data)) != -1) {
				fos.write(data, 0, len);
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			if (httpGet != null) {
				httpGet.releaseConnection();
				httpGet.abort();
				httpGet = null;
			}
			if (fos != null) {
				fos.close();
				fos = null;
			}if (null != response) {// 回收链接到连接池
				try {
					EntityUtils.consume(response.getEntity());
					response.close();
					response = null;
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
	}

	public static void download(Map<String, String> header, String fileType, String strUrl, String fileName) throws Exception {
		HttpGet httpGet = new HttpGet(strUrl);
		CloseableHttpResponse response = null;
		for (String key : header.keySet()) {
			httpGet.setHeader(key, header.get(key));
		}

		String fullFilePath = PropertiesUtils.read("customer.file.location") + "/" + fileType + "/";
		File file = new File(fullFilePath);
		FileOutputStream fos = null;
		try {
			// 客户端开始向指定的网址发送请求
			response = getHttpClient(strUrl).execute(httpGet, HttpClientContext.create());
			InputStream inputStream = response.getEntity().getContent();
			if (!file.exists()) {
				file.mkdirs();
			}

			fos = new FileOutputStream(fullFilePath + fileName);
			byte[] data = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(data)) != -1) {
				fos.write(data, 0, len);
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			if (httpGet != null) {
				httpGet.releaseConnection();
				httpGet.abort();
				httpGet = null;
			}
			if (fos != null) {
				fos.close();
				fos = null;
			}if (null != response) {// 回收链接到连接池
				try {
					EntityUtils.consume(response.getEntity());
					response.close();
					response = null;
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
	}

	public static void main(String[] args) {
//		System.out.println(new HttpClientManager().loadUrl("http://192.168.233.129:5700/env"));
//		System.out.println(new HttpClientManager().loadUrl("http://www.baidu.com"));
//		System.out.println(new HttpClientManager().loadUrl("https://www.baidu.com/s?wd=httpclientUtil%205%E4%BD%BF%E7%94%A8&pn=10&oq=httpclientUtil%205%E4%BD%BF%E7%94%A8&ie=utf-8&rsv_pq=dc74e58e0000453b&rsv_t=e22ao6ItMqNOF%2BuE7P6UhFMFrCrlp%2BSzouR8gRbKrpAdiIPKH82aeru51SM&gpc=stf%3D1658029891%2C1660708290%7Cstftype%3D1&tfflag=1&rsv_page=1"));
//		System.out.println(new HttpClientManager().loadUrl("https://mp.weixin.qq.com/cgi-bin/safecenterstatus?action=view&t=setting/safe-index&token=327092373&lang=zh_CN"));
//		System.out.println(new HttpClientManager().loadUrl("https://t.10jqka.com.cn/circle/241263/"));
	}
}
