package info.thinkingcloud.jersey.main.utils;

import info.thinkingcloud.jersey.core.utils.WriteBuffer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileBuffer extends WriteBuffer {

	private String file;

	public FileBuffer(String file) {
		this.file = file;
	}

	public void remove() throws Exception {
		this.clear();
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

	@Override
	protected void doFlush(byte[] data) throws Exception {
		FileOutputStream out = new FileOutputStream(getFile(), true);
		out.write(data);
		out.flush();
		out.close();
	}

	@Override
	protected void doClose() throws Exception {
	}

	@Override
	protected void doClear() throws Exception {
		getFile().delete();
	}
}
