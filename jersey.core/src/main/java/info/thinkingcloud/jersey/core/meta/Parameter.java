package info.thinkingcloud.jersey.core.meta;

public @interface Parameter {
	String name();
	
	boolean multi() default false;

	String type() default "object";

	boolean optional() default false;

	String doc() default "";
}
