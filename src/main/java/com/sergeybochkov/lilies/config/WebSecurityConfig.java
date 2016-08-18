package com.sergeybochkov.lilies.config;

import com.sergeybochkov.lilies.service.UserServiceDetailsDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserServiceDetailsDB service;

    @Autowired
    public WebSecurityConfig(UserServiceDetailsDB service) {
        this.service = service;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(service)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .and()
                .formLogin().loginPage("/admin/login/").defaultSuccessUrl("/admin/music/").permitAll()
                .and()
                .logout().logoutUrl("/admin/logout/").deleteCookies("remember-me").logoutSuccessUrl("/").permitAll()
                .and()
                .rememberMe();
    }
}
