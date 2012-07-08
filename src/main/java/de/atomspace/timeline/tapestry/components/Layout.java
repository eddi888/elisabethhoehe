package de.atomspace.timeline.tapestry.components;

import java.util.HashMap;

import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.SymbolConstants;

/**
 * Layout component for pages of application timeline.
 */
@Import(stylesheet = "context:layout/design.css")
public class Layout {

	/**
	 * The page title, for the <title> element and the <h1>element.
	 */
	@Property
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String title;

	@Property
	private String pageName;

	@Inject
	private ComponentResources resources;

	@Property
	@Inject
	@Symbol(SymbolConstants.APPLICATION_VERSION)
	private String appVersion;

	public String getClassForPageName() {
		return resources.getPageName().equalsIgnoreCase(pageName) ? "current_page_item" : null;
	}

	public String[] getPageNames() {
		return new String[] { "Index", "Contact" };
	}

	/**
	 * Accept no Robots on Special Pages
	 * 
	 * @return
	 */
	public String getRobots() {
		HashMap<String, String> robots = new HashMap<String, String>();
		robots.put("About", "noindex");
		robots.put("Contact", "noindex");
		String robot = robots.get(resources.getPageName());

		if (robot == null) {
			robot = "index,follow";
		}
		return robot;
	}

}
