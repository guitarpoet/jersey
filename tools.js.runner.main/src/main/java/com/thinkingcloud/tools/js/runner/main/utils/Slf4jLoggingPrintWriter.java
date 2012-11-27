package com.thinkingcloud.tools.js.runner.main.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jLoggingPrintWriter extends PrintWriter {

	private static final Logger logger = LoggerFactory
			.getLogger(Slf4jLoggingPrintWriter.class);

	public Slf4jLoggingPrintWriter() throws IOException {
		super(new StringWriter());
	}

	@Override
	public void println(boolean x) {
		logger.error(String.valueOf(x));
	}

	@Override
	public void println(char x) {
		logger.error(String.valueOf(x));
	}

	@Override
	public void println(double x) {
		logger.error(String.valueOf(x));
	}

	@Override
	public void println(float x) {
		logger.error(String.valueOf(x));
	}

	@Override
	public void println(Object x) {
		logger.error(String.valueOf(x));
	}

	@Override
	public void println(String x) {
		logger.error(x);
	}
}
