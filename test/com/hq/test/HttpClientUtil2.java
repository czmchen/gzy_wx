package com.hq.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpClientUtil2 {

	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil2.class);

	private static final int DEFULT_TIMEOUT = 30 * 1000;// 默认超时时间20秒

	/**
	 * 可以访问 http://localhost:9005/yzhwsj/addPersonal/1/2 这样的接口
	 * 
	 * @param url
	 * @param headers
	 * @param urlParam
	 * @param timeout
	 * @return
	 */
	private static String doUrlGet(String url, Map<String, String> headers, List<String> urlParam, Integer timeout) {
		// 创建httpClient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String resultString = null;
		CloseableHttpResponse response = null;
		try {
			// 创建uri
			if (urlParam != null) {
				for (String param : urlParam) {
					url = url + "/" + param;
				}
			}
			// 创建hTTP get请求
			HttpGet httpGet = new HttpGet(url);
			// 设置超时时间
			int timeoutTmp = DEFULT_TIMEOUT;
			if (timeout != null) {
				timeoutTmp = timeout;
			}
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeoutTmp)
					.setConnectionRequestTimeout(timeoutTmp).setSocketTimeout(timeoutTmp).build();
			httpGet.setConfig(requestConfig);
			// 设置头信息
			if (null != headers) {
				for (String key : headers.keySet()) {
					httpGet.setHeader(key, headers.get(key));
				}
			}
			// 执行请求
			response = httpClient.execute(httpGet);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				resultString = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
			}
		} catch (IOException e) {
			logger.error("http调用异常" + e.toString(), e);
		} finally {
			try {
				if (null != response) {
					response.close();
				}
			} catch (IOException e) {
				logger.error("response关闭异常" + e.toString(), e);
			}
			try {
				if (null != httpClient) {
					httpClient.close();
				}
			} catch (IOException e) {
				logger.error("httpClient关闭异常" + e.toString(), e);
			}
		}
		return resultString;
	}

	/**
	 * @param url
	 * @param headers
	 * @param params
	 * @param timeout
	 * @return
	 */
	private static String doGet(String url, Map<String, String> headers, Map<String, Object> params, Integer timeout) {
		// 创建httpClient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String resultString = null;
		CloseableHttpResponse response = null;
		try {
			// 1.创建uri
			URIBuilder builder = new URIBuilder(url);
			if (params != null) {
				// uri添加参数
				for (String key : params.keySet()) {
					builder.addParameter(key, String.valueOf(params.get(key)));
				}
			}
			URI uri = builder.build();
			// 2.创建hTTP get请求
			HttpGet httpGet = new HttpGet(uri);
			// 3.设置超时时间
			int timeoutTmp = DEFULT_TIMEOUT;
			if (timeout != null) {
				timeoutTmp = timeout;
			}
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeoutTmp)
					.setConnectionRequestTimeout(timeoutTmp).setSocketTimeout(timeoutTmp).build();
			httpGet.setConfig(requestConfig);
			// 4.设置头信息
			if (null != headers) {
				for (String key : headers.keySet()) {
					httpGet.setHeader(key, headers.get(key));
				}
			}
			// 5.执行请求
			response = httpClient.execute(httpGet);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				resultString = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
			}
		} catch (URISyntaxException e) {
			logger.error("http调用异常" + e.toString(), e);
		} catch (IOException e) {
			logger.error("http调用异常" + e.toString(), e);

		} finally {
			try {
				if (null != response) {
					response.close();
				}
			} catch (IOException e) {
				logger.error("response关闭异常" + e.toString(), e);
			}
			try {
				if (null != httpClient) {
					httpClient.close();
				}
			} catch (IOException e) {
				logger.error("httpClient关闭异常" + e.toString(), e);
			}
		}
		return resultString;
	}

	/**
	 * 调用http post请求（json数据）
	 *
	 * @param url
	 * @param headers
	 * @param json
	 * @return
	 */
	public static String doJsonPost(String url, Map<String, String> headers, JSONObject json, Integer timeout) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 1.创建http post请求
			HttpPost httpPost = new HttpPost(url);
			// 2.设置超时时间
			int timeoutTmp = DEFULT_TIMEOUT;
			if (timeout != null) {
				timeoutTmp = timeout;
			}
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeoutTmp)
					.setConnectionRequestTimeout(timeoutTmp).setSocketTimeout(timeoutTmp).build();
			httpPost.setConfig(requestConfig);
			// 3.设置参数信息
			StringEntity s = new StringEntity(json.toString(), Consts.UTF_8);
			// 发送json数据需要设置的contentType
			s.setContentType("application/json");
			httpPost.setEntity(s);
			// 4.设置头信息
			if (headers != null) {
				for (String key : headers.keySet()) {
					httpPost.setHeader(key, headers.get(key));
				}
			}
			// 5.执行http请求
			response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				resultString = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("调用http异常" + e.toString(), e);
		} catch (ClientProtocolException e) {
			logger.error("调用http异常" + e.toString(), e);
		} catch (IOException e) {
			logger.error("调用http异常" + e.toString(), e);
		} finally {
			try {
				if (null != response) {
					response.close();
				}
			} catch (IOException e) {
				logger.error("关闭response异常" + e.toString(), e);
			}
			try {
				if (null != httpClient) {
					httpClient.close();
				}
			} catch (IOException e) {
				logger.error("关闭httpClient异常" + e.toString(), e);
			}
		}
		return resultString;
	}

	/**
	 * 调用http post请求 基础方法
	 *
	 * @param url     请求的url
	 * @param headers 请求头
	 * @param params  参数
	 * @param timeout 超时时间
	 * @return
	 */
	public static String doPost(String url, Map<String, String> headers, Map<String, Object> params, Integer timeout) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 1.创建http post请求
			HttpPost httpPost = new HttpPost(url);
			// 2.设置超时时间
			int timeoutTmp = DEFULT_TIMEOUT;
			if (timeout != null) {
				timeoutTmp = timeout;
			}
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeoutTmp)
					.setConnectionRequestTimeout(timeoutTmp).setSocketTimeout(timeoutTmp).build();
			httpPost.setConfig(requestConfig);
			// 3.设置参数信息
			if (params != null) {
				List<NameValuePair> paramList = new ArrayList<>();
				for (String key : params.keySet()) {
					paramList.add(new BasicNameValuePair(key, String.valueOf(params.get(key))));
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Consts.UTF_8);
				httpPost.setEntity(entity);
			}
			// 4.设置头信息
			if (headers != null) {
				for (String key : headers.keySet()) {
					httpPost.setHeader(key, headers.get(key));
				}
			}
			// 5.执行http请求
			response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				resultString = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("调用http异常" + e.toString(), e);
		} catch (ClientProtocolException e) {
			logger.error("调用http异常" + e.toString(), e);
		} catch (IOException e) {
			logger.error("调用http异常" + e.toString(), e);
		} finally {
			try {
				if (null != response) {
					response.close();
				}
			} catch (IOException e) {
				logger.error("关闭response异常" + e.toString(), e);
			}
			try {
				if (null != httpClient) {
					httpClient.close();
				}
			} catch (IOException e) {
				logger.error("关闭httpClient异常" + e.toString(), e);
			}
		}
		return resultString;
	}

	/**
	 * 通过httpClient上传文件
	 *
	 * @param url      请求的URL
	 * @param headers  请求头参数
	 * @param path     文件路径
	 * @param fileName 文件名称
	 * @param timeout  超时时间
	 * @return
	 */
	public static String UploadFileByHttpClient(String url, Map<String, String> headers, String path, String fileName,
			Integer timeout) {
		String resultString = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		InputStream content = null;
		BufferedReader in = null;
		try {
			// 1.创建http post请求
			HttpPost httpPost = new HttpPost(url);

			// 2.设置超时时间
			int timeoutTmp = DEFULT_TIMEOUT;
			if (timeout != null) {
				timeoutTmp = timeout;
			}
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeoutTmp)
					.setConnectionRequestTimeout(timeoutTmp).setSocketTimeout(timeoutTmp).build();
			httpPost.setConfig(requestConfig);

			// 3.设置参数信息
			// HttpMultipartMode.RFC6532参数的设定是为避免文件名为中文时乱码
			MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
			// 上传文件的路径
			File file = new File(path + File.separator + fileName);
			builder.setCharset(Charset.forName("UTF-8"));
			builder.addBinaryBody("file", file, ContentType.MULTIPART_FORM_DATA, fileName);
			HttpEntity entity = builder.build();
			httpPost.setEntity(entity);

			// 4.设置头信息
			if (headers != null) {
				for (String key : headers.keySet()) {
					httpPost.setHeader(key, headers.get(key));
				}
			}

			// 5.执行http请求
			response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				resultString = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
			}
		} catch (Exception e) {
			logger.error("上传文件失败：", e);
		} finally {
			try {
				if (null != httpClient) {
					httpClient.close();
				}
			} catch (IOException e) {
				logger.error("关闭httpClient异常" + e.toString(), e);
			}
			try {
				if (null != content) {
					content.close();
				}
			} catch (IOException e) {
				logger.error("关闭content异常" + e.toString(), e);
			}
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				logger.error("关闭in异常" + e.toString(), e);
			}
		}

		return resultString;
	}

	/**
	 * 通过httpClient批量上传文件
	 *
	 * @param url     请求的URL
	 * @param headers 请求头参数
	 * @param params  参数
	 * @param paths   文件路径(key文件名称，value路径)
	 * @param timeout 超时时间
	 * @return
	 */
	public static String UploadFilesByHttpClient(String url, Map<String, String> headers, Map<String, String> params,
			Map<String, String> paths, Integer timeout) {
		String resultString = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		InputStream content = null;
		BufferedReader in = null;
		try {
			// 1.创建http post请求
			HttpPost httpPost = new HttpPost(url);

			// 2.设置超时时间
			int timeoutTmp = DEFULT_TIMEOUT;
			if (timeout != null) {
				timeoutTmp = timeout;
			}
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeoutTmp)
					.setConnectionRequestTimeout(timeoutTmp).setSocketTimeout(timeoutTmp).build();
			httpPost.setConfig(requestConfig);

			// 3.设置文件信息
			// HttpMultipartMode.RFC6532参数的设定是为避免文件名为中文时乱码
			MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
			builder.setCharset(Charset.forName("UTF-8"));
			// 上传文件的路径
			for (Map.Entry<String, String> m : paths.entrySet()) {
				File file = new File(m.getValue() + File.separator + m.getKey());
				builder.addBinaryBody("files", file, ContentType.MULTIPART_FORM_DATA, m.getKey());
			}

			// 4.设置参数信息
			ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
			for (Map.Entry<String, String> param : params.entrySet()) {
				builder.addTextBody(param.getKey(), param.getValue(), contentType);
			}
			HttpEntity entity = builder.build();
			httpPost.setEntity(entity);

			// 5.设置头信息
			if (headers != null) {
				for (String key : headers.keySet()) {
					httpPost.setHeader(key, headers.get(key));
				}
			}

			// 6.执行http请求
			response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				resultString = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
			}
		} catch (Exception e) {
			logger.error("上传文件失败：", e);
		} finally {
			try {
				if (null != httpClient) {
					httpClient.close();
				}
			} catch (IOException e) {
				logger.error("关闭httpClient异常" + e.toString(), e);
			}
			try {
				if (null != content) {
					content.close();
				}
			} catch (IOException e) {
				logger.error("关闭content异常" + e.toString(), e);
			}
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				logger.error("关闭in异常" + e.toString(), e);
			}
		}

		return resultString;
	}
}
