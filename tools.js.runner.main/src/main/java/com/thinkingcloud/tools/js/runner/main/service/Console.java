package com.thinkingcloud.tools.js.runner.main.service;

import java.text.MessageFormat;
import java.util.List;

import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.main.NewGlobal;
import com.thinkingcloud.tools.js.runner.main.utils.meta.Plugin;

@Service("console")
@Plugin(doc = "The console object")
public class Console extends BaseService {

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
	public String inspect(Object o, int indentLevel) {
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
		} else if (o instanceof NativeJavaObject) {
			sb.append(((NativeJavaObject) o).unwrap());
		}
		return sb.toString();
	}

	public void dir(Object o) {
		print(inspect(o, 0));
	}
}
