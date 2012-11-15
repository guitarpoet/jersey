package com.thinkingcloud.tools.js.runner.main.service;

import java.util.Arrays;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.tools.shell.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Job implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(Job.class);

	private String name;

	private Context context;

	private Object[] args;

	private BaseFunction function;

	private BaseFunction callback;

	public Job(String name, Context context, BaseFunction function, BaseFunction callback) {
		this(name, context, function, callback, new Object[0]);
	}

	public Job(String name, Context context, BaseFunction function) {
		this(name, context, function, null, new Object[0]);
	}

	public Job(String name, Context context, BaseFunction function, Object[] args) {
		this(name, context, function, null, args);
	}

	public Job(String name, Context context, BaseFunction function, BaseFunction callback, Object[] args) {
		this.name = name;
		this.context = context;
		this.function = function;
		this.args = args;
		this.callback = callback;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * @return the args
	 */
	public Object[] getArgs() {
		return args;
	}

	/**
	 * @param args
	 *            the args to set
	 */
	public void setArgs(Object[] args) {
		this.args = args;
	}

	/**
	 * @return the callback
	 */
	public BaseFunction getCallback() {
		return callback;
	}

	/**
	 * @param callback
	 *            the callback to set
	 */
	public void setCallback(BaseFunction callback) {
		this.callback = callback;
	}

	@Override
	public void run() {
		logger.info("Ready to execute job {} with args {}", name, Arrays.toString(args));
		Object ret = function.call(context, Main.getGlobal(), Main.getGlobal(), args);
		if (callback != null)
			callback.call(context, Main.getGlobal(), Main.getGlobal(), new Object[] { ret, name });
	}
}
