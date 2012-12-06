package com.thinkingcloud.tools.js.runner.sec.model;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.bouncycastle.openssl.PEMWriter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.beans.BeanUtils;

import com.thinkingcloud.tools.js.runner.core.NewGlobal;
import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Parameter;

public class KeyStoreModel {
	private KeyStore keystore;
	private NewGlobal global;

	public KeyStoreModel(KeyStore keystore, NewGlobal global) {
		this.keystore = keystore;
		this.global = global;
	}

	public KeyStore getKeyStore() {
		if (keystore == null)
			throw new IllegalStateException("Keystore can't be null!!!");
		return keystore;
	}

	public static KeyPair getPrivateKey(KeyStore keystore, String alias, char[] password) {
		try {
			Key key = keystore.getKey(alias, password);
			if (key instanceof PrivateKey) {
				Certificate cert = keystore.getCertificate(alias);
				PublicKey publicKey = cert.getPublicKey();
				return new KeyPair(publicKey, (PrivateKey) key);
			}
		} catch (Throwable e) {
		}
		return null;
	}

	@Function(doc = "Export the private key in the keystore", parameters = {
	        @Parameter(name = "name", type = "string", doc = "The name for the private key"),
	        @Parameter(name = "password", type = "string", doc = "The password for the private key."),
	        @Parameter(name = "path", type = "string", doc = "The doc to export the key") })
	public void exportPrivateKey(String name, String password, String path) throws IOException {
		KeyPair keyPair = getPrivateKey(keystore, name, password.toCharArray());
		if (keyPair == null)
			throw new IllegalArgumentException(MessageFormat.format(
			        "There is no key named {0} or password {1} is not right.", name, password));
		PrivateKey privateKey = keyPair.getPrivate();
		PEMWriter writer = new PEMWriter(new FileWriter(path));
		writer.writeObject(privateKey);
		writer.close();
	}

	@Function(doc = "Export the certificate in the key store to another location.", parameters = {
	        @Parameter(name = "name", type = "string", doc = "The name of the certificate"),
	        @Parameter(name = "path", doc = "The export file path", type = "string") })
	public void exportCertificate(String name, String path) throws KeyStoreException, IOException {
		X509Certificate cert = (X509Certificate) keystore.getCertificate(name);
		if (cert != null) {
			File output = new File(path);
			if (!output.exists()) {
				if (!output.getParentFile().exists())
					output.getParentFile().mkdirs();
				output.createNewFile();
			}
			FileWriter writer = new FileWriter(path);
			writer.write(cert.toString());
			writer.flush();
			writer.close();
		}
	}

	@Function(doc = "Read the certificate object from the keystore.", parameters = @Parameter(name = "name", type = "string", doc = "The certificate's name"), returns = "The certificate object.")
	public Object getCertificate(String name) throws KeyStoreException, IllegalArgumentException,
	        IllegalAccessException, InvocationTargetException {
		Scriptable ret = Context.getCurrentContext().newObject(global);
		X509Certificate cer = (X509Certificate) keystore.getCertificate(name);
		for (PropertyDescriptor pc : BeanUtils.getPropertyDescriptors(X509Certificate.class)) {
			ret.put(pc.getName(), ret, String.valueOf(pc.getReadMethod().invoke(cer)));
		}
		return ret;
	}

	@Function(doc = "List all the aliases in this keystore.", returns = "All the aliases in this keystore.")
	public String[] aliases() throws KeyStoreException {
		List<String> aliases = new ArrayList<String>();
		Enumeration<String> e = getKeyStore().aliases();
		while (e.hasMoreElements()) {
			aliases.add(e.nextElement());
		}
		return aliases.toArray(new String[aliases.size()]);
	}
}
