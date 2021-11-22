package lead.mentoring.springboot2.integration;

import lead.mentoring.springboot2.domain.Anime;
import lead.mentoring.springboot2.repository.AnimeRepository;
import lead.mentoring.springboot2.requests.AnimePostRequestBody;
import lead.mentoring.springboot2.util.AnimeCreator;
import lead.mentoring.springboot2.util.AnimePostRequestBodyCreator;
import lead.mentoring.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
//Configuração padrão de banco de dados, em nosso caso o H2 está configurado para o scopo de teste
//Executando as configurações padrãos do spring test em uma porta aleatória a cada execução
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//Indicando que o spring deve "limpar" o banco antes  de cada teste
//Buscando o efeito do transactional, para reverter as alterações no banco, ou fazer os testes considerando persistencia
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

    @Test
    @DisplayName("Return list of animes when successful")
    void listAll_ReturnListOfAnimes_WhenSuccessful(){
        Anime animeSaved = testAnimeRepository.save(AnimeCreator.generateAnimeToSave());

        String expectedName = animeSaved.getNome();

        List<Anime> animeslist = testRestTemplate.exchange("/animes/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animeslist)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeslist.get(0).getNome()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Return one anime when successful")
    void findById_ReturnOneAnime_WhenSuccessful(){
        Anime animeSaved = testAnimeRepository.save(AnimeCreator.generateAnimeToSave());
        Long expectedId = AnimeCreator.generateAnimeValid().getId();

        //Usar exchange ou getforobject
        Anime anime = testRestTemplate.exchange("/animes/{id}", HttpMethod.GET, null,
                new ParameterizedTypeReference<Anime>() {
                },expectedId).getBody();

        Assertions.assertThat(anime)
                .isNotNull();

        Assertions.assertThat(anime.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("FindByName Return list of anime when successful")
    void findByName_ReturnListOfAnime_WhenSuccessful(){
        Anime animeSaved = testAnimeRepository.save(AnimeCreator.generateAnimeToSave());

        String expectedName = animeSaved.getNome();

        List<Anime> animeList = testRestTemplate.exchange("/animes/param?nome={nome}", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                },expectedName).getBody();

        Assertions.assertThat(animeList)
                .isNotEmpty()
                .isNotNull()
                .hasSize(1);

        Assertions.assertThat(animeList.get(0).getNome()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName return list empty of anime when anime is not found")
    void findByName_ReturnEmptyListOfAnime_WhenAnimeIsNotFound(){

        List<Anime> animeList = testRestTemplate.exchange("/animes/param?nome={nome}", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                },AnimeCreator.generateAnimeValid().getNome()).getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save and return one anime when successful")
    void save_ReturnOneAnime_WhenSuccessful(){

        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createdAnimePostRequestBody();
        ResponseEntity<Anime> anime = testRestTemplate.postForEntity("/animes/",animePostRequestBody,Anime.class);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(anime.getBody()).isNotNull();
        Assertions.assertThat(anime.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("replace update anime when successful")
    void replace_UpdateAnime_WhenSuccessful(){

        Anime animeSaved = testAnimeRepository.save(AnimeCreator.generateAnimeToSave());
        animeSaved.setNome("update");


        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes/",
                HttpMethod.PUT,
                new HttpEntity<>(animeSaved),
                Void.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemoveAnime_WhenSuccessful(){

        Anime animeSaved = testAnimeRepository.save(AnimeCreator.generateAnimeToSave());

        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                animeSaved.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }
}
