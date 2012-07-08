package de.atomspace.timeline.tapestry.pages.moment;

import java.io.IOException;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Response;
import org.ektorp.DocumentNotFoundException;

import de.atomspace.timeline.moment.domain.Moment;
import de.atomspace.timeline.moment.service.MomentService;

public class View {

	@Inject
	MomentService momentService;

	@Inject
	Response response;

	@Property
	Moment moment;

	void onActivate() throws IOException {
		if(moment==null){
			response.sendError(404, "Moment Not Found!");
		}
			
	}

	void onActivate(String id) throws IOException {
		try {
			moment = momentService.findOneById(id);
		} catch (DocumentNotFoundException e) {
			response.sendError(404, "Moment Not Found!");
		}
	}
}
