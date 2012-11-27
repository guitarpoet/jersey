package com.thinkingcloud.tools.js.runner.core.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Function {
	Parameter[] parameters() default {};

	String doc() default "";

	String returns() default "";
}
