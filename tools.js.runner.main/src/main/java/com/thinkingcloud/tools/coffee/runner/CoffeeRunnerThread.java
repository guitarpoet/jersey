package com.thinkingcloud.tools.coffee.runner;

import java.io.File;
import java.io.FileReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.mozilla.javascript.tools.shell.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class CoffeeRunnerThread {

	private static Logger logger = LoggerFactory.getLogger(CoffeeRunnerThread.class);

	@Autowired
	private ConfigurableApplicationContext context;

	private String[] args;

	@Autowired
	private NewGlobal global;

	/**
	 * @return the args
	 */
	public String[] getArgs() {
		return args;
	}

	/**
	 * @param args
	 *            the args to set
	 */
	public void setArgs(String[] args) {
		this.args = args;
	}

	public void run() {
		try {
			GnuParser parser = new GnuParser();
			Options opts = new Options();
			opts.addOption("c", "config", true, "The configuration of this application.");

			CommandLine cmd = parser.parse(opts, args);

			String config = "config.properties";
			if (cmd.hasOption("c")) {
				config = cmd.getOptionValue("c");
			}

			File f = new File(config);
			if (f.exists()) {
				System.getProperties().load(new FileReader(f));
			} else {
				logger.warn("The configuration file {} is not existed.", f);
			}
			Main.global = global;
			Main.setErr(System.err);
			Main.setOut(System.out);
			Main.setIn(System.in);
			global.put("context", global, context);
			Main.main(cmd.getArgs());
			if (context.isRunning())
				context.close();
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
		}
	}
}
