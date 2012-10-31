package com.thinkingcloud.tools.js.runner.application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaMethod;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.thinkingcloud.tools.js.runner.application.service.DBService;
import com.thinkingcloud.tools.js.runner.application.service.SolrService;
import com.thinkingcloud.tools.js.runner.application.service.StringUtilsService;
import com.thinkingcloud.tools.js.runner.application.service.UtilService;

public class MainApplication {
	private static Logger logger = LoggerFactory.getLogger(MainApplication.class);

	public static void main(String[] args) throws FileNotFoundException, IOException, SecurityException,
	        NoSuchMethodException {
		System.setOut(new PrintStream(System.out, true, "UTF-8"));
		if (args.length != 1) {
			logger.error("Need only 1 script file to run.");
		} else {
			ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("application_context.xml");
			Context context = Context.enter();
			context.setOptimizationLevel(-1);
			Scriptable scope = context.initStandardObjects();
			scope.put("logger", scope, Context.javaToJS(logger, scope));
			scope.put("println", scope,
			        new NativeJavaMethod(System.out.getClass().getMethod("println", new Class<?>[] { Object.class }),
			                "println"));
			scope.put("context", scope, Context.javaToJS(appContext, scope));

			UtilService utils = appContext.getBean(UtilService.class);
			utils.scope = scope;
			StringUtilsService sutils = appContext.getBean(StringUtilsService.class);
			DBService db = appContext.getBean(DBService.class);
			SolrService solr = appContext.getBean(SolrService.class);
			scope.put("utils", scope, Context.javaToJS(utils, scope));
			scope.put("sutils", scope, Context.javaToJS(sutils, scope));
			scope.put("db", scope, Context.javaToJS(db, scope));
			scope.put("solr", scope, solr);
			context.evaluateReader(scope, new InputStreamReader(Thread.currentThread().getContextClassLoader()
			        .getResourceAsStream("scripts/init.js")), "init.js", 1, null);
			context.evaluateReader(scope, new FileReader(args[0]), args[0], 1, null);
		}
	}
}
