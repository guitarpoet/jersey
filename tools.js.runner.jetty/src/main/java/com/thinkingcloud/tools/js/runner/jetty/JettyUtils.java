package com.thinkingcloud.tools.js.runner.jetty;

import org.mozilla.javascript.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.NewGlobal;
import com.thinkingcloud.tools.js.runner.core.meta.Module;
import com.thinkingcloud.tools.js.runner.core.utils.BaseService;

@Service("jetty")
@Module(doc = "The service component for jetty")
public class JettyUtils extends BaseService {

	@Autowired
	private NewGlobal global;

	public JettyServer createServer(Function handler) {
		return new JettyServer(handler, global);
	}
}