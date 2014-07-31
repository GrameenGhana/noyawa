package org.motechproject.noyawa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Locale locale, Model model) {
        logger.info("Welcome home! The client locale is {}.", locale);

        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

        String formattedDate = dateFormat.format(date);

        model.addAttribute("serverTime", formattedDate);
        model.addAttribute("serverFormat", dateFormat);

        return "home";
    }

    @RequestMapping(value = "/fireCouchFromExcel", method = RequestMethod.GET)
    public @ResponseBody
    String fireCouchFromExcel(@RequestParam(value = "filename") String filename) {

        System.out.println("Procesiing file :"+filename);
        
        try {

            FileInputStream file = new FileInputStream(new File("/var/www/noyawa/public/storage/"+filename));

            //Get the workbook instance for XLS file 
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows from first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                Cell cell = row.getCell(0);

                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_BOOLEAN:
                        System.out.print("no" + "\t\t");
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                       String old = String.valueOf(cell.getNumericCellValue());
                    
                       old = old.replace(".", "").replace("E8", "");
                       System.out.println("Phone: "+ old.toString());
                       
                       Thread.sleep(7000);
                       fire("233" + old.toString());
                       
                        break;
                    case Cell.CELL_TYPE_STRING:
                        System.out.print( cell.getStringCellValue() + "\t\t");
                        break;
                }
               
                System.out.println("");
            }
            file.close();

        }  catch (Exception e) {
            e.printStackTrace();
        }

        
        return "All excel data processed.";
    }
    
    
     public void fire(String phonenumber) {

        System.out.println("Firing number :" + phonenumber);

        try {

            String url = "http://41.191.245.72:8080/noyawa/fireToCouchDb?all=n&phone=" + phonenumber;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

		//add request header
            //con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = con.getResponseCode();
           // System.out.println("Sending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());

        } catch (Exception e) {
        }

    }


}
