package info.thinkingcloud.jersey.core;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.tools.shell.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class NewGlobal extends Global {

	private static final long serialVersionUID = -1941559911033392335L;

	@Autowired
	private ApplicationContext context;

	public static final Logger logger = LoggerFactory.getLogger("info.thinkingcloud.jersey");

	public void init(ApplicationContext context) {
		defineProperty("logger", logger, ScriptableObject.DONTENUM);
		defineProperty("__dirname", System.getProperties().get("user.dir"), ScriptableObject.DONTENUM);
		for (Map.Entry<String, BaseFunction> e : context.getBeansOfType(BaseFunction.class).entrySet()) {
			put(e.getKey(), e.getValue(), this);
		}

		for (Map.Entry<String, Object> e : context.getBeansWithAnnotation(Service.class).entrySet()) {
			defineProperty(e.getKey(), e.getValue(), ScriptableObject.DONTENUM);
		}
	}

	@PostConstruct
	public void init() {
		init(context);
	}
}
