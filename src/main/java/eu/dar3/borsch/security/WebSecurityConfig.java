package eu.dar3.borsch.security;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
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
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import static eu.dar3.borsch.utils.Constants.DEFAULT_PROPERTIES_FILE_NAME;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {

    @Configuration
    public static class MultipartConfig {

        @Bean
        public MultipartConfigElement multipartConfigElement() {
            MultipartConfigFactory factory = new MultipartConfigFactory();
            factory.setMaxFileSize(DataSize.ofBytes(50 * 1024 * 1024));
            factory.setMaxRequestSize(DataSize.ofBytes(50 * 1024 * 1024));
            return factory.createMultipartConfig();
        }
    }

    @Autowired
    private DataSource dataSource;

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.ENGLISH);
        slr.setLocaleAttributeName("session.current.locale");
        slr.setTimeZoneAttributeName("session.current.timezone");
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor
                = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean("messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource =
                new ResourceBundleMessageSource();
        messageSource.setBasenames("language/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public Properties properties() {
        Properties properties = new Properties();
        try {
            properties.load(WebSecurityConfig.class.getClassLoader().getResourceAsStream(DEFAULT_PROPERTIES_FILE_NAME));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

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
//                            "/**",
//                            "/static/**",
                            "/index",
                            "/about",
                            "/info",
                            "/login",
                            "/sendEmailConfirmation",
                            "/img/**",
                            "/css/**",
                            // TODO check if needed for uploads
                            "/uploads/**",
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
