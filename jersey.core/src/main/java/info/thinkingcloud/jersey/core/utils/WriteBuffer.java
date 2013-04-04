package info.thinkingcloud.jersey.core.utils;

import info.thinkingcloud.jersey.core.Buffer;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public abstract class WriteBuffer implements Buffer {

	private ByteArrayOutputStream buffer;

	private PrintWriter writer;

	public OutputStream getBuffer() {
		if (buffer == null)
			buffer = new ByteArrayOutputStream();
		return buffer;
	}

	public PrintWriter getWriter() {
		if (writer == null) {
			writer = new PrintWriter(getBuffer());
		}
		return writer;
	}

	public void write(Object message) throws Exception {
		getWriter().print(message);
	}

	public void writeln(Object message) throws Exception {
		getWriter().println(message);
	}

	public void clear() throws Exception {
		try {
			reset();
		} finally {
			doClear();
		}
	}

	public void close() throws Exception {
		try {
			reset();
		} finally {
			doClose();
		}
	}

	public void flush() throws Exception {
		getWriter().flush();
		doFlush(buffer.toByteArray());
		reset();
	}

	public void reset() throws Exception {
		try {
			if (buffer != null)
				buffer.close();
			if (writer != null)
				writer.close();
		} finally {
			buffer = null;
			writer = null;
		}
	}

	protected abstract void doFlush(byte[] data) throws Exception;

	protected abstract void doClose() throws Exception;

	protected abstract void doClear() throws Exception;
}
