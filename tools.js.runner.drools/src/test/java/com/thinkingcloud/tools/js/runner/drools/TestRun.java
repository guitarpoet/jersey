package com.thinkingcloud.tools.js.runner.drools;

import java.util.HashMap;
import java.util.Map;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;

public class TestRun {
	public static void main(String[] args) {
		KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		builder.add(ResourceFactory.newClassPathResource("test.drl"), ResourceType.DRL);

		if (builder.hasErrors()) {
			throw new RuntimeException(builder.getErrors().toString());
		}

		KnowledgeBase base = KnowledgeBaseFactory.newKnowledgeBase();
		base.addKnowledgePackages(builder.getKnowledgePackages());

		StatelessKnowledgeSession session = base.newStatelessKnowledgeSession();
		for (int i = 0; i < 100; i++) {
			Map<Object, Object> object = new HashMap<Object, Object>();
			object.put("num", i);
			session.execute(object);
		}
	}
}