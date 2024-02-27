package eu.dar3.borsch;

import eu.dar3.borsch.authentification.VerificationToken;
import eu.dar3.borsch.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Calendar;

public class Main {
    public static void main(String[] args) {
        User user = new User();
        VerificationToken verificationToken = new VerificationToken();

        user.setCreatedDate(Calendar.getInstance().toInstant());
        System.out.println("user = " + user);
        System.out.println(verificationToken);
    }
}
