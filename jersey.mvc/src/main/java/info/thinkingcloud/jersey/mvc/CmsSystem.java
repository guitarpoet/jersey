package info.thinkingcloud.jersey.mvc;

import info.thinkingcloud.jersey.core.NewGlobal;
import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.meta.Parameter;
import info.thinkingcloud.jersey.core.utils.BaseService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;

import javax.annotation.PostConstruct;

import org.mozilla.javascript.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service("cms")
@Module(doc = "The cms system.")
public class CmsSystem extends BaseService {

	private static Logger logger = LoggerFactory.getLogger(CmsSystem.class);

	@Autowired
	private Configuration freemarker;

	@Autowired
	private NewGlobal global;

	@Autowired
	private ResourceLoader resourceLoader;

	public String theme = "default";

	public String themeDir = "themes/";

	public String prefix = "modules/";

	public String suffix = ".ftl";

	@PostConstruct
	public void init() {
		freemarker.setCustomAttribute("cms", this);
	}

	public String dump(Object o) {
		return o.toString();
	}

	public String module(String name, Object model) throws IOException {
		Context c = Context.enter();
		Resource r = resourceLoader.getResource(MessageFormat.format("classpath:modules/{0}/{0}.js", name));
		logger.info("Resource for {} is {}", name, r);
		if (r != null && r.getInputStream() != null) {
			return String.valueOf(c.evaluateReader(global, new InputStreamReader(r.getInputStream()), name, 1, null));
		}
		return null;
	}

	public String stackTrace(Exception ex) {
		logger.error("", ex);
		StringWriter writer = new StringWriter();
		ex.printStackTrace(new PrintWriter(writer));
		writer.flush();
		return writer.toString();
	}

	@Function(doc = "Process the module's template view", parameters = {
	        @Parameter(name = "view", type = "string", doc = "The view's location"),
	        @Parameter(name = "model", type = "string", doc = "The template processing model.") })
	public String moduleView(String view, Object model) throws IOException, TemplateException {
		StringWriter out = new StringWriter();
		Template template = null;
		try {
			template = freemarker.getTemplate(prefix + view + suffix);
		} catch (IOException ex) {
			template = freemarker.getTemplate(themeDir + theme + "/" + prefix + view + suffix);
		}
		if (template != null) {
			template.process(model, out);
			out.flush();
			return out.toString();
		}
		return null;
	}
}