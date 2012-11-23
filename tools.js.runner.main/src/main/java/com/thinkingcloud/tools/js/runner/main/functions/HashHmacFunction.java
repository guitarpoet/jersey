package com.thinkingcloud.tools.js.runner.main.functions;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;

@Service("hash_hmac")
public class HashHmacFunction extends BaseFunction {
	private static final long serialVersionUID = -3760752443304460927L;

	private static final Map<String, String> algs = new HashMap<String, String>();

	static {
		algs.put("sha1", "HmacSHA1");
		algs.put("md5", "HmacMD5");
	}

	private static String byte2hex(byte[] bytes) {
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex.toUpperCase());
		}
		return sign.toString();
	}

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		try {
			String alg = algs.get(args[0]);
			String value = (String) args[1];
			String key = (String) args[2];
			byte[] keyBytes = key.getBytes();
			SecretKeySpec signingKey = new SecretKeySpec(keyBytes, alg);

			// Get an hmac_sha1 Mac instance and initialize with the signing
			// key
			Mac mac = Mac.getInstance(alg);
			mac.init(signingKey);

			// Compute the hmac on input data bytes
			return byte2hex(mac.doFinal(value.getBytes()));
		} catch (Throwable t) {
			return null;
		}
	}
}
