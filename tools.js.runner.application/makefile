code: .
	git push origin master
	ssh jubuntu.local "cd /home/jack/Projects/tools.js.runner/tools.js.runner.application && git pull origin master"
rebuild: code
	ssh jubuntu.local "cd /home/jack/Projects/tools.js.runner/tools.js.runner.application && mvn clean package"
scp_cas: rebuild projects.tmp/update_using_cas.js
	scp projects.tmp/update_using_cas.js jubuntu.local:/home/jack/Projects/tools.js.runner/tools.js.runner.application/projects.tmp
run_cas: scp_cas
	ssh jubuntu.local "cd /home/jack/Projects/tools.js.runner/tools.js.runner.application && ./run projects.tmp/update_using_cas.js"
scp_solr: rebuild projects.tmp/update_solr.js
	scp projects.tmp/update_solr.js jubuntu.local:/home/jack/Projects/tools.js.runner/tools.js.runner.application/projects.tmp
run_solr: scp_solr
	ssh jubuntu.local "cd /home/jack/Projects/tools.js.runner/tools.js.runner.application && ./run projects.tmp/update_solr.js"
test: src/main/scripts/simple.js
	./run -c src/main/scripts/test.properties src/main/scripts/simple.js
cup: projects.tmp/fetch_cupboard_no.js
	./run projects.tmp/fetch_cupboard_no.js
remote_test: rebuild
	ssh jubuntu.local "cd /home/jack/Projects/tools.js.runner/tools.js.runner.application && make test"
