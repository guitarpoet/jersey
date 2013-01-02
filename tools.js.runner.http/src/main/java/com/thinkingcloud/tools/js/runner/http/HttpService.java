package com.thinkingcloud.tools.js.runner.http;

import java.io.File;
import java.io.FileWriter;
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

import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Module;
import com.thinkingcloud.tools.js.runner.core.meta.Parameter;
import com.thinkingcloud.tools.js.runner.core.utils.BaseService;

@Service("http")
@Module(doc = "The http service module.")
public class HttpService extends BaseService {
	public final String safari = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_4) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2";

	private Logger logger = LoggerFactory.getLogger(HttpService.class);

	public Object posta;

	public Object geta;

	private String defaultCharset = "gbk";

	/**
	 * @return the defaultCharset
	 */
	public String getDefaultCharset() {
		return defaultCharset;
	}

	/**
	 * @param defaultCharset
	 *            the defaultCharset to set
	 */
	public void setDefaultCharset(String defaultCharset) {
		this.defaultCharset = defaultCharset;
	}

	@Autowired
	private HttpClient client;

	@Function(doc = "The default http headers.")
	public Map<String, String> defaultHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("User-Agent", safari);
		headers.put("Accept-Language", "zh,en-us;q=0.7,en;q=0.3");
		return headers;
	}

	@Function(doc = "Get response using http post.", parameters = {
	        @Parameter(name = "url", type = "string", doc = "The post url"),
	        @Parameter(name = "headers", optional = true, doc = "the headers for the requrest", type = "map"),
	        @Parameter(name = "data", type = "map", optional = true, doc = "The post data") }, returns = "The result string.")
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
			return EntityUtils.toString(response.getEntity(), defaultCharset);
		} finally {
			EntityUtils.consume(response.getEntity());
		}
	}

	@Function(doc = "Downlod the file, keep the original name", parameters = {
	        @Parameter(name = "url", type = "string", doc = "The url of this document"),
	        @Parameter(name = "basePath", type = "string", doc = "The base path of this document") })
	public void download(String url, String basePath) throws IOException, HttpException {
		String fileName = url.substring(url.lastIndexOf("/") + 1);
		File out = new File(basePath + "/" + fileName);
		if (!out.exists())
			out.createNewFile();
		FileWriter writer = new FileWriter(out);
		writer.write(get(url));
		writer.flush();
		writer.close();
	}

	@Function(doc = "Get response using http get.", parameters = {
	        @Parameter(name = "url", type = "string", doc = "The get url"),
	        @Parameter(name = "headers", optional = true, doc = "the headers for the requrest", type = "map") }, returns = "The result string.")
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
			        || (!response.getEntity().getContentType().getValue().contains("text/html")
			                && !response.getEntity().getContentType().getValue().contains("json") && !response
			                .getEntity().getContentType().getValue().contains("Chemical/MolFile"))) {
				return null;
			}
			logger.info("Decoding using default charset {}", defaultCharset);
			return EntityUtils.toString(response.getEntity(), defaultCharset);
		} finally {
			EntityUtils.consume(response.getEntity());
		}
	}

	public String get(String url) throws HttpException, IOException {
		return get(url, null);
	}
}