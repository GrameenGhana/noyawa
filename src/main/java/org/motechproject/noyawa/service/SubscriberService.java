/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.motechproject.noyawa.service;

import org.motechproject.noyawa.domain.Subscriber;

/**
 *
 * @author liman
 */
public interface SubscriberService {
    
    public Subscriber registerSubscriber(String phone);
}
