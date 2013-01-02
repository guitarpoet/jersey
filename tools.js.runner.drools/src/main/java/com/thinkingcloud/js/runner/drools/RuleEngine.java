package com.thinkingcloud.js.runner.drools;

import javax.annotation.PostConstruct;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Module;
import com.thinkingcloud.tools.js.runner.core.meta.Parameter;

@Service("drools")
@Module(doc = "The rule engine based on JBoss drools")
public class RuleEngine {
	private KnowledgeBuilder builder;
	private KnowledgeBase base;

	@PostConstruct
	public void init() {
		builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		base = KnowledgeBaseFactory.newKnowledgeBase();
	}

	@Function(doc = "Add drl rules to rule engine.", parameters = @Parameter(name = "path", doc = "The pathes for all the rules", multi = true, type = "string"))
	public void addRules(String... paths) {
		for (String path : paths) {
			builder.add(ResourceFactory.newClassPathResource(path), ResourceType.DRL);
		}
	}

	@Function(doc = "Open 1 stateless session to the knowledge base")
	public StatelessKnowledgeSession session() {
		return base.newStatelessKnowledgeSession();
	}

	@Function(doc = "Open 1 stateful session to the knowledge base")
	public StatefulKnowledgeSession statefulSession() {
		return base.newStatefulKnowledgeSession();
	}
}
