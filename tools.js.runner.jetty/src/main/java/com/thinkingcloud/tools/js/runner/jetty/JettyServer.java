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
import org.mozilla.javascript.Scriptable;

import com.thinkingcloud.tools.js.runner.core.NewGlobal;

public class JettyServer {

	private Server jetty;

	private Scriptable handler;

	private NewGlobal global;

	public JettyServer(Scriptable handler, NewGlobal global) {
		if (handler == null) {
			throw new IllegalArgumentException("Handler can't be null!!!");
		}
		this.global = global;
		this.handler = handler;
	}

	public void listen(int port) throws Exception {
		jetty = new Server(port);
		jetty.setHandler(new AbstractHandler() {
			@Override
			public void handle(String target, Request baseRequest, HttpServletRequest request,
			        HttpServletResponse response) throws IOException, ServletException {
				if (handler instanceof Function) {
					((Function) handler).call(Context.enter(), global, handler, new Object[] { target, baseRequest,
					        request, response });
				} else {
					if (handler.getPrototype() != null) {
						Function handle = (Function) handler.getPrototype().get("handle", handler);
						handle.call(Context.enter(), global, handler, new Object[] { target, baseRequest, request,
						        response });

					}
				}
			}
		});
		jetty.start();
	}
}
