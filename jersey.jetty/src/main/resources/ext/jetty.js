jettyContext = appContext("classpath:ext/jetty_context.xml");
jetty = bean("jetty", jettyContext);