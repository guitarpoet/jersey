package info.thinkingcloud.jersey.main.functions;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Parameter;
import info.thinkingcloud.jersey.core.utils.SimpleFunction;

import java.util.ArrayList;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;


@Service("list")
@Function(parameters = { @Parameter(multi = true, name = "obj", type = "object", optional = true, doc = "The objects to insert into the arraylist.") }, doc = "Create an array list using the args.", returns = "An arraylist.")
public class ListFunction extends SimpleFunction {
	private static final long serialVersionUID = -5433234360321485483L;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object object : args) {
			list.add(object);
		}
		return list;
	}
}
