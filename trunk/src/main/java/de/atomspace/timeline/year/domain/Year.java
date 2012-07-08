package de.atomspace.timeline.year.domain;

public class Year {

	private String year;
	private String view;
	private String style;

	public Year(String year, String style, String view) {
		this.year = year;
		this.style = style;
		this.view = view;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
}
