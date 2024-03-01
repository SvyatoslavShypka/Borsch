package eu.dar3.borsch.authentification;

import eu.dar3.borsch.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

import java.security.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

//@Entity
//@Getter
//@Setter
public class VerificationToken {
/*
    @Override
    public String toString() {
        return "VerificationToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", user=" + user +
                ", expiryDate=" + expiryDate +
                '}';
    }

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Instant expiryDate = calculateExpiryDate(EXPIRATION);

    private Instant calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return (new Date(cal.getTime().getTime())).toInstant();
    }
*/

    // standard constructors, getters and setters
}
