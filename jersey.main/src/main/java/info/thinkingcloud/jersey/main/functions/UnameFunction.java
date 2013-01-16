package info.thinkingcloud.jersey.main.functions;

import java.text.MessageFormat;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.utils.SimpleFunction;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("uname")
@Function(doc = "Show the detail informations for this shell.")
public class UnameFunction extends SimpleFunction {
	private static final long serialVersionUID = 4546944082474210867L;

	@Value("${application.version}")
	private String version;

	@Value("${application.name}")
	private String name;

	@Value("${application.company}")
	private String company;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		return MessageFormat.format("{0} version {1}, provided by {2}", name, version, company);
	}
}
