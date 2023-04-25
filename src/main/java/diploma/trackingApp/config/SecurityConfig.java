package diploma.trackingApp.config;

import diploma.trackingApp.services.security.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //конфигурируем сам Spring Security
        //конфигурируем авторизацию
        //читается сверху вниз
        //в начале более специфичные, а потом общие
        //formLogin() - форма для логина
        //csrf().disable() - отключается защиту от межсайтовой подделки запросов
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/student/**").hasRole("STUDENT")
                .antMatchers("/worker/**").hasRole("WORKER")
                .antMatchers("/", "/auth/login", "/auth/registration", "/error").permitAll()
                .anyRequest().hasAnyRole("STUDENT", "ADMIN")
                .and()
                .formLogin().loginPage("/auth/login")
                .usernameParameter("email")
                .loginProcessingUrl("/process_login")
                .successHandler((request, response, authentication) -> {
                    for (GrantedAuthority authority : authentication.getAuthorities()) {
                        if (authority.getAuthority().equals("ROLE_ADMIN")) {
                            response.sendRedirect("/admin/main");
                            return;
                        } else if (authority.getAuthority().equals("ROLE_STUDENT")) {
                            response.sendRedirect("/student/main");
                            return;
                        } else if (authority.getAuthority().equals("ROLE_WORKER")) {
                            response.sendRedirect("/worker/main");
                            return;
                        }
                    }
                    // если не удалось определить роль пользователя, перенаправляем на дефолтную страницу
                    response.sendRedirect("/");
                })
                .failureUrl("/auth/login?error")
                .and()
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessUrl("/auth/login");
    }

    //настраивает аутентификацию
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(getPasswordEncoder());//в провайдере передаём логику аутентификации
    }

    //необходимо для шифрования паролей
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
