package info.thinkingcloud.jersey.mvc;

import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.utils.BaseService;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;


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