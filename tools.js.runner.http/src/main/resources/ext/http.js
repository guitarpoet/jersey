httpContext = appContext("ext/http_context.xml");
http = bean("http", httpContext)
http.geta = function(url, options) {
	var headers = options.headers || http.defaultHeaders();
	var callback = options.callback;
	job("GET:" + url, function() {
		return http.get(url, headers);
	}, callback);
}
http.posta = function(url, data, options) {
	var headers = options.headers || http.defaultHeaders();
	var callback = options.callback;
	job("POST:" + url, function() {
		return http.post(url, headers, data || {});
	}, callback);
}