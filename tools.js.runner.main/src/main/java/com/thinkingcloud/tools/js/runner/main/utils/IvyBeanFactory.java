package com.thinkingcloud.tools.js.runner.main.utils;

import javax.annotation.PostConstruct;

import org.apache.ivy.Ivy;
import org.apache.ivy.util.AbstractMessageLogger;
import org.apache.ivy.util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service("ivy")
public class IvyBeanFactory implements FactoryBean<Ivy> {

	private static Logger logger = LoggerFactory.getLogger("tools.js.jpm.ivy");

	private Ivy ivy;

	@Autowired
	private ResourceLoader loader;

	@Value("${jpm.config}")
	private String config;

	@PostConstruct
	public void init() {
		Message.setDefaultLogger(new AbstractMessageLogger() {

			@Override
			public void rawlog(String msg, int level) {
				log(msg, level);
			}

			@Override
			public void log(String msg, int level) {
				switch (level) {
				case Message.MSG_DEBUG:
					logger.debug(msg);
					break;
				case Message.MSG_ERR:
					logger.error(msg);
					break;
				case Message.MSG_INFO:
					logger.info(msg);
					break;
				case Message.MSG_VERBOSE:
					logger.trace(msg);
					break;
				case Message.MSG_WARN:
					logger.warn(msg);
					break;
				}
			}

			@Override
			protected void doProgress() {
				System.out.print(".");
			}

			@Override
			protected void doEndProgress(String msg) {
				System.out.println("Done!");
			}
		});
	}

	@Override
	public Ivy getObject() throws Exception {
		if (ivy == null) {
			ivy = Ivy.newInstance();
			ivy.configure(loader.getResource(config).getURL());
		}
		return ivy;
	}

	@Override
	public Class<?> getObjectType() {
		return Ivy.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
