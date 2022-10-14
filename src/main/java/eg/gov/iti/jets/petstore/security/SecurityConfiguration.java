package eg.gov.iti.jets.petstore.security;


import eg.gov.iti.jets.petstore.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AuthService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthorizationFilter authorizationFilter;


    @Autowired
    public SecurityConfiguration(AuthService userService, BCryptPasswordEncoder bCryptPasswordEncoder, AuthorizationFilter authorizationFilter) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authorizationFilter = authorizationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors().and()
                .csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/admins/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/products/**").permitAll()
                .antMatchers(HttpMethod.GET,"/search/**").permitAll()
                .antMatchers("/api-docs/**").permitAll()
                .antMatchers(HttpMethod.GET,"/brands/**").permitAll()
                .antMatchers(HttpMethod.GET,"/categories/**").permitAll()
                .antMatchers(HttpMethod.GET,"/types/**").permitAll()
                .antMatchers("/customers/signUp/**").permitAll()
                .antMatchers(HttpMethod.GET,"/customers/**").permitAll()
                .antMatchers("/sellers/signUp").permitAll()
                .antMatchers("/services/**").permitAll()
                .antMatchers("/serviceproviders/signUp").permitAll()
                .antMatchers("/sellers/**").hasAnyAuthority("ROLE_SELLER", "ROLE_ADMIN")
                .antMatchers("/serviceproviders/**").hasAnyRole("SERVICE_PROVIDER", "ADMIN")
                .antMatchers("/auth/**")
                .permitAll()
                .anyRequest()
                .authenticated()
        ;
        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
//                .addFilter(new AuthenticationFilter(authenticationManager(), jwtUtil))

    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
