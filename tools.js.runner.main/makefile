deploy: rebuild
	cp target/tools.js.runner.main-1.0-SNAPSHOT.jar /Applications/jsconsole/lib
coffee:
	find . -name "*.coffee" -exec coffee -b -c {} \;
rebuild: coffee
	mvn clean package
