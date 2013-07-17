package info.thinkingcloud.jersey.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JSRunnerApplication {

	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application_context.xml");
		JSRunnerThread thread = context.getBean(JSRunnerThread.class);
		thread.setArgs(args);
		thread.run();
		context.close();
	}
}
