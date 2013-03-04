package info.thinkingcloud.jersey.main.service;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.meta.Parameter;
import info.thinkingcloud.jersey.core.utils.BaseService;
import info.thinkingcloud.jersey.core.utils.StreamLineIterator;
import info.thinkingcloud.jersey.main.utils.StreamBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;

@Service("streams")
@Module(doc = "The stream manipulations")
public class StreamUtils extends BaseService {
	@Function(doc = "Iterate the stream using line.", parameters = @Parameter(name = "in", type = "InputStream", doc = "The input stream to read."), returns = "The stream line iterator")
	public StreamLineIterator iterate(InputStream in) {
		return new StreamLineIterator(in);
	}

	@Function(doc = "Read the input to end", parameters = @Parameter(name = "in", type = "InputStream", doc = "The input stream to read"), returns = "The content of stream as string")
	public String read(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IOUtils.copy(in, out);
		out.flush();
		in.close();
		return new String(out.toByteArray());
	}

	@Function(doc = "Get the buffer for use output stream", parameters = @Parameter(name = "output", type = "Output stream", doc = "The output stream to write"), returns = "The output stream buffer.")
	public StreamBuffer getBuffer(OutputStream output) {
		return new StreamBuffer(output);
	}

}
