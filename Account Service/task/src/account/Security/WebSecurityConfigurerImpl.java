package account.Security;

import account.Exceptions.CustomAccessDeniedHandler;
import account.Services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@EnableWebSecurity
public class WebSecurityConfigurerImpl extends WebSecurityConfigurerAdapter {


    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(getEntryPoint())
                .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console.
                .and()
                .authorizeRequests() // manage access
                .mvcMatchers("/h2/**").permitAll()
                .mvcMatchers("api/acct/**").hasAnyRole("ACCOUNTANT")
                .mvcMatchers("api/admin/**").hasAnyRole("ADMINISTRATOR")
                .mvcMatchers("api/empl/**").hasAnyRole("ACCOUNTANT", "USER")
                .mvcMatchers("api/security/events/**").hasAnyRole("AUDITOR")
                .mvcMatchers(HttpMethod.POST, "api/auth/signup").permitAll()
                .mvcMatchers(HttpMethod.POST, "/actuator/shutdown").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public AuthenticationEntryPoint getEntryPoint() {
        return new UserAuthenticationEntryPoint();
    }


}
