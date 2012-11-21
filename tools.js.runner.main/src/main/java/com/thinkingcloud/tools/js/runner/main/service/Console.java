package com.thinkingcloud.tools.js.runner.main.service;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.main.NewGlobal;

@Service("console")
public class Console {

	private static final Logger logger = NewGlobal.logger;

	public void log(Object o) {
		logger.info("{}", o);
	}

	public void print(Object o) {
		System.out.println(String.valueOf(o));
	}

	public void logf(String format, Object... args) {
		MessageFormat f = new MessageFormat(format);
		log(f.format(args));
	}

	public void info(String message, Object... args) {
		logger.info(message, args);
	}

	public void debug(String message, Object... args) {
		logger.debug(message, args);
	}

	public void warn(String message, Object... args) {
		logger.warn(message, args);
	}

	public void trace(String message, Object... args) {
		logger.trace(message, args);
	}

	public void error(String message, Object... args) {
		logger.error(message, args);
	}

	@SuppressWarnings("rawtypes")
	public void dir(Object o) {
		StringBuilder sb = new StringBuilder();
		if (o instanceof List) {
			sb.append(Arrays.toString(((List) o).toArray()));
		} else if (o instanceof Scriptable) {
			sb.append("{\n");
			Scriptable script = (Scriptable) o;
			boolean first = true;
			for (Object id : script.getIds()) {
				if (first) {
					first = false;
				} else {
					sb.append(",\n");
				}
				sb.append("\t").append(id).append(":");
				if (id instanceof Integer) {
					sb.append(script.get((Integer) id, script));
				} else {
					sb.append(script.get(String.valueOf(id), script));
				}
			}
			sb.append("\n}");
		} else {
			sb.append(ToStringBuilder.reflectionToString(o));
		}
		print(sb);
	}
}
