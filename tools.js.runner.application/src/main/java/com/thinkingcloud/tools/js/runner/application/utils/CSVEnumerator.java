package com.thinkingcloud.tools.js.runner.application.utils;

import java.io.IOException;
import java.io.Reader;
import java.util.Enumeration;

import au.com.bytecode.opencsv.CSVReader;

public class CSVEnumerator implements Enumeration<String[]> {

	private CSVReader reader;

	private String[] current;

	public CSVEnumerator(Reader reader) {
		this.reader = new CSVReader(reader);
	}

	@Override
	public boolean hasMoreElements() {
		try {
			current = reader.readNext();
		} catch (IOException e) {
		}
		return current != null;
	}

	@Override
	public String[] nextElement() {
		return current;
	}

}
