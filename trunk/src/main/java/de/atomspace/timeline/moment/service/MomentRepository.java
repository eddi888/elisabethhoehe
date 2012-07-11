package de.atomspace.timeline.moment.service;

import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.atomspace.timeline.moment.domain.Moment;

@Component
public class MomentRepository extends CouchDbRepositorySupport<Moment> {

    @Autowired
    protected MomentRepository(CouchDbConnector db) {
        super(Moment.class, db);
        initStandardDesignDocument();
    }

    @GenerateView
    public List<Moment> findByYear(int year) {
        return queryView("by_year", year);
    }

    @GenerateView
    public List<Moment> findByVisibleKey(String visibleKey) {
        return queryView("by_visibleKey", visibleKey);
    }

    CouchDbConnector getDb() {
        return this.db;
    }

    @View(name = "by_years", map = "classpath:/de/atomspace/timeline/moment/service/findByYearsMapFunction.js")
    public List<Moment> findByYears(int year) {
        return queryView("by_years", year);
    }

}
