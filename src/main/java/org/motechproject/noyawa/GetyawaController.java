package org.motechproject.noyawa;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.motechproject.noyawa.dao.RegistrationDao;
import org.motechproject.noyawa.domain.RegisterProgramSMS;
import org.motechproject.noyawa.domain.Subscriber;
import org.motechproject.noyawa.domain.Subscription;
import org.motechproject.noyawa.eventhandler.IncomingYawaMessage;
import org.motechproject.noyawa.msgflow.YawaAlgoSubscription;
import org.motechproject.noyawa.msgflow.YawaLongCodeRegistration;
import org.motechproject.noyawa.msgflow.YawaOutboundMessage;
import org.motechproject.noyawa.parser.RegisterProgramMessageParser;
import org.motechproject.noyawa.repository.AllProgramTypes;
import org.motechproject.noyawa.repository.AllSubscribers;
import org.motechproject.noyawa.repository.AllSubscriptions;
import org.motechproject.noyawa.service.SMSHandler;
import org.motechproject.noyawa.service.SubscriptionService;
import org.motechproject.noyawa.sms.SMSDouble;
import org.motechproject.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Controller
public class GetyawaController {

    public static final Logger logger = LoggerFactory.getLogger(GetyawaController.class);

    @Autowired
    private SMSHandler smsHandler;
    @Autowired
    private AllSubscriptions allSubscriptions;
    @Autowired
    SubscriptionService subscriptionService;
    @Autowired
    RegisterProgramMessageParser registerProgramParser;
    @Autowired
    private AllProgramTypes allProgramTypes;
    @Autowired
    private IncomingYawaMessage incomingYawaMessage;
    @Autowired
    private YawaOutboundMessage yawaOutboundMessage;
    @Autowired
    private SMSDouble sMSDouble;
    @Autowired
    private YawaLongCodeRegistration yawaLongCodeRegistration;

    DateFormat dateFormat = new SimpleDateFormat("HHmmss");
    Calendar cal = Calendar.getInstance();
    String startTime = dateFormat.format(cal.getTime());

    private String gender = null;
    private String age = null;
    private String educaLevel = null;

    @RequestMapping(value = "/getsms", method = RequestMethod.GET)
    public @ResponseBody
    String getSms(@RequestParam(value = "phone", required = true) String phone,
            @RequestParam(value = "msg", required = true) String msg) throws Exception {
        yawaOutboundMessage.smsYawaMsg(phone, msg); //UNCOMMENT THIS TO SEND OUTBOUND MESSAGES TO NEW SUBSCRIBERS
        return String.format(phone + "  " + msg);
    }

    /* @RequestMapping("/getsms/{phone}/{msg}")
     @ResponseBody
     public  String getsms(@PathVariable String phone, @PathVariable String msg) throws Exception {
     yawaOutboundMessage.smsYawaMsg(phone, msg);
     return String.format(phone + "  " + msg);
     } */
//    @RequestMapping(value = "/coughRegister", method = RequestMethod.GET)
//    public @ResponseBody
//    String registerSms(@RequestParam(value = "subNo", required = true) String subNo, @RequestParam(value = "prgm", required = true) String prgm) throws JSONException {
//        RegisterProgramSMS registerProgramSMS = registerProgramParser.parse(prgm + " " + startTime, subNo);
//        JSONObject responseObject = new JSONObject();
//        responseObject.put("phoneNumber", subNo.replaceFirst("0", "233"));
//
//        if (registerProgramSMS == null) {
//            responseObject.put("status", "Failed");
//            responseObject.put("reason", "Start Time is not valid");
//        } else {
//            registerProgramSMS.process(smsHandler);
//            responseObject.put("status", "Success");
//        }
//
//        return responseObject.toString();
//    }
    @RequestMapping(value = "/couchRegister", method = RequestMethod.GET)
    public @ResponseBody
    String registerSms(@RequestParam(value = "subNo", required = true) String subNo, @RequestParam(value = "prgm", required = true) String prgm) throws JSONException {

        logger.info("Registering on couchdb : number->" + subNo + " prgm->" + prgm);

        RegisterProgramSMS registerProgramSMS = registerProgramParser.parse(prgm + " " + startTime, subNo);

        logger.info("Program checked......");

        JSONObject responseObject = new JSONObject();
        responseObject.put("phoneNumber", subNo.replaceFirst("0", "233"));

        if (registerProgramSMS == null) {
            logger.info("Null Program");

            responseObject.put("status", "Failed");
            responseObject.put("reason", "Start Time is not valid");
        } else {
            logger.info("Program passed");

            registerProgramSMS.process(smsHandler);
            responseObject.put("status", "Success");
        }

        logger.info("Response->" + responseObject.toString());

        return responseObject.toString();
    }

    @RequestMapping(value = "/registersms", method = RequestMethod.GET)
    public @ResponseBody
    String registersms(@RequestParam(value = "phone", required = true) String phone, @RequestParam(value = "msg", required = true) String msg, @RequestParam(value = "stage", required = false, defaultValue = "1") String stage) throws Exception {

        yawaLongCodeRegistration.longCodeRegistration(phone, msg);
        return String.format(phone + "  " + msg);

        /* String response = "Send gender ( m/f ), age ( 15 - 24 ), education level ( jhs,shs,ter,n/a ) to '0546687715' ";

         if (msg.matches("Start") || msg.matches("start") || msg.matches("START")){
         sMSDouble.longcodeMessage(response, phone);
         } else  {

         gender = msg.toString().substring(0,1);
         age = msg.toString().substring(2,4);
         educaLevel = msg.toString().substring(5,8);

         if (gender.toString().matches("m") || gender.toString().matches("M") || gender.toString().matches("f") || gender.toString().matches("F")) {

         if (age.matches("15") || age.matches("15") || age.matches("16") || age.matches("17") || age.matches("18") || age.matches("19") || age.matches("15") || age.matches("20") ||
         age.matches("21") || age.matches("22") || age.matches("23") || age.matches("23"))      {

         if (educaLevel.toString().matches("jhs") || educaLevel.toString().matches("shs") || educaLevel.toString().matches("ter") || educaLevel.toString().matches("n/a")) {
         sMSDouble.longcodeMessage("Correct for hundred points, ", phone);
         }
         }

         }
         }
         return gender + " " + age + " " + educaLevel ;  */
    }

    // @Autowired
    //AllSubscribers allSubscribers;
    @Autowired
    private YawaAlgoSubscription yawaAlgoSubscription;

    private RegistrationDao registrationDao = new RegistrationDao();

    @RequestMapping(value = "/fireToCouchDb", method = RequestMethod.GET)
    public @ResponseBody
    String fireToCouchDb(@RequestParam(value = "phone", required = false) String phone, @RequestParam(value = "all", required = false, defaultValue = "y") String all) {

        logger.info("Firing to couchDb");


       // List<Subscriber> listOfSubscribers = null;
        //List<Subscription> listOfSubscription = null;

        boolean success = false;

        try {
            ResultSet resultSet = null;

            if (all != null && all.equalsIgnoreCase("y")) {

                logger.info("Firing all clients into couchDb");
                resultSet = registrationDao.getAllClients();
                resultSet.last();
                int total = resultSet.getRow();
                logger.info("Total number of clients in mysql -> " + total);

                resultSet.beforeFirst();

            } else {
                logger.info("Firing client into couchDb with a phone number ->" + phone);

                if (phone != null) {
                    resultSet = new RegistrationDao().getClientForSms(phone.trim());
                } else {
                    return "Phone number is empty";
                }
            }

//            if (resultSet != null) {
//                listOfSubscribers = allSubscribers.getAll();
//                listOfSubscription = allSubscriptions.getAll();
//            }

            ArrayList<MySqlSubscriber> arrayList = new ArrayList<MySqlSubscriber>();
            //logger.info("List of subscribers:" + listOfSubscribers.size());
            while (resultSet.next()) {
          
                arrayList.add(new MySqlSubscriber(resultSet.getString("client_number"),resultSet.getString("client_gender"), resultSet.getString("client_age"), resultSet.getString("client_education_level"), resultSet.getString("status")));

            }

            if(!arrayList.isEmpty()) {
                
                logger.info("MySql Subscribers List : "+arrayList.size());
                
                
             for(MySqlSubscriber c : arrayList) {
                 
              //  for (Subscriber s : listOfSubscribers) {
               //     if (!s.getNumber().equalsIgnoreCase(c.number)) {
                        Subscriber subscriber = new Subscriber();
                        subscriber.setNumber(c.number);
                        logger.info("Subscriber saved in the db");
               //     } else {
               //         logger.error("Subscriber found in the db");
                //    }
               // }


              //  for (Subscription sp : listOfSubscription) {
               //     if (!sp.getSubscriber().getNumber().equalsIgnoreCase(c.number)) {
                        yawaAlgoSubscription.SubscribeUserToCampaign(c.number, c.age, c.eduLevel, c.status);
                        logger.info("Susbcription saved in the db");
               //     } else {
                //        logger.error("Susbcription not saved in the db");
               //     }
               // }


                

            }
             
             success = true;
             
            }
        } catch (Exception e) {

            e.printStackTrace();

            success = false;
        }

        if (success) {
            return "Work is done, yeah !!!";
        } else {
            return "Something went wrong, check and rerun script !!!";
        }

    }
    
    
    
    
    @RequestMapping(value = "/fireAmSorry", method = RequestMethod.GET)
    public @ResponseBody
    String fireAmSorry() {
       
        
        try {
            ResultSet resultSet = null;

                logger.info("Firing  am sorry message to all subscbribers .....");
                resultSet = registrationDao.getAllClients();
                resultSet.last();
                int total = resultSet.getRow();
                logger.info("Total number of subscbribers in mysql -> " + total);

                resultSet.beforeFirst();

                String sorryMsg = "We know it is frustrating whn ur messages dont come on time but thank u 4 ur patience "
                                + "as we have worked 2 improve the No Yawa message system. We still dey 4 u!";
                
            while (resultSet.next()) {
          
                sMSDouble.outingMessage(resultSet.getString("client_number"), sorryMsg) ;
                
            }
            
            return "Finished sending am sorry message to all subscribers @ " + DateUtil.now() ;
          
        } catch (Exception e) {
            
            return "There is a problem somewhere - > "+e.getMessage();
        }
         
    }
    
    
    // class to keep  subscriber details from mysql
    public class MySqlSubscriber {

        private String number;
        private String gender;
        private String age;
        private String eduLevel;
        private String status;

        public MySqlSubscriber(String number, String gender, String age, String eduLevel, String status) {
            this.number = number;
            this.gender = gender;
            this.age = age;
            this.eduLevel = eduLevel;
            this.status = status;
        }

    }
    
    
    
}
