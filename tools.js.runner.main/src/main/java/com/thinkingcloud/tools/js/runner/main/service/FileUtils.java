package com.thinkingcloud.tools.js.runner.main.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.esotericsoftware.wildcard.Paths;
import com.thinkingcloud.tools.js.runner.core.meta.Function;
import com.thinkingcloud.tools.js.runner.core.meta.Module;
import com.thinkingcloud.tools.js.runner.core.meta.Parameter;
import com.thinkingcloud.tools.js.runner.core.utils.BaseService;
import com.thinkingcloud.tools.js.runner.main.utils.FileBuffer;
import com.thinkingcloud.tools.js.runner.main.utils.StreamLineIterator;

@Service("file")
@Module(doc = "The file utils.")
public class FileUtils extends BaseService {
	private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

	@Function(parameters = { @Parameter(name = "path", type = "string", doc = "The resource's path.") }, doc = "Iterate the stream using the path.", returns = "The stream iterator.")
	public StreamLineIterator iterate(String path) throws FileNotFoundException {
		File file = getFile(path);
		if (file == null)
			return null;
		return new StreamLineIterator(new FileInputStream(file));
	}

	@Function(doc = "Test if the file is a directory", parameters = @Parameter(doc = "The file's path", type = "string", name = "path"), returns = "The result")
	public boolean isDir(String path) {
		File f = getFile(path);
		if (f.exists()) {
			return f.isDirectory();
		}
		return false;
	}

	@Function(doc = "List the files in the directory.", parameters = @Parameter(name = "The directories's path."), returns = "The file list")
	public String[] list(String path) {
		File f = new File(path);
		if (!f.exists())
			return null;

		if (f.isDirectory()) {
			List<String> out = new ArrayList<String>();
			for (File file : f.listFiles()) {
				out.add(file.getAbsolutePath());
			}
			return out.toArray(new String[0]);
		}
		return new String[] { f.getAbsolutePath() };
	}

	@Function(parameters = @Parameter(name = "path", doc = "The file path", type = "string"), doc = "Test if the file is exists or is blank")
	public boolean isBlank(String path) {
		File file = new File(path);
		return !file.exists() || org.apache.commons.io.FileUtils.sizeOf(file) == 0;
	}

	@Function(doc = "Delete the file from system", parameters = @Parameter(name = "path", doc = "The file to delete", type = "string"))
	public void remove(String path) {
		org.apache.commons.io.FileUtils.deleteQuietly(getFile(path));
	}

	private File getFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			logger.warn("File {} is not existed!", path);
			return null;
		}
		return file;
	}

	@Function(doc = "Get the file's meta information", returns = "The file's meta information", parameters = @Parameter(name = "path", type = "string", doc = "The file's path"))
	public Map<String, Object> fileMeta(String path) {
		File f = getFile(path);

		if (f != null) {
			Map<String, Object> ret = new HashMap<String, Object>();
			ret.put("length", f.length());
			ret.put("lastModified", f.lastModified());
			ret.put("hidden", f.isHidden());
			ret.put("abs", f.getAbsolutePath());
			StringBuilder modes = new StringBuilder();
			if (f.canRead())
				modes.append("r");
			else
				modes.append("-");

			if (f.canWrite())
				modes.append("w");
			else
				modes.append("-");
			if (f.canExecute())
				modes.append("x");
			else
				modes.append("-");
			ret.put("mod", modes.toString());
			return ret;
		}
		return null;
	}

	@Function(doc = "The simple find function for find all the files in the directory and all subdirectories.", parameters = {
	        @Parameter(name = "path", type = "string", doc = "The directory to search"),
	        @Parameter(name = "patterns", type = "string", doc = "The patterns", multi = true, optional = true) })
	public String[] find(String path, String... patterns) {
		ArrayList<String> ret = new ArrayList<String>();
		for (String file : new Paths(path, Arrays.asList(patterns))) {
			ret.add(file);
		}
		return ret.toArray(new String[ret.size()]);
	}

	@Function(doc = "Get the file's file name", parameters = @Parameter(name = "path", type = "string", doc = "The file's path."), returns = "The file's name")
	public String fileName(String path) {
		return new File(path).getName();
	}

	@Function(doc = "Copy the file", parameters = {
	        @Parameter(name = "from", doc = "The source location", type = "string"),
	        @Parameter(name = "to", type = "string", doc = "The destination") })
	public void cp(String from, String to) throws IOException {
		org.apache.commons.io.FileUtils.copyFile(new File(from), new File(to));
	}

	@Function(doc = "Move the file", parameters = {
	        @Parameter(name = "from", doc = "The source location", type = "string"),
	        @Parameter(name = "to", type = "string", doc = "The destination") })
	public void mv(String from, String to) throws IOException {
		File source = new File(from);
		File dest = new File(to);
		if (source.exists()) {
			if (source.isDirectory()) {
				if (dest.exists() && dest.isDirectory())
					org.apache.commons.io.FileUtils.moveDirectoryToDirectory(source, dest, true);
				else
					org.apache.commons.io.FileUtils.moveDirectory(source, dest);
			} else if (dest.exists() && dest.isDirectory())
				org.apache.commons.io.FileUtils.moveFileToDirectory(source, dest, false);
			else
				org.apache.commons.io.FileUtils.moveFile(source, dest);
		}
	}

	public String dirName(String path) {
		return new File(path).getParentFile().getAbsolutePath();
	}

	@Function(parameters = @Parameter(name = "path", type = "string", doc = "The resources's path"), doc = "Open the file to write.", returns = "The opened file buffer.")
	public FileBuffer open(String path) {
		return new FileBuffer(path);
	}

	@Function(parameters = @Parameter(name = "path", type = "string", doc = "The resources's path"), doc = "Read the file content into a string.", returns = "The file's content.")
	public String read(String path) throws FileNotFoundException, IOException {
		File file = getFile(path);
		if (file == null)
			return null;
		return org.apache.commons.io.FileUtils.readFileToString(file);
	}
}
