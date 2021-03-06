package info.thinkingcloud.jersey.main.functions;

import info.thinkingcloud.jersey.core.NewGlobal;
import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Parameter;
import info.thinkingcloud.jersey.core.utils.SimpleFunction;
import info.thinkingcloud.jersey.main.service.Job;
import info.thinkingcloud.jersey.main.service.JobManager;

import java.util.Arrays;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("job")
@Function(parameters = {
        @Parameter(name = "name", type = "string", doc = "The background job's name"),
        @Parameter(name = "function", type = "function", doc = "The function that need to run in background."),
        @Parameter(name = "callback", optional = true, type = "function", doc = "The callback function if the function in job has returned."),
        @Parameter(name = "args", optional = true, multi = true, doc = "The args that need to send to the job function.") }, doc = "Create a background job.", returns = "The future object for this job.")
public class JobFunction extends SimpleFunction {

	private static final long serialVersionUID = -7559259376215033767L;

	private static Logger logger = LoggerFactory.getLogger(JobFunction.class);

	@Autowired
	private JobManager jobManager;

	@Autowired
	private NewGlobal global;

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		if (args.length < 3) {
			logger.error("The args are too little to do the job.");
		}
		String name = (String) args[0];
		BaseFunction function = (BaseFunction) args[1];
		BaseFunction callback = null;
		int offset = 2;
		if (args[2] instanceof BaseFunction) {
			callback = (BaseFunction) args[2];
			offset = 3;
		}
		Object[] callargs = new Object[0];
		if (offset < args.length)
			callargs = Arrays.copyOfRange(args, offset, args.length);
		return jobManager.submit(new Job(name, cx, global, function, callback, callargs));
	}
}
