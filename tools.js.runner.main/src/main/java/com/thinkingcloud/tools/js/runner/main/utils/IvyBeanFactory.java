package com.thinkingcloud.tools.js.runner.main.utils;

import org.apache.ivy.Ivy;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service("ivy")
public class IvyBeanFactory implements FactoryBean<Ivy> {

	private Ivy ivy;

	@Autowired
	private ResourceLoader loader;

	@Value("${jpm.config}")
	private String config;

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
