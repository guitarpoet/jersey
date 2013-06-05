package info.thinkingcloud.jersey.core.utils;

import java.text.MessageFormat;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

public class MessageSupport {
	@Autowired
	private MessageSource source;

	@Value("${application.locale}")
	private String localeString;

	private Locale locale;

	protected void init() throws Exception {

	}

	protected void destroy() throws Exception {

	}

	@PostConstruct
	public void initialize() throws Exception {
		if (localeString.equals("default")) {
			locale = Locale.getDefault();
		} else {
			locale = new Locale(localeString);
		}
		init();
	}

	@PreDestroy
	public void cleanup() throws Exception {
		destroy();
	}

	public String text(String code, Object... args) {
		try {
			return source.getMessage(code, args, locale);
		} catch (NoSuchMessageException ex) {
			MessageFormat temp = new MessageFormat(code);
			return temp.format(args);
		}
	}
}
