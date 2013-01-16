package info.thinkingcloud.jersey.http;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import org.apache.http.ProtocolException;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RobostRedirectStrategy extends DefaultRedirectStrategy {
	private static Logger logger = LoggerFactory.getLogger(RobostRedirectStrategy.class);

	public String fix(String uri) throws UnsupportedEncodingException {
		return URLEncoder.encode(uri, "iso-8859-1").replace("%2F", "/").replace("%3F", "?").replace("%3D", "=")
		        .replace("%26", "&");
	}

	@Override
	protected URI createLocationURI(String location) {
		try {
			return super.createLocationURI(location);
		} catch (ProtocolException ex) {
			try {
				String fixed = fix(location);
				logger.warn("The url that redirected is not valid, fix {} -> {}", location, fixed);
				return new URI(fixed);
			} catch (Throwable e) {
				logger.error(ex.getMessage(), ex);
				return null;
			}
		}
	}
}
