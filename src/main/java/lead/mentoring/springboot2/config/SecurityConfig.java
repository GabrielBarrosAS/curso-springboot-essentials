package lead.mentoring.springboot2.config;

import lead.mentoring.springboot2.service.SpringbootEssentialsUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
//Criando um bean que será carregado na aplicação configurando questões de segurança
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SpringbootEssentialsUserService springbootEssentialsUserService;

    @Override
    //Definir oq vamos protejer com o protocolo http
    protected void configure(HttpSecurity http) throws Exception {
        //Indicando que toda requisição http deve passar pela autorização definida a seguir
        //Todas as requests -> Todas devem estar autenticadas -> Usando a forma httpBasic(existem mais forma possíveis)
        //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) -> enviando um token gerado pela
        //aplicação para o front-end poder continuar se comunicando de forma segura ->
        //Assim, após se autenticar você recebe uma chave aleatória para continuar a manipular
        //Por simplificação vamos desabilitar, mas em outros escopos é necessário habilitar
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin() //utilizando a página de login padrão do spring
                .and()
                .httpBasic();
        //todas as requisições dos controller vão ter que passar pela autenticação básica
    }

    @Override
    //Definir usuários em memória e criptografica básica para o password
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        //criando o nosso gerenciador de autenticação
        log.info("Password encoded {}",passwordEncoder.encode("defauldPassword"));

        //Vamos criar nossos usuários em memória da aplicação
        /*auth.inMemoryAuthentication()
                .withUser("spring-boot-essentials")
                .password(passwordEncoder.encode("spring-boot-essentials-password"))
                .roles("USER","ADMIN")
                .and()
                .withUser("Gabriel-Barros")
                .password(passwordEncoder.encode("Gabriel-Barros-password"))
                .roles("USER");*/
        //Importante lembrar que podem exister dois pontos de entradas de usuários,
        //duas fontes diferentes -> banco e memória

        //Por padrão o spring espera um UserDetailsService para realizar autenticação com banco de dados
        //Para isso vamos criar o repository para manter os dados no banco e ao criar o service, iremos
        //implementar uma ‘interface’ que permitirá usar o nosso serviço para o objetivo final
        //Espera algo que implemente UserDetailsService -> loadUserByUsername
        auth.userDetailsService(springbootEssentialsUserService)
                .passwordEncoder(passwordEncoder);
        //Indicando que no banco de dados a senha vai estar criptografada com o decodificador definido acima
    }
}
