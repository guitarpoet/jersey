getDoc = (obj) ->
	if obj.doc
		print obj.doc()
		return
	if typeof(obj) == 'function'
		if obj.meta
			print doc.functionDoc(obj.meta)
		else
			print doc.functionDoc obj
	else
		if obj.meta
			print doc.moduleDoc(obj.meta)

man = (name)->
	sub = global()[name]
	if sub
		getDoc sub
	else
		if name
			getDoc name

man.meta = {
	functionName: "man",
	doc: "Get the manual for subject",
	parameters: [
		{
			name: "subject",
			type: "string",
			doc: "The subject to get the manual."
		}
	]
}
