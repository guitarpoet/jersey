package com.thinkingcloud.tools.js.runner.main.functions;

import java.util.Arrays;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.main.service.Job;
import com.thinkingcloud.tools.js.runner.main.service.JobManger;

@Service("job")
public class JobFunction extends BaseFunction {

	private static final long serialVersionUID = -7559259376215033767L;

	private static Logger logger = LoggerFactory.getLogger(JobFunction.class);

	@Autowired
	private JobManger jobManager;

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
		return jobManager.submit(new Job(name, Context.getCurrentContext(), function, callback, callargs));
	}
}
