require("std/date");
print("Just hello");
print(context.toString());
logger.info("Tomorrow is {}", (1).day().fromNow().toString());
logger.info("Tomorrow is {}", Date.parse("tomorrow").toString());

function greetings(name) {
	var g = "Hello " + name;
	print(g);
	return g;
}

function callback(data, job) {
	print("Job " + job + " is finished! Result is " + data);
}

for(var i = 1; i < 10; i++) {
	job("job" + i, greetings, callback, "world" + i);
}
