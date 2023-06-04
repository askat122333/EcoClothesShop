package com.example.onlineStore.security;


import com.example.onlineStore.enums.Permission;
import com.example.onlineStore.jwtUtil.JwtRequestFilter;
import com.example.onlineStore.userDetails.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        super.configure(auth);
    }

    @Bean
    protected PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/authenticate","/product/category/{categoryId}",
                        "/user/resetPassword","/user/newPassword/{id}/{token}","/order/byUserId/{id}",
                        "/cart/findByUser/{id}","/user/create","/product/allWithImage",
                        "/user/photo/{id}","/category/{id}","/category/all",
                        "/user/oauthSuccess","/product/{id}","/product/all").permitAll()
                .antMatchers("/payment/makePayment","/payment/stripe/{id}," +
                        "/payment/addStripeCustomer",
                        "/paymentCard/create/{userId}","/paymentCard/update/{id}","/paymentCard/delete/{id}",
                        "/order/create","/order/update/{id}","/order/delete/{id}",
                        "/cart/update/**","/cart/addNewProduct","/cart/removeProduct",
                        "/cart/delete/{id}","/order/quickCreate").hasAuthority(Permission.ADMIN_READ.getPermission())
                .antMatchers(HttpMethod.GET,"/order/{id}",
                        "/cart/{id}").hasAuthority(Permission.ADMIN_READ.getPermission())
                .antMatchers(HttpMethod.GET,"/user/**","/paymentCard/{id}","/paymentCard/all",
                        "/order/all","/discount/**","/cart/all").hasAuthority(Permission.ADMIN_UPDATE.getPermission())
                .antMatchers(HttpMethod.POST,"/discount/create",
                        "/category/create","/product/create","/product/*").hasAuthority(Permission.ADMIN_UPDATE.getPermission())
                .antMatchers(HttpMethod.DELETE,"/product/delete/**","/user/delete/**","/discount/delete/**",
                        "/category/delete/**").hasAuthority(Permission.ADMIN_UPDATE.getPermission())
                .antMatchers(HttpMethod.PUT,"/product/update/**","/user/update/**","/discount/update/**",
                        "/category/update/**").hasAuthority(Permission.ADMIN_UPDATE.getPermission())
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .and().httpBasic();
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
