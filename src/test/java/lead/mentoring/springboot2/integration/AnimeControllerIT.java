package lead.mentoring.springboot2.integration;

import lead.mentoring.springboot2.domain.Anime;
import lead.mentoring.springboot2.repository.AnimeRepository;
import lead.mentoring.springboot2.util.AnimeCreator;
import lead.mentoring.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
//Configuração padrão de banco de dados, em nosso caso o H2 está configurado para o scopo de teste
//Executando as configurações padrãos do spring test em uma porta aleatória a cada execução
class AnimeControllerIT {

    @Autowired
    //RestTemplate identifica automaticamente a porta de execução dos testes
    private TestRestTemplate testRestTemplate;
    //@LocalServerPort
    //Pegando o valor da porta que a aplicação está rodando
    //private int port;
    @Autowired
    private AnimeRepository testAnimeRepository;
    @Test
    @DisplayName("Return list of anime inside page object when sucessful")
    void list_ReturnListOfAnimesInsidePageObject_WhenSucessful(){

        Anime animeSaved = testAnimeRepository.save(AnimeCreator.generateAnimeToSave());

        String expectedName = animeSaved.getNome();

        PageableResponse<Anime> animePageableResponse = testRestTemplate.exchange("/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();

        Assertions.assertThat(animePageableResponse)
                .isNotNull();

        Assertions.assertThat(animePageableResponse.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePageableResponse.toList().get(0).getNome()).isEqualTo(expectedName);

    }
}
