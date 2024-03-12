package eu.dar3.borsch.security;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import static eu.dar3.borsch.utils.Constants.DEFAULT_PROPERTIES_FILE_NAME;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    ResourceBundle resourceBundle(){
//            Locale locale = new Locale(properties.getProperty("app.language"));
        return ResourceBundle.getBundle("messages", new Locale("en"));
    }

    @Bean
    public Properties properties(){
        Properties properties = new Properties();
        try {
            properties.load(WebSecurityConfig.class.getClassLoader().getResourceAsStream(DEFAULT_PROPERTIES_FILE_NAME));
//            ResourceBundle exampleBundle = ResourceBundle.getBundle("messages", locale);
//            System.out.println("exampleBundle.getString(\"page.login.label.information\") = " + exampleBundle.getString("page.login.label.information"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    /*@Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().disable().csrf().disable()
            .authorizeHttpRequests(requests -> {
                    requests
                        .requestMatchers(
                            "/about",
                            "/info",
                            "/login",
                            "/sendEmailConfirmation",
                            "/img/**",
                            "/css/**",
                            "/recipe/share/**",
                            "/error/**",
                            "/register/**"
                        )
                        .permitAll();
                    requests
                        .requestMatchers("/").permitAll()
                        .anyRequest()
                        .authenticated();
                }
            )
            .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
            .formLogin(a -> a.loginPage("/login"))
            .formLogin(a -> a.defaultSuccessUrl("/", true).permitAll())
            .logout(LogoutConfigurer::permitAll);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().passwordEncoder(passwordEncoder())
            .dataSource(dataSource)
            .usersByUsernameQuery(
                "select username, password, enabled from access.users where username=?")
            .authoritiesByUsernameQuery("select username, role from access.users where username=?");
    }
}
