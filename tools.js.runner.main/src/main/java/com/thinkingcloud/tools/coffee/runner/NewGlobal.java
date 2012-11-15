package com.thinkingcloud.tools.coffee.runner;

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

	private static Logger logger = LoggerFactory.getLogger("com.thinkingcloud.tools.coffee.runner");

	@PostConstruct
	public void init() {
		defineProperty("logger", logger, ScriptableObject.DONTENUM);
		for (Map.Entry<String, BaseFunction> e : context.getBeansOfType(BaseFunction.class).entrySet()) {
			defineProperty(e.getKey(), e.getValue(), ScriptableObject.DONTENUM);
		}
	}
}
