package info.thinkingcloud.jersey.drools;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.meta.Parameter;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("drools")
@Module(doc = "The rule engine based on JBoss drools")
public class RuleEngine {
	private Logger logger = LoggerFactory.getLogger(RuleEngine.class);
	private KnowledgeBuilder builder;
	private KnowledgeBase base;
	@Autowired
	private JavaScriptUtils jsutils;

	@PostConstruct
	public void init() {
		builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		base = KnowledgeBaseFactory.newKnowledgeBase();
	}

	@Function(doc = "Add drl rules to rule engine.", parameters = @Parameter(name = "path", doc = "The pathes for all the rules", multi = true, type = "string"))
	public void addRules(String... paths) {
		logger.info("Reading rules {} into rule engine.", Arrays.toString(paths));
		for (String path : paths) {
			builder.add(ResourceFactory.newClassPathResource(path), ResourceType.DRL);
		}
		if (builder.hasErrors()) {
			throw new RuntimeException(builder.getErrors().toString());
		}
		base.addKnowledgePackages(builder.getKnowledgePackages());
	}

	@Function(doc = "Open 1 stateless session to the knowledge base")
	public StatelessKnowledgeSession session() {
		StatelessKnowledgeSession session = base.newStatelessKnowledgeSession();
		session.setGlobal("jsutils", jsutils);
		return session;
	}

	@Function(doc = "Open 1 stateful session to the knowledge base")
	public StatefulKnowledgeSession statefulSession() {
		StatefulKnowledgeSession session = base.newStatefulKnowledgeSession();
		session.setGlobal("jsutils", jsutils);
		return session;
	}
}
