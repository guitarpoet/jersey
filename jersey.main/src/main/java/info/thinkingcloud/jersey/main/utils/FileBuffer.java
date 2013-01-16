package info.thinkingcloud.jersey.main.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileBuffer {

	private String file;

	public FileBuffer(String file) {
		this.file = file;
	}

	private StringBuilder sb = new StringBuilder();

	public void write(String text) {
		sb.append(text);
	}

	public void writeln(String line) {
		write(line + "\n");
	}

	public void remove() throws IOException {
		getFile().delete();
	}

	public void flush() throws IOException {
		FileWriter writer = new FileWriter(getFile(), true);
		writer.write(sb.toString());
		writer.flush();
		writer.close();
		sb = new StringBuilder();
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
}
