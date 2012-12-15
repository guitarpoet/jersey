package com.thinkingcloud.tools.js.runner.jetty;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.SessionHandler;
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
		jetty.setSessionIdManager(new HashSessionIdManager());
		jetty.setHandler(new SessionHandler() {
			@Override
			public void doHandle(String target, org.eclipse.jetty.server.Request baseRequest,
			        HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws IOException,
			        ServletException {
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
