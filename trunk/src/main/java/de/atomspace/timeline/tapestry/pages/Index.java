package de.atomspace.timeline.tapestry.pages;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.internal.services.CookieSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.Response;
import org.springframework.beans.factory.annotation.Autowired;

import de.atomspace.timeline.moment.domain.Moment;
import de.atomspace.timeline.moment.service.MomentService;
import de.atomspace.timeline.year.domain.Year;
import de.atomspace.timeline.year.service.YearService;

public class Index {

    @Autowired
    @Inject
    MomentService momentService;

    @Inject
    YearService yearService;

    @SessionAttribute
    String visibleKey;

    @Inject
    RequestGlobals _requestGlobals;

    @Inject
    CookieSource cookieSource;

    @Inject
    Response response;

    @Property
    @SessionAttribute
    Integer currentYear;

    @Property
    List<Year> years;

    @Property
    Year year;

    @Property
    List<Moment> moments;

    @Property
    Moment moment;

    /**
     * StartPage
     */
    void onActivate() throws IOException {
        if (currentYear == null) {
            currentYear = Calendar.getInstance().get(Calendar.YEAR);
            // rememberYearBefore=currentYear;
        }
        this.onActivate(currentYear.toString());
    }

    /**
     * Page with Year Parameter
     * 
     * @throws IOException
     */
    void onActivate(String year) throws IOException {
        try {
            currentYear = Integer.valueOf(year);
        } catch (Exception e) {
            response.sendError(404, "Page " + year + " not found!");
            return;
        }
        if (currentYear < 1926) {
            response.sendError(404, "Page " + year + " not found!");
            return;
        }
        if (currentYear > new GregorianCalendar().get(Calendar.YEAR)) {
            response.sendError(404, "Page " + year + " not found!");
            return;
        }
        years = yearService.getYears(currentYear);
        moments = momentService.findByYears(currentYear, getVisibleKey());
    }

    public String getVisibleKey() {
        String visibleKeyCookie = null;
        Cookie[] cookies = cookieSource.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase("visibleKey")) {
                    visibleKeyCookie = cookie.getValue();
                }
            }
        }
        if (visibleKeyCookie == null) {
            Cookie cookie = new Cookie("visibleKey", UUID.randomUUID().toString());
            cookie.setMaxAge(31104000); // 360days
            _requestGlobals.getHTTPServletResponse().addCookie(cookie);
            visibleKeyCookie = cookie.getValue();
        }
        return visibleKeyCookie;
    }
}
