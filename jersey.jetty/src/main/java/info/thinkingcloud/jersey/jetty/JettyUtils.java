package info.thinkingcloud.jersey.jetty;

import info.thinkingcloud.jersey.core.NewGlobal;
import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.utils.BaseService;

import org.mozilla.javascript.Scriptable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("jetty")
@Module(doc = "The service component for jetty")
public class JettyUtils extends BaseService {

	@Autowired
	private NewGlobal global;

	public JettyServer createServer(Scriptable handler) {
		return new JettyServer(handler, global);
	}
}