package lead.mentoring.springboot2.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
//Criando um bean que será carregado na aplicação configurando questões de segurança
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    //Definir oq vamos protejer com o protocolo http
    protected void configure(HttpSecurity http) throws Exception {
        //Indicando que toda requisição http deve passar pela autorização definida a seguir
        //Todas as requests -> Todas devem estar autenticadas -> Usando a forma httpBasic(existem mais forma possíveis)
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
        //todas as requisições dos controller vão ter que passar pela autenticação básica
    }

    @Override
    //Definir usuários em memória e criptografica básica para o password
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        //criando nosso gerenciador de autenticação
        log.info("Password encoded {}",passwordEncoder.encode("test"));

        //Vamos criar nossos usuários em memória da aplicação
        auth.inMemoryAuthentication()
                .withUser("spring-boot-essentials")
                .password(passwordEncoder.encode("spring-boot-essentials-password"))
                .roles("USER","ADMIN")
                .and()
                .withUser("Gabriel-Barros")
                .password(passwordEncoder.encode("Gabriel-Barros-password"))
                .roles("USER");
    }
}
