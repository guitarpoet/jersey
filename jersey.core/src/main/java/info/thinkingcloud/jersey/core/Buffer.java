package info.thinkingcloud.jersey.core;


public interface Buffer {
	void write(Object message) throws Exception;

	void writeln(Object message) throws Exception;

	void clear() throws Exception;

	void close() throws Exception;

	void flush() throws Exception;

	void reset() throws Exception;
}
