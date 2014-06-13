/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.motechproject.noyawa.service;

import java.util.HashMap;
import java.util.Map;
import org.motechproject.noyawa.domain.Subscriber;

/**
 *
 * @author liman
 */
public class SubscriberServiceImpl implements SubscriberService {

    @Override
    public Subscriber registerSubscriber(String phone) {
        
        Subscriber subscriber = new Subscriber(phone);
        
        return subscriber;
    }
    
}
