package com.thinkingcloud.tools.js.runner.core.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Scriptable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Module;
import com.thinkingcloud.tools.js.runner.core.meta.Parameter;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service("doc")
@Module(doc = "The document utils.")
public class DocUtils {

	@Autowired
	private Configuration freemarker;

	public String genFunctionDoc(Map<String, Object> data) throws IOException, TemplateException {
		Template template = freemarker.getTemplate("classpath:templates/function_doc.ftl");
		StringWriter out = new StringWriter();
		template.process(data, out);
		out.flush();
		return out.toString();
	}

	@Function(parameters = @Parameter(name = "object", doc = "scriptable"), doc = "Generate the function's documentation.")
	public String functionDoc(Scriptable scriptable) throws IOException, TemplateException {
		Map<String, Object> data = new HashMap<String, Object>();
		for (Object key : scriptable.getIds()) {
			data.put(String.valueOf(key), scriptable.get(String.valueOf(key), scriptable));
		}
		return genFunctionDoc(data);
	}

	public Map<String, Object> generateModuleData(Class<?> clazz) {
		Map<String, Object> map = new HashMap<String, Object>();
		Module p = clazz.getAnnotation(Module.class);
		if (p != null) {
			Service s = clazz.getAnnotation(Service.class);
			map.put("serviceName", s.value());
			map.put("doc", p.doc());
			List<Map<String, Object>> functions = new ArrayList<Map<String, Object>>();
			map.put("functions", functions);
			for (Method method : clazz.getMethods()) {
				Function f = method.getAnnotation(Function.class);
				if (f != null) {
					functions.add(functionDataInner(method.getName(), f));
				}
			}
		}
		return map;
	}

	public Map<String, Object> generateFunctionData(Class<?> clazz, String functionName) {
		return functionDataInner(functionName, clazz.getAnnotation(Function.class));
	}

	private Map<String, Object> functionDataInner(String functionName, Function f) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (f != null) {
			map.put("functionName", functionName);
			map.put("doc", f.doc());
			map.put("returns", f.returns());
			ArrayList<Map<String, Object>> parameters = new ArrayList<Map<String, Object>>();
			map.put("parameters", parameters);
			if (f.parameters().length > 0) {
				for (Parameter p : f.parameters()) {
					Map<String, Object> parameter = new HashMap<String, Object>();
					parameter.put("name", p.name());
					if (p.multi())
						parameter.put("multi", p.multi());
					parameter.put("type", p.type());
					parameter.put("doc", p.doc());
					parameters.add(parameter);
				}
			}
		}
		return map;
	}

	public String serviceDoc(Map<String, Object> data) throws IOException, TemplateException {
		Template template = freemarker.getTemplate("classpath:templates/service_doc.ftl");
		StringWriter out = new StringWriter();
		template.process(data, out);
		out.flush();
		return out.toString();
	}

	public String functionDoc(Class<?> clazz, String functionName) throws IOException, TemplateException {
		return genFunctionDoc(generateFunctionData(clazz, functionName));
	}
}