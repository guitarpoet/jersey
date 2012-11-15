package com.thinkingcloud.tools.js.runner.main.utils;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import au.com.bytecode.opencsv.CSVReader;

public class CSVIterator implements Iterator<String[]> {

	private CSVReader reader;

	private String[] current;

	public CSVIterator(Reader reader) {
		this.reader = new CSVReader(reader);
	}

	@Override
	public boolean hasNext() {
		try {
			current = reader.readNext();
		} catch (IOException e) {
		}
		return current != null;
	}

	@Override
	public String[] next() {
		return current;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
