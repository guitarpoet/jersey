package com.thinkingcloud.tools.js.runner.application.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.stereotype.Service;

@Service
public class FileWriterService {
	private StringBuilder sb = new StringBuilder();

	public void write(String text) {
		sb.append(text);
	}

	public void writeln(String line) {
		write(line + "\n");
	}

	public void flush(String file) throws IOException {
		File f = new File(file);
		if (!f.exists()) {
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			f.createNewFile();
		}
		FileWriter writer = new FileWriter(f, true);
		writer.write(sb.toString());
		writer.flush();
		writer.close();
	}
}
