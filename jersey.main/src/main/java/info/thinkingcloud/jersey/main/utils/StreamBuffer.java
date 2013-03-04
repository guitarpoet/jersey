package info.thinkingcloud.jersey.main.utils;

import java.io.OutputStream;

import info.thinkingcloud.jersey.core.utils.WriteBuffer;

public class StreamBuffer extends WriteBuffer {

	private OutputStream output;

	public StreamBuffer(OutputStream output) {
		this.output = output;
	}

	@Override
	protected void doFlush(byte[] data) throws Exception {
		output.write(data);
		output.flush();
	}

	@Override
	protected void doClose() throws Exception {
		output.close();
	}

	@Override
	protected void doClear() throws Exception {
		throw new UnsupportedOperationException();
	}
}
