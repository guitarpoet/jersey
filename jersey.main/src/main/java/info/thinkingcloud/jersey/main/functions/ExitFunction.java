package info.thinkingcloud.jersey.main.functions;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.utils.SimpleFunction;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;


@Service("exit")
@Function(doc = "Exit the shell.")
public class ExitFunction extends SimpleFunction {

	private static final long serialVersionUID = 3033796323919573312L;

	private static Logger logger = LoggerFactory.getLogger(ExitFunction.class);

	@Autowired
	private ConfigurableApplicationContext context;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		ConfigurableApplicationContext parent = context;
		while (parent.getParent() != null) {
			parent = (ConfigurableApplicationContext) parent.getParent();
		}
		parent.close();

		logger.info("Bye.");
		System.exit(0);
		return null;
	}
}