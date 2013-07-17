package info.thinkingcloud.jersey.solr;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.meta.Parameter;
import info.thinkingcloud.jersey.core.utils.BaseService;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
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
@Module(doc = "The solr module.")
public class SolrService extends BaseService {

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

	public SolrDocumentList query(String query, int start, int count) throws SolrServerException {
		SolrQuery q = new SolrQuery(query);
		q.setStart(start);
		q.setRows(count);
		return solr.query(q).getResults();
	}

	@Function(doc = "Query for the solr documents", parameters = @Parameter(name = "query", type = "string", doc = "The solr query"))
	public SolrDocument[] queryForArray(String query) throws SolrServerException {
		return query(query).toArray(new SolrDocument[0]);
	}

	public SolrDocument[] queryForArray(String query, int start, int count) throws SolrServerException {
		return query(query, start, count).toArray(new SolrDocument[0]);
	}

	@Function(doc = "Convert the solr doc to solr input doc.", parameters = @Parameter(name = "doc", type = "solr", doc = "The doc to convert."), returns = "The solr input document")
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

	@Function(doc = "Remove the data from solr using the id", parameters = @Parameter(name = "id", type = "string", doc = "The data's id in solr."))
	public void remove(String id) throws SolrServerException, IOException {
		solr.deleteById(id);
	}

	@Function(doc = "Added the data into solr.", parameters = @Parameter(type = "object", doc = "The data to add to solr.", name = "doc"))
	public void add(Map<String, Object> doc) throws SolrServerException, IOException {
		add(createDoc(doc));
	}

	public void add(SolrInputDocument doc) throws SolrServerException, IOException {
		solr.add(doc);
	}

	@Function(doc = "Rollback the solr session!")
	public void rollback() throws SolrServerException, IOException {
		solr.rollback();
	}

	@Function(doc = "Commit the solr data.")
	public void commit() throws SolrServerException, IOException {
		solr.commit();
	}
}