package com.thinkingcloud.tools.js.runner.main.functions;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.main.utils.meta.Function;
import com.thinkingcloud.tools.js.runner.main.utils.meta.Parameter;

@Service("md5")
@Function(parameters = { @Parameter(name = "str", type = "string", doc = "The string for md5 hash.") }, doc = "Generate the md5 hash for the input string", returns = "The md5 string.")
public class MD5Function extends SimpleFunction {

	private static final long serialVersionUID = -5815230307564450858L;

	private static final Logger logger = LoggerFactory.getLogger(MD5Function.class);

	@Override
	public Object call(Context context, Scriptable scope, Scriptable thisObj, Object[] args) {
		try {
			String data = (String) args[0];
			MessageDigest md = MessageDigest.getInstance("MD5");
			return new String(Hex.encodeHex(md.digest(data.getBytes("UTF-8"))));
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
		}
		return null;
	}
}
