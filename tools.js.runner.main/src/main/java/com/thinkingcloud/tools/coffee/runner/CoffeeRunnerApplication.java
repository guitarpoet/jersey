package com.thinkingcloud.tools.coffee.runner;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CoffeeRunnerApplication {

	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application_context.xml");
		CoffeeRunnerThread thread = context.getBean(CoffeeRunnerThread.class);
		thread.setArgs(args);
		thread.run();
	}
}
