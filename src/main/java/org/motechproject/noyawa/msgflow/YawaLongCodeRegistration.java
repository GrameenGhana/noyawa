package org.motechproject.noyawa.msgflow;

import org.motechproject.noyawa.constant.YawaConstant;
import org.motechproject.noyawa.dao.RegistrationDao;
import org.motechproject.noyawa.sms.SMSDouble;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.motechproject.noyawa.domain.Subscriber;


public class YawaLongCodeRegistration {
    private static final Logger logger = LoggerFactory.getLogger(YawaLongCodeRegistration.class);

    private YawaRegistrationMessages yawaRegistrationMessages = new YawaRegistrationMessages();
    private RegistrationDao registrationDao = new RegistrationDao();
    private YawaConstant yawaConstant = new YawaConstant();

    private String gender = null;
    private String age = null;
    private String educaLevel = null;

    Date date = new Date();
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    ResultSet resultSet = null;
    String lphone = null;
    String lgender = null;
    String lage = null;
    String ledulevel = null;
    String lstatus = null;

    @Autowired
    private SMSDouble sMSDouble;

    @Autowired
    private YawaAlgoSubscription yawaAlgoSubscription;

    public void longCodeRegistration(String phone, String msg) throws Exception{
        logger.info(String.format("Sending Message From The Long Code ::  (%S) To Originator  (%S) Message ", phone, msg));
        
       
        if(msg.toString().trim().matches(yawaConstant.bigStart()) || msg.toString().trim().matches(yawaConstant.smallStart()) || msg.toString().trim().matches(yawaConstant.smallStart().toUpperCase())
                || msg.toString().trim().matches(yawaConstant.bigYawa()) || msg.toString().trim().matches(yawaConstant.smallYawa()) || msg.toString().trim().matches(yawaConstant.smallYawa().toUpperCase())){
            
            //handles first stage of registration - stage 1 : user sends start
            
            if(registrationDao.getClientNumber(phone.trim()) == null) { //checks for non-existing user
                sMSDouble.longcodeMessage(yawaRegistrationMessages.LongCodeStart, phone); // sends user sms to continue to stage 2
                logger.info("User with phone number ->"+phone+" not subscribed");
                logger.info("The message " + yawaRegistrationMessages.LongCodeStart + " is sent to " + phone.trim());
                registrationDao.insertClientSms(phone.trim());  // registers user into mysql
                
                logger.info("User saved in mysql DB");
                
            } else if(registrationDao.getClientNumber(phone.trim()).matches(phone.toString().trim())){  //checks for existing user
                logger.info("Subscriber found with phone ->"+phone);
                
                sMSDouble.longcodeMessage(yawaRegistrationMessages.LongCodeStart, phone); 
            }
        } else if (msg.toString().trim().matches("s") || msg.toString().trim().matches("S")){ //subscribes into couchdb : campaign
            
            logger.info("Subscriber susbcribing to receive campaign via sms");
            
            resultSet = registrationDao.getClientForSms(phone.trim());
            while (resultSet.next()){
                lphone = resultSet.getString("client_number");
                lgender = resultSet.getString("client_gender");
                lage = resultSet.getString("client_age");
                ledulevel = resultSet.getString("client_education_level");
                lstatus = resultSet.getString("status");
                yawaAlgoSubscription.SubscribeUserToCampaign(lphone, lage, ledulevel, lstatus);
                sMSDouble.longcodeMessage(yawaRegistrationMessages.afterSms, phone);
                 logger.info("Subscription for phone ->"+lphone +" is done");
            }

            System.out.println("The message " + yawaRegistrationMessages.afterSms + " is sent to " + phone);

        } else if(msg.toString().trim().matches(yawaConstant.getVoice()) || msg.toString().trim().matches(yawaConstant.getVoice().toLowerCase()) || msg.toString().trim().matches(yawaConstant.getVoice().toUpperCase())){
             logger.info("Subscriber susbcribing to receive campaign via voice");
            
            sMSDouble.longcodeMessage(yawaRegistrationMessages.afterVoiceSms + " via Voice!", phone);
            System.out.println("The message " + yawaRegistrationMessages.afterVoiceSms + " is sent to " + phone);
            registrationDao.goVoice(phone, "1", dateFormat.format(date));
        } else  {

            gender = msg.toString().substring(0,1);
            age = msg.toString().substring(2,4);
            educaLevel = msg.toString().substring(5,8);
            
            logger.info("Subscriber details : gender->"+gender +" ; age->"+age+"; educaLevel->"+educaLevel);

            if (gender.toString().matches("m") || gender.toString().matches("M") || gender.toString().matches("f") || gender.toString().matches("F")) {

                if (age.matches("15") || age.matches("15") || age.matches("16") || age.matches("17") || age.matches("18") || age.matches("19") || age.matches("15") || age.matches("20") ||
                        age.matches("21") || age.matches("22") || age.matches("23") || age.matches("24"))      {

                    if (educaLevel.toString().matches("jhs") || educaLevel.toString().matches("shs") || educaLevel.toString().matches("ter") || educaLevel.toString().matches("n/a")) {
                         registrationDao.getClientLongCode(gender, age, educaLevel, phone);
                         sMSDouble.longcodeMessage(yawaRegistrationMessages.LongCodeSuccess, phone);
                        logger.info(String.format("Sending Message From The Long Code ::  (%S) To Originator  (%S) Message ", phone, msg));
                        
                        //registers into couchdb
                        
                        Subscriber subscriber = new Subscriber(); 
                        subscriber.setNumber(phone); // saves subscriber
                        
                        logger.info("Subscriber saved into couchDb!");
                    }
                }

            }
        }

    }

}
