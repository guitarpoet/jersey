package com.thinkingcloud.tools.js.runner.main;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.tools.shell.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.NewGlobal;
import com.thinkingcloud.tools.js.runner.main.service.StringService;

@Service
public class JSRunnerThread {

	private static final Logger logger = LoggerFactory.getLogger(JSRunnerThread.class);

	@Autowired
	private ConfigurableApplicationContext context;

	@Autowired
	private StringService sutils;

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
			opts.addOption("s", "script", true, "The script to run");

			CommandLine cmd = parser.parse(opts, args);

			String config = "config.properties";
			if (cmd.hasOption("c")) {
				config = cmd.getOptionValue("c");
			}

			File f = new File(config);
			if (f.exists()) {
				System.getProperties().load(new FileReader(f));
			} else {
				logger.info("The configuration file {} is not existed, using default ones.", f);
			}
			Context c = Context.enter();

			c.setOptimizationLevel(-1);
			Main.global = global;
			Main.setErr(System.err);
			Main.setOut(System.out);
			Main.setIn(System.in);
			if (cmd.hasOption("s")) {
				String source = cmd.getOptionValue("s");
				if (!source.equals("-"))
					Main.setIn(new ByteArrayInputStream(source.getBytes()));
				Main.main(new String[0]);
				return;
			} else
				Main.main(cmd.getArgs());
			if (context.isRunning())
				context.close();
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
		}
	}
}
