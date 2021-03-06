package info.thinkingcloud.jersey.sec;

import info.thinkingcloud.jersey.core.NewGlobal;
import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.meta.Parameter;
import info.thinkingcloud.jersey.core.utils.BaseService;
import info.thinkingcloud.jersey.sec.model.KeyStoreModel;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;

import javax.annotation.PostConstruct;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;


@Service("sec")
@Module(doc = "The security functions module")
public class SecurityUtilsService extends BaseService {

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private NewGlobal global;

	private KeyStore current;

	@PostConstruct
	public void init() {
		Security.addProvider(new BouncyCastleProvider());
	}

	@Function(doc = "Get the keystore from the location and password, and set the current keystore to this keystore.", parameters = {
	        @Parameter(name = "path", type = "string", doc = "The keystore's location"),
	        @Parameter(name = "password", type = "string", doc = "The password for this key store."),
	        @Parameter(name = "type", type = "string", optional = true, doc = "The keystore's type, if not set, using the default type.") }, returns = "KeyStore")
	public KeyStoreModel keystore(String path, String password) throws KeyStoreException, NoSuchAlgorithmException,
	        CertificateException, IOException {
		return keystore(path, password, KeyStore.getDefaultType());
	}

	public KeyStoreModel keystore(String path, String password, String type) throws KeyStoreException,
	        NoSuchAlgorithmException, CertificateException, IOException {
		current = KeyStore.getInstance(type);
		Resource r = resourceLoader.getResource(path);
		if (r.getInputStream() != null) {
			current.load(r.getInputStream(), password.toCharArray());
		}

		return new KeyStoreModel(current, global);
	}
}
