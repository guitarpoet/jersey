mvcContext = appContext "ext/mvc_context.xml"
requestUtils = bean "requestUtils", mvcContext

class Router
	constructor: (@name) ->
		@suffix = ".ftl"
		@prefix = "views/"
		@errorPage = "error"
		@notFoundPage = "404"
		@gets = smap()
		@posts = smap()
		@dels = smap()
		@puts = smap()

	get: (pattern, handler) ->
		@gets.put pattern, handler

	post: (pattern, handler) ->
		@posts.put pattern, handler

	del: (pattern, handler) ->
		@dels.put pattern, handler

	put: (pattern, handler) ->
		@puts.put pattern, handler

	service: (pattern, handler) ->
		this.get pattern, handler
		this.post pattern, handler
	
	getMatched: (map, target)->
		ret = []
		logger.info "Matching for target {}", target
		for pattern in map.keySet().toArray()
			logger.info "Pattern {} Target {}", pattern, target
			match = sutils.match(pattern, target)
			logger.info match
			if match
				logger.info match.groups()
				if match.groups() > 1
					logger.info "Matching controller object's function {}", match.group(1)
					func = map.get(pattern)[match.group(1)]
					if match.groups() > 2
						func.groups = []
						for i in [2..match.groups()]
							func.groups.push match.group(i)
					ret.push map.get(pattern)[match.group(1)]
				else
					ret.push map.get(pattern)
			if sutils.antMatch(pattern, target)
				ret.push map.get(pattern)
		return ret
	
	getHandlers: (method, target)->
		switch "#{method.toLowerCase()}"
			when "get" then return this.getMatched(@gets, target)
			when "post" then return this.getMatched(@posts, target)
			when "put" then return this.getMatched(@puts, target)
			when "delete" then return this.getMatched(@dels, target)
		logger.warn "Nothing matched for target {} for method {}", target, method
		return []

	show: (view, out, model) ->
		logger.info "Directing to view #{view}"
		out.println template("classpath:#{@prefix+view+@suffix}", model)
		out.close()
	
	handle: (target, baseRequest, request, response)->
		logger.info "Incomming request {} for target {}", request, target
		handlers = this.getHandlers(request.getMethod(), target)
		model = {
			target: target,
			baseRequest: baseRequest,
			request: request,
			response: response
		}
		model.self = model
		try
			if handlers.length # If the handler is found
				e = request.getAttributeNames()
				while e.hasMoreElements()
					n = e.nextElement()
					model[n] = request.getAttribute(n)
				handler = handlers[0]
				model.args = handler.groups if handler.groups
				view = handlers[0](request, response, model)
				if view
					this.show view, response.getWriter(), model
			else # If there wasn't any handler here, pass through to the classpath
				#TODO: This will expose all the files to public, will need a restrict way to handle it
				logger.debug "Nothing matched target #{target}, passing through"
				requestUtils.pass target, response
		catch ex
			model.error = ex
			if "#{ex.javaException.getClass().getSimpleName()}" == "FileNotFoundException"
				logger.warn "Not Found, " + ex.javaException
				this.show @notFoundPage, response.getWriter(), model
			else
				this.show @errorPage, response.getWriter(), model
