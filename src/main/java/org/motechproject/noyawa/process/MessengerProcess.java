package org.motechproject.noyawa.process;

import org.motechproject.noyawa.domain.MessageBundle;
import org.motechproject.noyawa.domain.ProgramMessage;
import org.motechproject.noyawa.domain.Subscription;
import org.motechproject.noyawa.repository.AllProgramMessages;
import org.motechproject.noyawa.repository.AllSubscriptions;
import org.motechproject.noyawa.service.SMSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessengerProcess extends BaseSubscriptionProcess {
    private static final Logger logger = LoggerFactory.getLogger(MessengerProcess.class);
    private AllProgramMessages allProgramMessages;
    private AllSubscriptions allSubscriptions;

    @Autowired
    protected MessengerProcess(SMSService smsService,
                               MessageBundle messageBundle,
                               AllProgramMessages allProgramMessages,
                               AllSubscriptions allSubscriptions) {

        super(smsService, messageBundle);
        this.allProgramMessages = allProgramMessages;
        this.allSubscriptions = allSubscriptions;
    }

    public void process(Subscription subscription, String messageKey) {
        ProgramMessage message = allProgramMessages.findBy(messageKey);
        if (message == null) return;
        logger.info("Testing Message path .......................................................");
        sendMessage(subscription, message.getContent());
        logger.info("Messages path still locating ...............................................");
    }
}
