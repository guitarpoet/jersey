package info.thinkingcloud.jersey.main.functions;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Parameter;
import info.thinkingcloud.jersey.core.utils.SimpleFunction;
import info.thinkingcloud.jersey.main.utils.PagerObject;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;


@Service("pager")
@Function(doc = "Read the long text in a pager", parameters = @Parameter(name = "text", type = "string", doc = "The long text to read."))
public class PagerFunction extends SimpleFunction {

	private static final long serialVersionUID = -1258652099182902834L;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		String text = null;
		if (args[0] instanceof Scriptable) {
			text = (String) Context.jsToJava(args[0], String.class);
		} else {
			text = String.valueOf(args[0]);
		}
		return new PagerObject(text);
	}
}
