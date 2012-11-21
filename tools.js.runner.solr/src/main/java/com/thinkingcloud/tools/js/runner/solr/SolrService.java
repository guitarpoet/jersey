package com.thinkingcloud.tools.js.runner.solr;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("solr")
public class SolrService {

	@Autowired
	private SolrServer solr;

	public void init() {
		HttpSolrServer server = (HttpSolrServer) solr;
		DefaultHttpClient client = (DefaultHttpClient) server.getHttpClient();
		client.addRequestInterceptor(new HttpRequestInterceptor() {
			@Override
			public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
				if (request instanceof UpdateRequest) {
					UpdateRequest r = (UpdateRequest) request;
					System.out.println(r.getXML());
				}
			}
		});

		client.addResponseInterceptor(new HttpResponseInterceptor() {
			@Override
			public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
				IOUtils.copy(response.getEntity().getContent(), System.out);
			}
		});
	}

	public String toXml(SolrInputDocument doc) {
		return ClientUtils.toXML(doc);
	}

	public String toXml(SolrDocument doc) {
		return toXml(ClientUtils.toSolrInputDocument(doc));
	}

	public SolrDocumentList query(String query) throws SolrServerException {
		SolrQuery q = new SolrQuery(query);
		return solr.query(q).getResults();
	}

	public SolrDocument[] queryForArray(String query) throws SolrServerException {
		return query(query).toArray(new SolrDocument[0]);
	}

	public SolrInputDocument convert(SolrDocument doc) {
		return ClientUtils.toSolrInputDocument(doc);
	}

	public SolrInputDocument createDoc() {
		return new SolrInputDocument();
	}

	public SolrInputDocument createDoc(Map<String, Object> data) {
		SolrInputDocument doc = createDoc();
		for (Map.Entry<String, Object> e : data.entrySet()) {
			doc.addField(e.getKey(), e.getValue());
		}
		return doc;
	}

	public void remove(String id) throws SolrServerException, IOException {
		solr.deleteById(id);
	}

	public void add(Map<String, Object> doc) throws SolrServerException, IOException {
		add(createDoc(doc));
	}

	public void add(SolrInputDocument doc) throws SolrServerException, IOException {
		solr.add(doc);
	}

	public void commit() throws SolrServerException, IOException {
		solr.commit();
	}
}