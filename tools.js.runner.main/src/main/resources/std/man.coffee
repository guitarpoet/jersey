getDoc = (obj) ->
	if obj.doc
		print obj.doc()
	if obj.meta
		print doc.functionDoc(obj.meta)

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