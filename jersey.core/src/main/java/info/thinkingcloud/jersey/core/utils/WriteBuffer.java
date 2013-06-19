package info.thinkingcloud.jersey.core.utils;

import info.thinkingcloud.jersey.core.Buffer;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public abstract class WriteBuffer implements Buffer {

	private int flushLimit = 1000;

	private ByteArrayOutputStream buffer;

	private PrintWriter writer;

	private boolean autoFlush = false;

	private int flushCount = 0;

	/**
	 * @return the autoFlush
	 */
	public boolean isAutoFlush() {
		return autoFlush;
	}

	/**
	 * @return the flushLimit
	 */
	public int getFlushLimit() {
		return flushLimit;
	}

	/**
	 * @param flushLimit
	 *            the flushLimit to set
	 */
	public void setFlushLimit(int flushLimit) {
		this.flushLimit = flushLimit;
	}

	/**
	 * @param autoFlush
	 *            the autoFlush to set
	 */
	public void setAutoFlush(boolean autoFlush) {
		this.autoFlush = autoFlush;
	}

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

	public void checkFlush() throws Exception {
		if (autoFlush) {
			if (flushCount++ == flushLimit) {
				try {
					flush();
				} finally {
					flushCount = 0;
				}
			}
		}
	}

	public void write(Object message) throws Exception {
		getWriter().print(message);
		checkFlush();
	}

	public void writeln(Object message) throws Exception {
		getWriter().println(message);
		checkFlush();
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
