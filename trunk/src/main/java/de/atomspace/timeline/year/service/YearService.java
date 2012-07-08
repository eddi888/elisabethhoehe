package de.atomspace.timeline.year.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.springframework.stereotype.Service;

import de.atomspace.timeline.year.domain.Year;

@Service
public class YearService {
	public ArrayList<Year> getYears(Integer currentYear) {
		ArrayList<String> yearsL = new ArrayList<String>();
		ArrayList<String> years = new ArrayList<String>();

		int yearMin = 1926; // mean before 1927
		int yearMax = new GregorianCalendar().get(Calendar.YEAR);
		int countL = 10;
		int countR = 10;

		if ((currentYear - countL) < yearMin) {
			int count = countL + countR;
			countL = currentYear - yearMin;
			countR = count - countL;
		}
		if ((currentYear + countR) > yearMax) {
			int count = countL + countR;
			countR = yearMax - currentYear;
			countL = count - countR;
		}

		// Left Side
		for (int i = 0; i <= countL; i++) {
			yearsL.add(currentYear - i + "");
		}
		for (int i = yearsL.size() - 1; i > 0; i--) {
			years.add(yearsL.get(i));
		}

		// Right Side
		for (int i = 0; i <= countR; i++) {
			years.add(currentYear + i + "");
		}

		// return years;
		ArrayList<Year> line = new ArrayList<Year>();
		for (String year : years) {
			Year point;
			if (currentYear.toString().equalsIgnoreCase(year)) {
				point = new Year(year, "navi_current", year);
			} else {
				point = new Year(year, "", year);
			}
			if (year.equals(yearMin + "")) {
				point.setView("<1927");
			}
			line.add(point);
		}
		return line;
	}
}
