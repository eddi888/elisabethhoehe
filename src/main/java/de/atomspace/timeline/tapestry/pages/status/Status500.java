package de.atomspace.timeline.tapestry.pages.status;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ExceptionReporter;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;

;

public class Status500 implements ExceptionReporter {

	@Property
	private String message;

	@Inject
	private Request request;

	@Inject
	private Response response;

	@Inject
	private HttpServletRequest httpSRequest;

	public void reportException(Throwable exception) {
		message = exception.getMessage();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		response.setStatus(500);
		try {
			String body = "";
			body += "URL: " + httpSRequest.getRequestURI() + "\n";
			body += "Date: " + dateFormat.format(new Date()) + "\n";
			body += "Method: " + httpSRequest.getMethod() + "\n";
			body += "Content type: " + httpSRequest.getContentType() + "\n";

			for (String name : request.getHeaderNames()) {
				body += name + ": " + request.getHeader(name) + "\n";
			}
			for (String name : request.getParameterNames()) {
				body += name + ": " + request.getParameter(name) + "\n";
			}
			StringWriter sWriter = new StringWriter();
			PrintWriter pWriter = new PrintWriter(sWriter);
			exception.printStackTrace(pWriter);
			body += sWriter + "\n";

			message += body;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
