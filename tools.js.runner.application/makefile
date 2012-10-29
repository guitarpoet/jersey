all: run
code: .
	git push origin master
	ssh jubuntu.local "cd /home/jack/Projects/tools.js.runner/tools.js.runner.application && git pull origin master"
scp: code projects.tmp/update_using_cas.js
	scp projects.tmp/update_using_cas.js jubuntu.local:/home/jack/Projects/tools.js.runner/tools.js.runner.application/projects.tmp
rebuild: scp
	ssh jubuntu.local "cd /home/jack/Projects/tools.js.runner/tools.js.runner.application && mvn clean package"
run: rebuild
	ssh jubuntu.local "cd /home/jack/Projects/tools.js.runner/tools.js.runner.application && ./run projects.tmp/update_using_cas.js"
test: src/main/scripts/simple.js
	./run src/main/scripts/simple.js
