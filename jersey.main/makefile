deploy: rebuild
	cp target/jersey.main-1.0.1.jar /Applications/jersey/lib
coffee:
	find . -name "*.coffee" -exec coffee -b -c {} \;
rebuild: coffee
	mvn clean package
