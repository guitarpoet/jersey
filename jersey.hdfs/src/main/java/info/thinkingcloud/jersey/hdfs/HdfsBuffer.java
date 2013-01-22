package info.thinkingcloud.jersey.hdfs;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import info.thinkingcloud.jersey.core.utils.WriteBuffer;

public class HdfsBuffer extends WriteBuffer {

	private FileSystem fileSystem;

	private Path path;

	public HdfsBuffer(FileSystem fileSystem, String path) {
		this.fileSystem = fileSystem;
		this.path = new Path(path);
	}

	@Override
	protected void doFlush(byte[] data) throws Exception {
		FSDataOutputStream output = null;
		if (fileSystem.exists(path)) {
			output = fileSystem.append(path);
		} else {
			output = fileSystem.create(path);
		}
		output.write(data);
		output.flush();
		output.close();
	}

	@Override
	protected void doClose() throws Exception {
		fileSystem.close();
	}

	@Override
	protected void doClear() throws Exception {
		fileSystem.delete(path, true);
	}
}
