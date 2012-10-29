package com.thinkingcloud.tools.js.runner.application.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;

public class BufferedStreamEnumeration implements Enumeration<String> {
	private BufferedReader reader;
	private String current;

	public BufferedStreamEnumeration(InputStream in) {
		this(new BufferedReader(new InputStreamReader(in)));
	}

	public BufferedStreamEnumeration(BufferedReader reader) {
		this.reader = reader;
	}

	@Override
	public boolean hasMoreElements() {
		try {
			return (current = reader.readLine()) != null;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public String nextElement() {
		return current;
	}

}
