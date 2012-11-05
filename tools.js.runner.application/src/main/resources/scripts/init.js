print = function(x) {
	java.lang.System.out.print(x)
};
println = function(x) {
	java.lang.System.out.println(x);
};
printf = function() {
	utils.printf.apply(utils, arguments)
};
console = {
	log: function() {
		logger.info.apply(logger, arguments);
	},
	info : function() {
		logger.info.apply(logger, arguments);
	},
	warn : function() {
		logger.warn.apply(logger, arguments);
	},
	error : function() {
		logger.error.apply(logger, arguments);
	}
}

require = function(name) {
	utils.require.apply(utils, arguments);
}

toi = function(n) {
	return new java.lang.Integer(n);
}

String.prototype.isBlank = function() {
	return sutils.isBlank(this);
}
String.prototype.capitalize = function() {
	return sutils.capitalize(this);
}
String.prototype.explode = function(sep) {
	return sutils.split(this, sep);
}
String.prototype.contains = function(str) {
	return sutils.contains(this, str);
}