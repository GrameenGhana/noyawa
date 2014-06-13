package org.motechproject.noyawa.repository;

import java.util.List;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.isEmpty;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.GenerateView;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.noyawa.domain.Message;
import org.motechproject.noyawa.domain.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllSubscribers extends MotechBaseRepository<Subscriber> {
    @Autowired
    protected AllSubscribers(@Qualifier("dbConnector") CouchDbConnector db) {
        super(Subscriber.class, db);
    }
    
    public Subscriber findBy(String number) {
        List<Subscriber> subList = findByNumber(number);
        return isNotEmpty(subList) ? subList.get(0) : null;
    }

    @GenerateView
    public List<Subscriber> findByNumber(String queryNumber) {
        return isEmpty(queryNumber) ? null : queryView("by_number", queryNumber);
    }
}
