package eu.dar3.borsch;

import eu.dar3.borsch.authentification.VerificationToken;
import eu.dar3.borsch.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Main {
    public static void main(String[] args) {

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



        /*LocalDate localDate = LocalDate.now();

        // Convert the java.time.LocalDate object to a java.sql.Date object
        Date sqlDate = Date.valueOf(localDate);

        System.out.println("localDate: " + localDate);
        System.out.println("sqlDate: " + sqlDate);
*/

        /*Instant instantOne;
        instantOne = Calendar.getInstance().toInstant();
        System.out.println("instantOne = " + instantOne);
        Instant instantTwo;
        instantTwo = Calendar.getInstance().toInstant();
        System.out.println("instantTwo = " + instantTwo);
        System.out.println(instantTwo.compareTo(instantOne));*/

    }
}
