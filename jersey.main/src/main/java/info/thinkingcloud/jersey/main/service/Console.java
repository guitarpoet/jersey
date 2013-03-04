package info.thinkingcloud.jersey.main.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import info.thinkingcloud.jersey.core.NewGlobal;
import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.meta.Parameter;
import info.thinkingcloud.jersey.core.utils.BaseService;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("console")
@Module(doc = "The console object")
public class Console extends BaseService {

	private static final Logger logger = NewGlobal.logger;

	private Map<String, Long> timestamps = new HashMap<String, Long>();

	@Autowired
	private Configuration freemarker;

	@Function(doc = "Set the begin timestamp", parameters = @Parameter(name = "name", type = "string", optional = true, doc = "The timestamp's name"))
	public void time(String name) {
		timestamps.put(name, System.currentTimeMillis());
	}

	@Function(doc = "List all the config into 1 string", returns = "The config string")
	public String listConfig() {
		StringWriter writer = new StringWriter();
		System.getProperties().list(new PrintWriter(writer));
		writer.flush();
		return writer.toString();
	}

	public void time() {
		time("--main--");
	}

	public long timeEnd() {
		return timeEnd("--main--");
	}

	@Function(doc = "End the timer that counts timestamp", parameters = { @Parameter(name = "name", type = "string", optional = true, doc = "The name for the timer") })
	public long timeEnd(String name) {
		if (timestamps.containsKey(name)) {
			long begin = timestamps.get(name);
			long ret = System.currentTimeMillis() - begin;
			logger.info("{}: {}ms", name, ret);
			timestamps.remove(name);
			return ret;
		}
		return 0;
	}

	@Function(doc = "Log to stdout", parameters = @Parameter(name = "o", doc = "The object to log", type = "object"))
	public void log(Object o) {
		logger.info("{}", o);
	}

	@Function(doc = "Println using the object.", parameters = @Parameter(name = "o", doc = "The obejct to print.", type = "object"))
	public void print(Object o) {
		System.out.println(String.valueOf(o));
	}

	@Function(doc = "Log using the message format", parameters = {
	        @Parameter(name = "format", type = "string", doc = "The string format"),
	        @Parameter(name = "args", multi = true, type = "object", doc = "The args in the format") })
	public void logf(String format, Object... args) {
		MessageFormat f = new MessageFormat(format);
		log(f.format(args));
	}

	@Function(doc = "Log info level log using the message format", parameters = {
	        @Parameter(name = "format", type = "string", doc = "The string format"),
	        @Parameter(name = "args", multi = true, type = "object", doc = "The args in the format") })
	public void info(String message, Object... args) {
		logger.info(message, args);
	}

	@Function(doc = "Log debug level log using the message format", parameters = {
	        @Parameter(name = "format", type = "string", doc = "The string format"),
	        @Parameter(name = "args", multi = true, type = "object", doc = "The args in the format") })
	public void debug(String message, Object... args) {
		logger.debug(message, args);
	}

	@Function(doc = "Log warn level log using the message format", parameters = {
	        @Parameter(name = "format", type = "string", doc = "The string format"),
	        @Parameter(name = "args", multi = true, type = "object", doc = "The args in the format") })
	public void warn(String message, Object... args) {
		logger.warn(message, args);
	}

	@Function(doc = "Log trace level log using the message format", parameters = {
	        @Parameter(name = "format", type = "string", doc = "The string format"),
	        @Parameter(name = "args", multi = true, type = "object", doc = "The args in the format") })
	public void trace(String message, Object... args) {
		logger.trace(message, args);
	}

	@Function(doc = "Log error level log using the message format", parameters = {
	        @Parameter(name = "format", type = "string", doc = "The string format"),
	        @Parameter(name = "args", multi = true, type = "object", doc = "The args in the format") })
	public void error(String message, Object... args) {
		logger.error(message, args);
	}

	@SuppressWarnings("rawtypes")
	@Function(doc = "Inspect the object.", parameters = {
	        @Parameter(name = "o", type = "object", doc = "The object that need inspect."),
	        @Parameter(name = "indentLevel", type = "int", doc = "The indent level for this inspect, using for beautify.") })
	public String inspect(Object o, int indentLevel) throws IOException, TemplateException {
		StringBuilder sb = new StringBuilder();
		if (o instanceof List) {
			sb.append("[\n");
			boolean first = true;
			for (Object item : (List) o) {
				if (first)
					first = false;
				else
					sb.append(",");
				sb.append(inspect(item, 0));
			}
			sb.append("]\n");
		} else if (o instanceof Scriptable && !(o instanceof NativeJavaObject)) {
			sb.append("{\n");
			Scriptable script = (Scriptable) o;
			boolean first = true;
			for (Object id : script.getIds()) {
				if (first) {
					first = false;
				} else {
					sb.append(",\n");
				}
				for (int i = 0; i <= indentLevel; i++)
					sb.append("\t");

				sb.append(id).append(":");
				if (id instanceof Integer) {
					sb.append(script.get((Integer) id, script));
				} else {
					sb.append(inspect(script.get(String.valueOf(id), script), indentLevel + 1));
				}
			}
			sb.append("\n");
			for (int i = 0; i < indentLevel; i++)
				sb.append("\t");
			sb.append("}");
		} else if (o instanceof String) {
			sb.append("\"").append(o).append("\"");
		} else if (o instanceof NativeJavaObject && ((NativeJavaObject) o).unwrap() instanceof String) {
			sb.append("\"").append(((NativeJavaObject) o).unwrap()).append("\"");
		} else {
			Object obj = o;
			if (o instanceof NativeJavaObject) {
				obj = ((NativeJavaObject) o).unwrap();
			}
			Template template = freemarker.getTemplate("classpath:templates/inspect.ftl");
			HashMap<String, Object> datas = new HashMap<String, Object>();
			datas.put("class", obj.getClass().getName());
			datas.put("fields", obj.getClass().getFields());
			datas.put("methods", obj.getClass().getMethods());
			datas.put("obj", obj);
			StringWriter writer = new StringWriter();
			template.process(datas, writer);
			sb.append(writer.toString());
		}
		return sb.toString();
	}

	@Function(doc = "Dump the object into stdout.", parameters = { @Parameter(name = "o", type = "object", doc = "The object that need inspect.") })
	public void dir(Object o) throws IOException, TemplateException {
		print(inspect(o, 0));
	}
}
