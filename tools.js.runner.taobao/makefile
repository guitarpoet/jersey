all: target/tools.js.runner.taobao-1.0-SNAPSHOT.jar
	cp target/tools.js.runner.taobao-1.0-SNAPSHOT.jar /tmp/lib
coffee:
	find . -name "*.coffee" -exec coffee -b -c {} \;
target/tools.js.runner.taobao-1.0-SNAPSHOT.jar: coffee
	mvn clean package
