package info.thinkingcloud.jersey.core.utils;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import freemarker.template.TemplateException;

public class BaseService extends MessageSupport {
	@Autowired
	private DocUtils doc;

	public String doc() throws IOException, TemplateException {
		return doc.serviceDoc(doc.generateModuleData(getClass()));
	}
}
