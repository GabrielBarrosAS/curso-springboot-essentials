package lead.mentoring.springboot2.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableArgumentResolver;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
//Adicionando uma configuração global ao spring
public class ProjectWebMvcConfigurer implements WebMvcConfigurer {

    //Vamos definir um novo padrão de tamanho da paginação e página padrão
    //Retornando sempre a primeira página, todas as páginas terão 5 elementos
    //Pode ser sobrescrito por URL
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        PageableHandlerMethodArgumentResolver pageHandler = new PageableHandlerMethodArgumentResolver();
        pageHandler.setFallbackPageable(PageRequest.of(0,5));
        resolvers.add(pageHandler);
    }
}
