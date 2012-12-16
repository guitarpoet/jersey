package com.thinkingcloud.tools.js.runner.main.functions;

import java.text.MessageFormat;
import java.util.ArrayList;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Parameter;
import com.thinkingcloud.tools.js.runner.core.utils.SimpleFunction;

@Service("message")
@Function(doc = "Get the message from resource bundle.", parameters = {
        @Parameter(name = "code", type = "string", doc = "The message code"),
        @Parameter(name = "args", multi = true, doc = "The args", optional = true, type = "object") })
public class MessageFunction extends SimpleFunction {
	private static final long serialVersionUID = -4637979510184783721L;

	@Autowired
	private MessageSource messages;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		if (args.length < 1)
			throw new IllegalArgumentException("Must give the message code.");
		String code = (String) args[0];
		ArrayList<Object> list = new ArrayList<Object>();
		for (int i = 1; i < args.length; i++) {
			list.add(args[i]);
		}
		try {
			return messages.getMessage(code, list.toArray(), cx.getLocale());
		} catch (NoSuchMessageException ex) {
			return MessageFormat.format(code, list.toArray());
		}
	}
}
