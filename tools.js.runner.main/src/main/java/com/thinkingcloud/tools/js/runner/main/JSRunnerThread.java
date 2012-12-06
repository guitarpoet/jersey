package com.thinkingcloud.tools.js.runner.main;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jline.console.ConsoleReader;
import jline.console.completer.FileNameCompleter;
import jline.internal.InputStreamReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.GeneratedClassLoader;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.SecurityController;
import org.mozilla.javascript.tools.SourceReader;
import org.mozilla.javascript.tools.ToolErrorReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.NewGlobal;
import com.thinkingcloud.tools.js.runner.main.service.StringService;

@Service
public class JSRunnerThread {

	private static final Logger logger = LoggerFactory.getLogger(JSRunnerThread.class);

	@Autowired
	private ConfigurableApplicationContext context;

	@Autowired
	private StringService sutils;

	private String[] args;

	@Autowired
	private NewGlobal global;

	private ScriptCache scriptCache;

	@Autowired
	private GlobalCompleter jsCompleter;

	/**
	 * @return the args
	 */
	public String[] getArgs() {
		return args;
	}

	/**
	 * @param args
	 *            the args to set
	 */
	public void setArgs(String[] args) {
		this.args = args;
	}

	@SuppressWarnings("unchecked")
	public void run() {
		try {
			GnuParser parser = new GnuParser();
			Options opts = new Options();
			opts.addOption("c", "config", true, "The configuration of this application.");
			opts.addOption("s", "script", true, "The script to run");

			CommandLine cmd = parser.parse(opts, args);

			String config = "config.properties";
			if (cmd.hasOption("c")) {
				config = cmd.getOptionValue("c");
			}

			File f = new File(config);
			if (f.exists()) {
				System.getProperties().load(new FileReader(f));
			} else {
				logger.info("The configuration file {} is not existed, using default ones.", f);
			}
			Context c = Context.enter();
			c.setOptimizationLevel(-1);
			global.init(c);
			if (cmd.getArgList().size() > 0) {
				for (String file : ((List<String>) cmd.getArgList())) {
					global.defineProperty("__filename", file, ScriptableObject.DONTENUM);
					c.evaluateReader(global, new FileReader(file), file, 1, null);
				}
			} else if (cmd.hasOption("s")) {
				String source = cmd.getOptionValue("s");
				global.defineProperty("__filename", "-", ScriptableObject.DONTENUM);
				if (source.equals("-")) {
					source = sutils.readToEnd(new InputStreamReader(System.in));
				}
				evaluateScript(loadScriptFromSource(c, source, "<stdin>", 1, null), c, global);
				return;
			} else {
				global.defineProperty("__filename", "-", ScriptableObject.DONTENUM);
				ConsoleReader reader = new ConsoleReader();
				reader.addCompleter(jsCompleter);
				reader.addCompleter(new FileNameCompleter());
				StringBuilder sb = new StringBuilder();
				String s = null;
				boolean pending = false;
				while (context.isRunning()) {
					try {
						String prompt = pending ? "" : "js> ";
						s = reader.readLine(prompt);
						sb.append("\n").append(s);
						if (c.stringIsCompilableUnit(sb.toString())) {
							pending = false;
							System.out.println(Context.toString(evaluateScript(
							        loadScriptFromSource(c, sb.toString(), "<stdin>", 1, null), c, global)));
							sb = new StringBuilder();
							pending = false;
						} else
							pending = true;
					} catch (Throwable t) {
						logger.error(t.getMessage());
						pending = false;
						sb = new StringBuilder();
					}
				}
			}
			if (context.isRunning())
				context.close();
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
		}
	}

	public Object evaluateScript(Script script, Context cx, Scriptable scope) {
		try {
			return script.exec(cx, scope);
		} catch (RhinoException rex) {
			ToolErrorReporter.reportException(cx.getErrorReporter(), rex);
		} catch (VirtualMachineError ex) {
			ex.printStackTrace();
			String msg = ToolErrorReporter.getMessage("msg.uncaughtJSException", ex.toString());
			Context.reportError(msg);
		}
		return Context.getUndefinedValue();
	}

	public void processFile(Context cx, Scriptable scope, String filename) {
		processFileSecure(cx, scope, filename, null);
	}

	public Script loadScriptFromSource(Context cx, String scriptSource, String path, int lineno, Object securityDomain) {
		try {
			return cx.compileString(scriptSource, path, lineno, securityDomain);
		} catch (EvaluatorException ee) {
		} catch (RhinoException rex) {
			ToolErrorReporter.reportException(cx.getErrorReporter(), rex);
		} catch (VirtualMachineError ex) {
			// Treat StackOverflow and OutOfMemory as runtime errors
			ex.printStackTrace();
			String msg = ToolErrorReporter.getMessage("msg.uncaughtJSException", ex.toString());
			Context.reportError(msg);
		}
		return null;
	}

	private static Script loadCompiledScript(Context cx, String path, byte[] data, Object securityDomain) {
		if (data == null) {
			return null;
		}
		// XXX: For now extract class name of compiled Script from path
		// instead of parsing class bytes
		int nameStart = path.lastIndexOf('/');
		if (nameStart < 0) {
			nameStart = 0;
		} else {
			++nameStart;
		}
		int nameEnd = path.lastIndexOf('.');
		if (nameEnd < nameStart) {
			// '.' does not exist in path (nameEnd < 0)
			// or it comes before nameStart
			nameEnd = path.length();
		}
		String name = path.substring(nameStart, nameEnd);
		try {
			GeneratedClassLoader loader = SecurityController.createLoader(cx.getApplicationClassLoader(),
			        securityDomain);
			Class<?> clazz = loader.defineClass(name, data);
			loader.linkClass(clazz);
			if (!Script.class.isAssignableFrom(clazz)) {
				throw Context.reportRuntimeError("msg.must.implement.Script");
			}
			return (Script) clazz.newInstance();
		} catch (RhinoException rex) {
			ToolErrorReporter.reportException(cx.getErrorReporter(), rex);
		} catch (IllegalAccessException iaex) {
			Context.reportError(iaex.toString());
		} catch (InstantiationException inex) {
			Context.reportError(inex.toString());
		}
		return null;
	}

	private byte[] getDigest(Object source) {
		byte[] bytes, digest = null;

		if (source != null) {
			if (source instanceof String) {
				try {
					bytes = ((String) source).getBytes("UTF-8");
				} catch (UnsupportedEncodingException ue) {
					bytes = ((String) source).getBytes();
				}
			} else {
				bytes = (byte[]) source;
			}
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				digest = md.digest(bytes);
			} catch (NoSuchAlgorithmException nsa) {
				// Should not happen
				throw new RuntimeException(nsa);
			}
		}

		return digest;
	}

	private Object readFileOrUrl(String path, boolean convertToString) {
		try {
			return SourceReader.readFileOrUrl(path, convertToString, "utf-8");
		} catch (IOException ex) {
			Context.reportError(ToolErrorReporter.getMessage("msg.couldnt.read.source", path, ex.getMessage()));
			return null;
		}
	}

	public void processFileSecure(Context cx, Scriptable scope, String path, Object securityDomain) {

		boolean isClass = path.endsWith(".class");
		Object source = readFileOrUrl(path, !isClass);

		if (source == null) {
			return;
		}

		byte[] digest = getDigest(source);
		String key = path + "_" + cx.getOptimizationLevel();
		ScriptReference ref = scriptCache.get(key, digest);
		Script script = ref != null ? ref.get() : null;

		if (script == null) {
			if (isClass) {
				script = loadCompiledScript(cx, path, (byte[]) source, securityDomain);
			} else {
				String strSrc = (String) source;
				// Support the executable script #! syntax: If
				// the first line begins with a '#', treat the whole
				// line as a comment.
				if (strSrc.length() > 0 && strSrc.charAt(0) == '#') {
					for (int i = 1; i != strSrc.length(); ++i) {
						int c = strSrc.charAt(i);
						if (c == '\n' || c == '\r') {
							strSrc = strSrc.substring(i);
							break;
						}
					}
				}
				script = loadScriptFromSource(cx, strSrc, path, 1, securityDomain);
			}
			scriptCache.put(key, digest, script);
		}

		if (script != null) {
			evaluateScript(script, cx, scope);
		}
	}

	static class ScriptReference extends SoftReference<Script> {
		String path;
		byte[] digest;

		ScriptReference(String path, byte[] digest, Script script, ReferenceQueue<Script> queue) {
			super(script, queue);
			this.path = path;
			this.digest = digest;
		}
	}

	static class ScriptCache extends LinkedHashMap<String, ScriptReference> {
		private static final long serialVersionUID = -1953395737446535918L;

		ReferenceQueue<Script> queue;
		int capacity;

		ScriptCache(int capacity) {
			super(capacity + 1, 2f, true);
			this.capacity = capacity;
			queue = new ReferenceQueue<Script>();
		}

		@Override
		protected boolean removeEldestEntry(Map.Entry<String, ScriptReference> eldest) {
			return size() > capacity;
		}

		ScriptReference get(String path, byte[] digest) {
			ScriptReference ref;
			while ((ref = (ScriptReference) queue.poll()) != null) {
				remove(ref.path);
			}
			ref = get(path);
			if (ref != null && !Arrays.equals(digest, ref.digest)) {
				remove(ref.path);
				ref = null;
			}
			return ref;
		}

		void put(String path, byte[] digest, Script script) {
			put(path, new ScriptReference(path, digest, script, queue));
		}

	}
}
