package me.wilsimpson.testmmo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthTokenFilter.class);

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception
    {
        builder.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
    }

    // Used when sending passwords
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    // Used so we used the same authentication manager with the web security config
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAuthTokenFilter getJwtAuthTokenFilter()
    {
        return new JwtAuthTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.cors()
            .and()
            // Dont need since we're authorized
            .csrf().disable()

            // Set the exception handling to the entry point which will be logged and responded to
            .exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint).and()

            // Using JWT so we can be stateless
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

            // Force authorization for all api requests
            .authorizeRequests()
                .antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers("/api/v1/pub/**").permitAll()
                .antMatchers("/api/v1/testing/**").permitAll()
                .antMatchers("/api/v1/admin/**").hasAnyAuthority("ADMIN_PRIVILEGES_ALL_RW")
                .antMatchers("/api/v1/mod/**").hasAnyAuthority("MOD_PRIVILEGES_ALL_RW")
                .antMatchers("/api/v1/user/**").hasAnyAuthority("USER_PRIVILEGES_ALL_RW")
            .and()

            // Setup filter for authentication
            .addFilterBefore(getJwtAuthTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Collections.singletonList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
