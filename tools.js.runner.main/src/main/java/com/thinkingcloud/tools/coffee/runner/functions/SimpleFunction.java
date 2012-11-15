package com.thinkingcloud.tools.coffee.runner.functions;

import org.mozilla.javascript.BaseFunction;

public class SimpleFunction extends BaseFunction {

	private static final long serialVersionUID = 3114654975362052116L;

	protected String name;

	public SimpleFunction(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	
}
