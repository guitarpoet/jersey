package com.thinkingcloud.tools.js.runner.jetty;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;

import com.thinkingcloud.tools.js.runner.core.NewGlobal;

public class JettyServer {

	private Server jetty;

	private Function handler;

	private NewGlobal global;

	private Context context;

	public JettyServer(Function handler, NewGlobal global) {
		if (handler == null) {
			throw new IllegalArgumentException("Handler can't be null!!!");
		}
		this.global = global;
		this.handler = handler;
		this.context = Context.getCurrentContext();
	}

	public void listen(int port) throws Exception {
		jetty = new Server(port);
		jetty.setHandler(new AbstractHandler() {
			@Override
			public void handle(String target, Request baseRequest, HttpServletRequest request,
			        HttpServletResponse response) throws IOException, ServletException {
				handler.call(context, global, global, new Object[] { target, baseRequest, request, response });
			}
		});
		jetty.start();
	}
}
