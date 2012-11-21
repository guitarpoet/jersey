package com.thinkingcloud.tools.js.runner.main.functions;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
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
			byte[] rawHmac = mac.doFinal(value.getBytes());

			// Convert raw bytes to Hex
			byte[] hexBytes = new Hex().encode(rawHmac);

			// Covert array of Hex bytes to a String
			return new String(hexBytes, "UTF-8");
		} catch (Throwable t) {
			return null;
		}
	}
}
