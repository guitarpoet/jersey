var TaobaoClient, dateFormat, taobao, taobaoApp, taobaoSecret, taobaoUrl;

require('std/date');

require('ext/http');

taobaoUrl = config("taobao.url");

taobaoApp = config("taobao.app");

taobaoSecret = config("taobao.secret");

dateFormat = 'yyyy-MM-dd HH:mm:ss';

TaobaoClient = (function() {

  function TaobaoClient() {}

  TaobaoClient.prototype.constcuctor = function(url, app, sec) {
    this.url = url;
    this.app = app;
    this.sec = sec;
    if (!this.url) this.url = config("taobao.url");
    if (!this.app) this.app = config("taobao.app");
    if (!this.sec) return this.sec = config("taobao.secret");
  };

  TaobaoClient.prototype.sign = function(params) {
    var key, s, str, _i, _len, _ref;
    s = smap(params);
    str = "";
    _ref = s.keySet().toArray();
    for (_i = 0, _len = _ref.length; _i < _len; _i++) {
      key = _ref[_i];
      str += key + s.get(key);
    }
    return hash_hmac("md5", str, taobaoSecret).toUpperCase();
  };

  TaobaoClient.prototype.encode = function(str) {
    if (typeof str !== 'string') return str;
    return encodeURIComponent(str);
  };

  TaobaoClient.prototype.execute = function(method, params) {
    var p, requestParams, rps, sign, t, toSign, url;
    t = "2012-11-21 23:36:28";
    requestParams = {
      timestamp: t,
      v: "2.0",
      app_key: taobaoApp,
      sign_method: 'hmac',
      partner_id: 'top-sdk-js',
      format: 'json',
      method: method
    };
    toSign = {};
    for (p in params) {
      toSign[p] = params[p];
    }
    for (p in requestParams) {
      toSign[p] = requestParams[p];
    }
    sign = this.sign(toSign);
    requestParams.sign = sign;
    rps = [];
    for (p in requestParams) {
      rps.push(this.encode(p) + "=" + this.encode(requestParams[p]));
    }
    url = "" + taobaoUrl + "?" + (rps.join('&'));
    return JSON.parse(http.post(url, params));
  };

  return TaobaoClient;

})();

taobao = new TaobaoClient();
