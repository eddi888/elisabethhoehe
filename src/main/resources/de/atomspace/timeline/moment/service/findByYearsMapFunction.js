function(doc) {
	if (doc.years) {
		for(var idx in doc.years) {
		    emit(doc.years[idx], doc._id);
		}
	}
}