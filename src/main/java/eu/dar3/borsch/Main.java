package eu.dar3.borsch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class Main {

//    private final MessageSource messages;

    public static void main(String[] args) throws IOException {

        /*Properties properties = new Properties();
        properties.load(Main.class.getClassLoader().getResourceAsStream("application.properties"));
        Locale locale = new Locale(properties.getProperty("app.language"));
        ResourceBundle exampleBundle = ResourceBundle.getBundle("messages", locale);

        System.out.println("exampleBundle.getString(\"currency\") = " + exampleBundle.getString("currency"));*/

/*
        assertEquals(exampleBundle.getString("currency"), "polish zloty");
        assertEquals(exampleBundle.getObject("toUsdRate"), new BigDecimal("3.401"));
        assertArrayEquals(exampleBundle.getStringArray("cities"), new String[]{"Warsaw", "Cracow"});
*/

/*
        Calendar cal1 = Calendar.getInstance(); // creates calendar
        cal1.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal1.setTime(new Date());               // sets calendar time/date
        cal1.add(Calendar.HOUR_OF_DAY, 24);      // adds one hour
        Calendar cal2 = Calendar.getInstance(); // creates calendar
        cal2.setTimeZone(TimeZone.getTimeZone("UTC"));

        Instant inst1 = cal1.getTime().toInstant();
        System.out.println("cal1.getTime() = " + cal1.getTime().toInstant());                         // returns new date object plus one hour
        Instant inst2 = cal2.getTime().toInstant();
        System.out.println("inst1.compareTo(inst2) = " + inst1.compareTo(inst2));
*/

    }
}
