var Router, mvcContext, requestUtils;

mvcContext = appContext("ext/mvc_context.xml");

requestUtils = bean("requestUtils", mvcContext);

Router = (function() {

  function Router(name) {
    this.name = name;
    this.suffix = ".ftl";
    this.prefix = "views/";
    this.errorPage = "error";
    this.notFoundPage = "404";
    this.gets = smap();
    this.posts = smap();
    this.dels = smap();
    this.puts = smap();
  }

  Router.prototype.get = function(pattern, handler) {
    return this.gets.put(pattern, handler);
  };

  Router.prototype.post = function(pattern, handler) {
    return this.posts.put(pattern, handler);
  };

  Router.prototype.del = function(pattern, handler) {
    return this.dels.put(pattern, handler);
  };

  Router.prototype.put = function(pattern, handler) {
    return this.puts.put(pattern, handler);
  };

  Router.prototype.service = function(pattern, handler) {
    this.get(pattern, handler);
    return this.post(pattern, handler);
  };

  Router.prototype.getMatched = function(map, target) {
    var pattern, ret, _i, _len, _ref;
    ret = [];
    logger.info("Matching map {} for target {}", map, target);
    _ref = map.keySet().toArray();
    for (_i = 0, _len = _ref.length; _i < _len; _i++) {
      pattern = _ref[_i];
      if (sutils.match(pattern, target) || sutils.antMatch(pattern, target)) {
        ret.push(map.get(pattern));
      }
    }
    return ret;
  };

  Router.prototype.getHandlers = function(method, target) {
    switch ("" + (method.toLowerCase())) {
      case "get":
        return this.getMatched(this.gets, target);
      case "post":
        return this.getMatched(this.posts, target);
      case "put":
        return this.getMatched(this.puts, target);
      case "delete":
        return this.getMatched(this.dels, target);
    }
    logger.warn("Nothing matched for target {} for method {}", target, method);
    return [];
  };

  Router.prototype.show = function(view, out, model) {
    logger.info("Directing to view " + view);
    out.println(template("classpath:" + (this.prefix + view + this.suffix), model));
    return out.close();
  };

  Router.prototype.handle = function(target, baseRequest, request, response) {
    var e, handlers, model, n, view;
    logger.info("Incomming request {} for target {}", request, target);
    handlers = this.getHandlers(request.getMethod(), target);
    model = {
      target: target,
      baseRequest: baseRequest,
      request: request,
      response: response
    };
    model.self = model;
    try {
      if (handlers.length) {
        e = request.getAttributeNames();
        while (e.hasMoreElements()) {
          n = e.nextElement();
          model[n] = request.getAttribute(n);
        }
        view = handlers[0](request, response, model);
        if (view) return this.show(view, response.getWriter(), model);
      } else {
        return requestUtils.pass(target, response);
      }
    } catch (ex) {
      model.error = ex;
      if (("" + (ex.javaException.getClass().getSimpleName())) === "FileNotFoundException") {
        return this.show(this.notFoundPage, response.getWriter(), model);
      } else {
        return this.show(this.errorPage, response.getWriter(), model);
      }
    }
  };

  return Router;

})();
