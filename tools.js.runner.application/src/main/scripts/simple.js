utils.require('underscore');
var e = utils.enumerateResource("file:/tmp/result.txt");

while(e.hasMoreElements()) {
	var line = e.nextElement();
	s = utils.split(line, "\t")[1];
	json = utils.parseJSONData(s);
	s = utils.getKeys(json);
	$.each(utils.toJS(s), function(v){
		utils.println(v);
	});
}