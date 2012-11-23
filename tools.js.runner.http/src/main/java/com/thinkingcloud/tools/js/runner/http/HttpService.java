package com.thinkingcloud.tools.js.runner.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("http")
public class HttpService {
	public final String safari = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_4) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2";

	private Logger logger = LoggerFactory.getLogger(HttpService.class);

	public Object posta;

	public Object geta;

	@Autowired
	private HttpClient client;

	public Map<String, String> defaultHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("User-Agent", safari);
		headers.put("Accept-Language", "zh,en-us;q=0.7,en;q=0.3");
		return headers;
	}

	public String post(String url, Map<String, Object> datas) throws ClientProtocolException, IOException {
		return post(url, null, datas);
	}

	public String post(String url, Map<String, String> headers, Map<String, Object> datas)
	        throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost(url);
		HttpResponse response = null;
		try {
			if (headers != null) {
				for (Map.Entry<String, String> e : headers.entrySet()) {
					post.addHeader(e.getKey(), e.getValue());
				}
			}
			if (datas != null) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				for (Map.Entry<String, Object> e : datas.entrySet()) {
					params.add(new BasicNameValuePair(e.getKey(), String.valueOf(e.getValue())));
				}
				post.setEntity(new UrlEncodedFormEntity(params));
			}
			response = client.execute(post);
			if (response.getEntity().getContentType() == null
			        || (!response.getEntity().getContentType().getValue().contains("text") && !response.getEntity()
			                .getContentType().getValue().contains("json"))) {
				return null;
			}
			return EntityUtils.toString(response.getEntity());
		} finally {
			EntityUtils.consume(response.getEntity());
		}
	}

	public String get(String url, Map<String, String> headers) throws HttpException, IOException {
		logger.info("Ready to get {}", url);
		HttpGet get = new HttpGet(url);
		HttpResponse response = null;
		try {
			if (headers != null) {
				for (Map.Entry<String, String> e : headers.entrySet()) {
					get.addHeader(e.getKey(), e.getValue());
				}
			}
			response = client.execute(get);
			if (response.getEntity().getContentType() == null
			        || (!response.getEntity().getContentType().getValue().contains("text/html") && !response
			                .getEntity().getContentType().getValue().contains("json"))) {
				return null;
			}
			return EntityUtils.toString(response.getEntity());
		} finally {
			EntityUtils.consume(response.getEntity());
		}
	}

	public String get(String url) throws HttpException, IOException {
		return get(url, null);
	}
}