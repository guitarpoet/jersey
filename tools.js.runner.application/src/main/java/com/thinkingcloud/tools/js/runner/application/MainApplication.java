package com.thinkingcloud.tools.js.runner.application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaMethod;
import org.mozilla.javascript.tools.shell.Global;
import org.mozilla.javascript.tools.shell.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.thinkingcloud.tools.js.runner.application.service.DBService;
import com.thinkingcloud.tools.js.runner.application.service.FileWriterService;
import com.thinkingcloud.tools.js.runner.application.service.SolrService;
import com.thinkingcloud.tools.js.runner.application.service.StringUtilsService;
import com.thinkingcloud.tools.js.runner.application.service.UtilService;

public class MainApplication {
	private static Logger logger = LoggerFactory.getLogger(MainApplication.class);

	public static void main(String[] args) throws FileNotFoundException, IOException, SecurityException,
	        NoSuchMethodException, ParseException {
		System.setOut(new PrintStream(System.out, true, "UTF-8"));
		GnuParser parser = new GnuParser();
		Options ops = new Options();
		ops.addOption("c", "config", true, "The configuration file location.");
		ops.addOption("a", "application", true, "Other spring configuration file location.");
		CommandLine cmd = parser.parse(ops, args);
		if (cmd.hasOption("c")) {
			Properties p = new Properties();
			p.load(new FileReader(cmd.getOptionValue("c")));
			System.getProperties().putAll(p);
		}

		ClassPathXmlApplicationContext appContext = null;
		if (!cmd.hasOption("a")) {
			appContext = new ClassPathXmlApplicationContext("application_context.xml");
		} else {
			appContext = new ClassPathXmlApplicationContext("application_context.xml", cmd.getOptionValue("a"));
		}
		if (cmd.getArgList().isEmpty()) {
			logger.error("You need at least 1 script to run.");
			System.exit(0);
		} else {
			Context context = Context.enter();
			context.setOptimizationLevel(-1);
			context.setLanguageVersion(Context.VERSION_1_5);
			Global g = Main.getGlobal();
			g.init(context);
			g.put("logger", g, Context.javaToJS(logger, g));
			g.put("println", g,
			        new NativeJavaMethod(System.out.getClass().getMethod("println", new Class<?>[] { Object.class }),
			                "println"));
			g.put("context", g, Context.javaToJS(appContext, g));

			UtilService utils = appContext.getBean(UtilService.class);
			utils.scope = g;
			StringUtilsService sutils = appContext.getBean(StringUtilsService.class);
			DBService db = appContext.getBean(DBService.class);
			SolrService solr = appContext.getBean(SolrService.class);
			FileWriterService file = appContext.getBean(FileWriterService.class);
			g.put("utils", g, Context.javaToJS(utils, g));
			g.put("sutils", g, Context.javaToJS(sutils, g));
			g.put("db", g, Context.javaToJS(db, g));
			g.put("solr", g, Context.javaToJS(solr, g));
			g.put("file", g, Context.javaToJS(file, g));
			context.evaluateReader(g, new InputStreamReader(Thread.currentThread().getContextClassLoader()
			        .getResourceAsStream("scripts/init.js")), "init.js", 1, null);

			context.evaluateReader(g, new FileReader(cmd.getArgs()[0]), cmd.getArgs()[0], 1, null);
		}
	}
}
