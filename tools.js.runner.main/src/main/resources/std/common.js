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
String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
};
String.prototype.contains = function(str) {
	return sutils.contains(this, str);
}
Object.prototype.clone = function() {
	var json = JSON.stringify(this);
	return sutils.json(json);
}