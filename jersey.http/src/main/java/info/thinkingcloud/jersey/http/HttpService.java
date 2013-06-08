package info.thinkingcloud.jersey.http;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.meta.Parameter;
import info.thinkingcloud.jersey.core.utils.BaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("http")
@Module(doc = "The http service module.")
public class HttpService extends BaseService {
	public final String safari = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_4) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2";

	private Logger logger = LoggerFactory.getLogger(HttpService.class);

	public Object posta;

	public Object geta;

	private String defaultCharset = "gbk";

	@Function(doc = "Setting the http proxy of the client.", parameters = {
	        @Parameter(name = "host", type = "string", doc = "The proxy host"),
	        @Parameter(name = "port", type = "int", doc = "The port of this proxy") })
	public void setProxy(String host, double port) {
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost(host, (int) port));
	}

	@Function(doc = "Remove the proxy that we have set.")
	public void removeProxy() {
		client.getParams().removeParameter(ConnRoutePNames.DEFAULT_PROXY);
	}

	@Function(doc = "Setting the timeout for the http", parameters = @Parameter(name = "timeout", doc = "The timeout parameter, units in millisecondes."))
	public void setTimeout(int timeout) {
		HttpConnectionParams.setConnectionTimeout(client.getParams(), timeout);
		HttpConnectionParams.setSoTimeout(client.getParams(), timeout);
	}

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

	public void put(String url, Map<String, Object> headers) throws IOException {
		put(url, headers, null);
	}

	public void put(String url) throws IOException {
		put(url, null, null);
	}

	@Function(doc = "Executing http put method", parameters = {
	        @Parameter(name = "url", type = "string", doc = "The url to execute the http put"),
	        @Parameter(name = "headers", type = "object", optional = true, doc = "The headers for http put"),
	        @Parameter(name = "file", type = "stream", optional = true, doc = "The file stream to put.") })
	public void put(String url, Map<String, Object> headers, InputStream file) throws IOException {
		HttpPut put = new HttpPut(url);
		if (headers != null) {
			for (Map.Entry<String, Object> e : headers.entrySet()) {
				put.addHeader(e.getKey(), String.valueOf(e.getValue()));
			}
		}
		if (file != null)
			put.setEntity(new InputStreamEntity(file, file.available()));
		try {
			HttpResponse response = client.execute(put);
			if (response.getStatusLine().getStatusCode() != 200) {
				HttpEntity e = response.getEntity();
				String message = EntityUtils.toString(e);
				EntityUtils.consume(e);
				throw new IOException(message);
			}
			System.out.println(response);
		} finally {
			put.releaseConnection();
		}
	}

	public void del(String url) throws ClientProtocolException, IOException {
		del(url, null);
	}

	@Function(doc = "Executing the http delete method", parameters = {
	        @Parameter(name = "url", type = "string", doc = "The url to execute the http delete method"),
	        @Parameter(name = "headers", optional = true, type = "object", doc = "The headers for http delete") })
	public void del(String url, Map<String, Object> headers) throws ClientProtocolException, IOException {
		HttpDelete delete = new HttpDelete(url);
		if (headers != null) {
			for (Map.Entry<String, Object> e : headers.entrySet()) {
				delete.addHeader(e.getKey(), String.valueOf(e.getValue()));
			}
		}
		try {
			client.execute(delete);
		} finally {
			delete.releaseConnection();
		}
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
		HttpResponse response = null;
		try {
			response = postResponse(url, headers, datas);
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
	        @Parameter(name = "basePath", type = "string", doc = "The base path of this document") }, returns = "The result that downloaded")
	public String download(String url, String basePath) throws IOException, HttpException {
		String fileName = url.substring(url.lastIndexOf("/") + 1);

		String data = get(url);
		if (data != null) {
			File out = new File(basePath + "/" + fileName);
			if (!out.exists())
				out.createNewFile();
			FileWriter writer = new FileWriter(out);
			writer.write(data);
			writer.flush();
			writer.close();
		}

		return data;
	}

	@Function(doc = "Post to get the raw http reponse object wrapper", parameters = {
	        @Parameter(name = "url", type = "string", doc = "The url to send http post"),
	        @Parameter(name = "header", type = "object", doc = "The headers for http post."),
	        @Parameter(name = "datas", type = "object", doc = "The data to post.") })
	public HttpResponse postResponse(String url, Map<String, String> headers, Map<String, Object> datas)
	        throws ClientProtocolException, IOException {
		logger.info("Ready to post {} using headers {} and data {}", new Object[] { url, headers, datas });
		HttpPost post = new HttpPost(url);
		HttpResponse response = null;

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
		return response;
	}

	@Function(doc = "Get the raw http reponse object wrapper", parameters = {
	        @Parameter(name = "url", type = "string", doc = "The url to send http get"),
	        @Parameter(name = "header", type = "object", doc = "The headers for http get.") })
	public HttpResponse getResponse(String url, Map<String, String> headers) throws ClientProtocolException,
	        IOException {
		logger.info("Ready to get {}", url);
		HttpGet get = new HttpGet(url);
		HttpResponse response = null;
		if (headers != null) {
			for (Map.Entry<String, String> e : headers.entrySet()) {
				get.addHeader(e.getKey(), e.getValue());
			}
		}
		response = client.execute(get);
		return response;
	}

	@Function(doc = "Execute the http head method", parameters = {
	        @Parameter(name = "url", type = "string", doc = "The url to get the http head"),
	        @Parameter(name = "headers", type = "object", doc = "The http headers.") })
	public HttpResponse head(String url, Map<String, String> headers) throws ClientProtocolException, IOException {
		HttpHead head = new HttpHead(url);
		if (headers != null) {
			for (Map.Entry<String, String> e : headers.entrySet()) {
				head.addHeader(e.getKey(), e.getValue());
			}
		}
		HttpResponse response = client.execute(head);
		return response;
	}

	@Function(doc = "Get response using http get.", parameters = {
	        @Parameter(name = "url", type = "string", doc = "The get url"),
	        @Parameter(name = "headers", optional = true, doc = "the headers for the requrest", type = "map") }, returns = "The result string.")
	public String get(String url, Map<String, String> headers) throws HttpException, IOException {
		HttpResponse response = null;
		try {
			response = getResponse(url, headers);
			if (response.getEntity().getContentType() == null) {
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