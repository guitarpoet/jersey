require 'std/date'
require 'ext/http'

taobaoUrl = config "taobao.url"
taobaoApp = config "taobao.app"
taobaoSecret = config "taobao.secret"

dateFormat = 'yyyy-MM-dd HH:mm:ss'

class TaobaoClient
	constcuctor: (@url, @app, @sec) ->
		@url = config "taobao.url" if !@url
		@app = config "taobao.app" if !@app
		@sec = config "taobao.secret" if !@sec

	sign: (params) ->
		s = smap(params)
		str = ""
		for key in s.keySet().toArray()
			str += key + s.get key
		hash_hmac("md5", str, taobaoSecret).toUpperCase()

	encode: (str) ->
		return str if typeof str != 'string'
		return encodeURIComponent str

	execute: (method, params) ->
		#t = this.encode Date.parse("now").toString(dateFormat)
		t = "2012-11-21 23:36:28"
		requestParams = {
			timestamp: t,
			v: "2.0",
			app_key: taobaoApp,
			sign_method: 'hmac',
			partner_id: 'top-sdk-js',
			format: 'json',
			method: method,
		}

		toSign = {}

		for p of params
			toSign[p] = params[p]

		for p of requestParams
			toSign[p] = requestParams[p]

		sign = this.sign toSign

		requestParams.sign = sign

		rps = []

		for p of requestParams
			rps.push this.encode(p) + "=" + this.encode(requestParams[p])

		url = "#{taobaoUrl}?#{rps.join('&')}"
		JSON.parse http.post(url, params)

taobao = new TaobaoClient()
