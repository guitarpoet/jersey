package info.thinkingcloud.jersey.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

public class StreamLineIterator implements Iterator<String> {
	private BufferedReader reader;
	private String current;

	public StreamLineIterator(InputStream in) {
		this(new BufferedReader(new InputStreamReader(in)));
	}

	public StreamLineIterator(BufferedReader reader) {
		this.reader = reader;
	}

	@Override
	public boolean hasNext() {
		try {
			return (current = reader.readLine()) != null;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public String next() {
		return current;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	public void close() throws IOException {
		reader.close();
	}
}
