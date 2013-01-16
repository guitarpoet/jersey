package info.thinkingcloud.jersey.main.service;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.meta.Parameter;
import info.thinkingcloud.jersey.core.utils.BaseService;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.apache.ivy.Ivy;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.retrieve.RetrieveOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("jpm")
@Module(doc = "The package management service.")
public class PackageManagerService extends BaseService {

	@Autowired
	private Ivy ivy;

	@Function(parameters = @Parameter(name = "modulefile", doc = "The module file"), doc = "Resolve the dependencies using the module file.", returns = "The resolve report.")
	public ResolveReport resolve(String moduleFile) throws ParseException, IOException {
		return ivy.resolve(new File(moduleFile));
	}

	@Function(parameters = @Parameter(name = "modulefile", doc = "The module file"), doc = "Download the dependencies of the module.")
	public void retrieve(String moduleFile) throws IOException, ParseException {
		ResolveReport report = resolve(moduleFile);
		RetrieveOptions options = new RetrieveOptions();
		options.setOverwriteMode(RetrieveOptions.OVERWRITEMODE_DIFFERENT);
		ivy.retrieve(report.getModuleDescriptor().getModuleRevisionId(), "lib/[artifact]-[revision].[type]", options);
	}
}