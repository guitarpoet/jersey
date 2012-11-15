package com.thinkingcloud.tools.js.runner.main.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVWriter;

public class CSVBuffer {

	private String file;
	private CSVWriter writer;
	private StringWriter buffer;

	public CSVBuffer(String file) throws IOException {
		this.file = file;
		reset();
	}

	private File getFile() throws IOException {
		File f = new File(file);
		if (!f.exists()) {
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			f.createNewFile();
		}
		return f;
	}

	public void write(Object[] data) throws IOException {
		ArrayList<String> list = new ArrayList<String>(data.length);
		for (Object o : data) {
			list.add(String.valueOf(o));
		}
		writer.writeNext(list.toArray(new String[0]));
	}

	public void flush() throws IOException {
		writer.flush();
		FileWriter file = new FileWriter(getFile(), true);
		file.write(buffer.toString());
		file.flush();
		file.close();
		reset();
	}

	public void remove() throws IOException {
		getFile().delete();
	}

	public void reset() throws IOException {
		if (buffer != null)
			buffer.close();
		if (writer != null)
			writer.close();
		buffer = new StringWriter();
		writer = new CSVWriter(buffer);
	}
}
