package info.thinkingcloud.jersey.core.utils;

import info.thinkingcloud.jersey.core.Buffer;

import java.io.OutputStream;
import java.io.PrintWriter;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

public abstract class WriteBuffer implements Buffer {

	private ByteOutputStream buffer;

	private PrintWriter writer;

	public OutputStream getBuffer() {
		if (buffer == null)
			buffer = new ByteOutputStream();
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
		doFlush(buffer.getBytes());
		reset();
	}

	public void reset() {
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
