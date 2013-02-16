package info.thinkingcloud.jersey.hdfs;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.meta.Parameter;
import info.thinkingcloud.jersey.core.utils.BaseService;
import info.thinkingcloud.jersey.core.utils.StreamLineIterator;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service("hdfs")
@Module(doc = "The hdfs access service.")
public class HdfsService extends BaseService {

	private FileSystem fileSystem;

	@Autowired
	private ResourceLoader resourceLoader;

	@Value("${hadoop.core.config}")
	private String hadoopCore;

	@Value("${hadoop.hdfs.config}")
	private String hadoopHdfs;

	@PostConstruct
	public void init() throws IOException {
		Configuration config = new Configuration();
		config.addResource(resourceLoader.getResource(hadoopCore).getURL());
		config.addResource(resourceLoader.getResource(hadoopHdfs).getURL());

		fileSystem = FileSystem.get(config);
	}

	@Function(doc = "Remove the file at the path", parameters = @Parameter(name = "path", type = "string", doc = "The file path to remove."))
	public void remove(String path) throws IOException {
		fileSystem.delete(new Path(path), true);
	}

	@Function(doc = "Get the file status of the file at the path", parameters = @Parameter(name = "path", type = "string", doc = "The file's path"), returns = "File status object")
	public FileStatus info(String path) throws IOException {
		return fileSystem.getFileStatus(new Path(path));
	}

	@Function(doc = "Open the hadoop path to write on", parameters = @Parameter(name = "path", type = "string", doc = "The file path to open."))
	public HdfsBuffer open(String path) {
		return new HdfsBuffer(fileSystem, path);
	}

	@Function(doc = "Iterate the file in the hdfs", parameters = @Parameter(name = "path", type = "string", doc = "The file path to iterate."))
	public StreamLineIterator iterate(String path) throws IOException {
		return new StreamLineIterator(fileSystem.open(new Path(path)));
	}

	@Function(doc = "List the directory", parameters = @Parameter(name = "path", type = "string", doc = "The directory path to list."))
	public String[] ls(String path) throws IOException {
		FileStatus[] status = fileSystem.listStatus(new Path(path));
		ArrayList<String> result = new ArrayList<String>();
		for (FileStatus s : status) {
			result.add(s.getPath().toString());
		}
		return result.toArray(new String[result.size()]);
	}
}
