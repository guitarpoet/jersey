package com.thinkingcloud.tools.js.runner.application.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class StringUtilsService {

	public boolean isNumber(Object o) {
		return StringUtils.isNumeric(String.valueOf(o));
	}

	public boolean isBlank(String str) {
		return StringUtils.isBlank(str);
	}

	public String[] split(String str, String sep) {
		return str.split(sep);
	}

	public boolean isEmpty(String str) {
		return StringUtils.isEmpty(str);
	}

	public String capitalize(String str) {
		return StringUtils.capitalize(str);
	}
}
