var getDoc, man;

getDoc = function(obj) {
  if (obj.doc) {
    print(obj.doc());
    return;
  }
  if (typeof obj === 'function') {
    if (obj.meta) {
      return print(doc.functionDoc(obj.meta));
    } else {
      return print(doc.functionDoc(obj));
    }
  } else {
    if (obj.meta) return print(doc.moduleDoc(obj.meta));
  }
};

man = function(name) {
  var sub;
  sub = global()[name];
  if (sub) {
    return getDoc(sub);
  } else {
    if (name) return getDoc(name);
  }
};

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
};
