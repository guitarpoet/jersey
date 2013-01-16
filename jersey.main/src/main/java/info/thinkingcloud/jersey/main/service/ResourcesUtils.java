package info.thinkingcloud.jersey.main.service;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.meta.Parameter;
import info.thinkingcloud.jersey.core.utils.BaseService;
import info.thinkingcloud.jersey.main.utils.StreamLineIterator;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;


@Service("resource")
@Module(doc = "The utils for resources.")
public class ResourcesUtils extends BaseService {

	@Autowired
	private ResourceLoader resourceLoader;

	@Function(parameters = @Parameter(name = "The resource path"), doc = "Load the resource using the path.", returns = "Iterate the resource using the stream iterator.")
	public StreamLineIterator iterate(String path) throws IOException {
		return new StreamLineIterator(load(path));
	}

	public InputStream load(String path) throws IOException {
		Resource resource = resourceLoader.getResource(path);
		if (resource == null)
			return new ByteArrayInputStream(new byte[0]);
		return resource.getInputStream();
	}

	@Function(parameters = @Parameter(name = "The resource path"), doc = "Load the resource using the path.", returns = "The loaded string.")
	public String read(String path) throws FileNotFoundException, IOException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(new InputStreamReader(load(path)), writer);
		writer.flush();
		writer.close();
		return writer.toString();
	}
}
