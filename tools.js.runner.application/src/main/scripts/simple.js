utils.require('underscore');

console.log(java.lang.System.getProperties());
console.log("test {}", "adsf");
logger.info("test {}", "asdf");
logger.info("test {}", "中文");
println("中文");
console.log("test {}", "asdf".capitalize());
console.log("".isBlank());
printf("Time is {0}", new Date().toString());
printf("Int for {0} is {1}", 1234567, toi(1234567));
console.log("a,b,c,d".split(","));
var map = {};
map.a = 1;
map.b = 2;
console.log(new java.util.HashMap(map));
