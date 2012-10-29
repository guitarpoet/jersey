print=function(x) {java.lang.System.out.print(x)};
println=function(x) {java.lang.System.out.println(x);};
console = {
		log: function() {logger.info.apply(logger, arguments);},
		info: function() {logger.info.apply(logger, arguments);},
		warn: function() {logger.warn.apply(logger, arguments);},
		error: function() {logger.error.apply(logger, arguments);}
}