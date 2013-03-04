package info.thinkingcloud.jersey.main.service;

import java.io.File;
import java.io.IOException;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.meta.Parameter;
import info.thinkingcloud.jersey.core.utils.BaseService;

import org.springframework.stereotype.Service;

@Service("process")
@Module(doc = "The module for launching processes")
public class ProcessUtils extends BaseService {
	@Function(doc = "Launch the application using the args", parameters = {
	        @Parameter(name = "app", doc = "The application to launch.", type = "string"),
	        @Parameter(name = "args", doc = "The args to start the application", type = "array", optional = true),
	        @Parameter(name = "workindir", doc = "The working dir, default is java temp folder", optional = true) }, returns = "The process that started.")
	public Process launch(String app, String[] args, String workingdir) throws IOException {
		String cmd = app;
		if (args != null) {
			for (String arg : args) {
				cmd += "\"" + arg + "\" ";
			}
		}
		ProcessBuilder builder = new ProcessBuilder(cmd);
		builder.directory(new File(workingdir));
		return builder.start();
	}

	public Process launch(String app, String workingdir) throws IOException {
		return launch(app, null, workingdir);
	}

	public Process launch(String app) throws IOException {
		return launch(app, System.getProperty("java.io.tmpdir"));
	}
}
