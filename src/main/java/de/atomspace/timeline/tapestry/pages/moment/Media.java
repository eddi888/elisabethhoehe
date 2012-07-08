package de.atomspace.timeline.tapestry.pages.moment;


import java.io.IOException;
import java.io.InputStream;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Response;
import org.ektorp.AttachmentInputStream;
import org.ektorp.DocumentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import de.atomspace.timeline.moment.service.MomentService;

/**
 * Return an Image from CouchDB Attachments
 *
 */
public class Media {

	@Inject
	@Autowired
	MomentService momentService;
	
	@Inject
    Response response;
	
	
	void onActivate() {
		//ONLY A STEAM
	}
	
	StreamResponse onActivate(String momentId, String attachmentId) throws IOException {
		try{
			final AttachmentInputStream inputStream = momentService.getAttachment(momentId, attachmentId);

			if(inputStream == null){
				return null;
			}else{
				return new StreamResponse() {
	
					public void prepareResponse(Response response) {
						response.setHeader("Content-Length", inputStream.getContentLength()+"" );
					}
	
					public String getContentType() {
						return inputStream.getContentType();
					}
	
					public InputStream getStream() throws IOException {
						return inputStream;
					}
				};
			}
		}catch(DocumentNotFoundException e){
			response.sendError(404, "Media Not Found!");
			return null;	
		}
	}
	
}