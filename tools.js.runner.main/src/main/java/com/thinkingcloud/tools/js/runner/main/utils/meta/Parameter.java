package com.thinkingcloud.tools.js.runner.main.utils.meta;

public @interface Parameter {
	String name();
	
	boolean multi() default false;

	String type() default "object";

	boolean optional() default false;

	String doc() default "";
}
