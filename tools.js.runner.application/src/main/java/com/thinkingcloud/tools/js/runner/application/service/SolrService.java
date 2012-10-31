package com.thinkingcloud.tools.js.runner.application.service;

import java.io.IOException;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolrService {

	@Autowired
	private SolrServer solr;

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

	public void add(SolrInputDocument doc) throws SolrServerException, IOException {
		solr.add(doc);
	}

	public void commit() throws SolrServerException, IOException {
		solr.commit();
	}
}