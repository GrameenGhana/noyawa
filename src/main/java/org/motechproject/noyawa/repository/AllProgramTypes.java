package org.motechproject.noyawa.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.noyawa.domain.ProgramType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AllProgramTypes extends MotechBaseRepository<ProgramType> {
    @Autowired
    protected AllProgramTypes(@Qualifier("dbConnector") CouchDbConnector db) {
        super(ProgramType.class, db);
    }

    public ProgramType findByCampaignShortCode(String shortCode) {
        List<ProgramType> programTypes = getAll();
        String shortCodeLowerCase = shortCode.toLowerCase();
        System.out.println("Passing ->"+shortCodeLowerCase);
        
        for (ProgramType programType : programTypes) {
            List<String> shortCodes = programType.getShortCodes();
            for (String code : shortCodes) {
                System.out.println("Code:"+code);
                
                if (code.toLowerCase().contains(shortCodeLowerCase))
                    return programType;
            }
        }
        return null;
    }

}
