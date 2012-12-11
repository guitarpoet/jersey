package com.thinkingcloud.tools.js.runner.mvc;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.meta.Module;
import com.thinkingcloud.tools.js.runner.core.utils.BaseService;

@Service("requestUtils")
@Module(doc = "The request's utils")
public class RequestUtils extends BaseService {

	@Autowired
	private ResourceLoader resourceLoader;

	public void pass(String path, HttpServletResponse response) throws IOException {
		Resource r = resourceLoader.getResource(path.startsWith("/") ? path.substring(1) : path);
		if (r != null && r.getInputStream() != null) {
			OutputStream out = response.getOutputStream();
			IOUtils.copyLarge(r.getInputStream(), out);
			out.flush();
			out.close();
		}
	}
}