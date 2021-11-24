package lead.mentoring.springboot2.integration;

import lead.mentoring.springboot2.domain.Anime;
import lead.mentoring.springboot2.domain.SpringbootEssentialsUser;
import lead.mentoring.springboot2.repository.AnimeRepository;
import lead.mentoring.springboot2.repository.SpringbootEssentialsUserRepository;
import lead.mentoring.springboot2.requests.AnimePostRequestBody;
import lead.mentoring.springboot2.util.AnimeCreator;
import lead.mentoring.springboot2.util.AnimePostRequestBodyCreator;
import lead.mentoring.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
//Configuração padrão de banco de dados, no nosso caso o H2 está configurado para o scopo de teste
//Executando as configurações padrão do spring teste em uma porta aleatória a cada execução
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//Indicando que o spring deve "limpar" o banco antes de cada teste
//Buscando o efeito do transactional, para reverter as alterações no banco, ou fazer os testes considerando persistencia
class AnimeControllerIT {

    @Autowired
    //RestTemplate identifica automaticamente a porta de execução dos testes
    @Qualifier(value = "TestRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateUser;

    @Autowired
    @Qualifier(value = "TestRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateAdmin;
    //@LocalServerPort
    //Pegando o valor da porta que a aplicação está rodando
    //private int port;
    @Autowired
    private AnimeRepository testAnimeRepository;

    @Autowired
    private SpringbootEssentialsUserRepository springbootEssentialsUserRepository;

    private static final SpringbootEssentialsUser USER = SpringbootEssentialsUser.builder()
            .nome("nome aleatorio")
            .authorities("ROLE_USER")
            .username("gabrielbarrosuser")
            .password("{bcrypt}$2a$10$PneE2JAgbrDAjSpv.vNdpeF6X0r9UErjY259cbhtgmZOwf1/okylS")
            .build();

    private static final SpringbootEssentialsUser ADMIN = SpringbootEssentialsUser.builder()
            .nome("nome aleatorio")
            .authorities("ROLE_ADMIN,ROLE_USER")
            .username("gabrielbarrosadmin")
            .password("{bcrypt}$2a$10$PneE2JAgbrDAjSpv.vNdpeF6X0r9UErjY259cbhtgmZOwf1/okylS")
            .build();

    @TestConfiguration
    @Lazy
    //Bean irá demorar mais para ser carregado para que possa ser iniciada depois da configuração de porta
    //Iremos criar essa classe para configurar nossos testes injetando um usuário com ROLE_USER
    static class config{
        @Bean("TestRestTemplateRoleUser")
        //renomeando o bean para referenciar, pois existem duas possibilidades de carregamento em que for usar
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port){
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("gabrielbarrosuser","defauldPassword");

            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean("TestRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port){
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("gabrielbarrosadmin","defauldPassword");

            return new TestRestTemplate(restTemplateBuilder);
        }
    }

    @Test
    @DisplayName("Return list of anime inside page object when sucessful")
    void list_ReturnListOfAnimesInsidePageObject_WhenSucessful(){

        Anime animeSaved = testAnimeRepository.save(AnimeCreator.generateAnimeToSave());

        String expectedName = animeSaved.getNome();

        //Criando um usuário em memória, pois não usamos o banco real nos testes
        springbootEssentialsUserRepository.save(USER);

        PageableResponse<Anime> animePageableResponse = testRestTemplateUser.exchange("/animes", HttpMethod.GET, null,
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
        springbootEssentialsUserRepository.save(USER);

        String expectedName = animeSaved.getNome();

        List<Anime> animeslist = testRestTemplateUser.exchange("/animes/all", HttpMethod.GET, null,
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
        springbootEssentialsUserRepository.save(USER);

        //Usar exchange ou getforobject
        Anime anime = testRestTemplateUser.exchange("/animes/{id}", HttpMethod.GET, null,
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
        springbootEssentialsUserRepository.save(USER);

        String expectedName = animeSaved.getNome();

        List<Anime> animeList = testRestTemplateUser.exchange("/animes/param?nome={nome}", HttpMethod.GET, null,
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
        springbootEssentialsUserRepository.save(USER);

        List<Anime> animeList = testRestTemplateUser.exchange("/animes/param?nome={nome}", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                },AnimeCreator.generateAnimeValid().getNome()).getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("Save and return one anime when successful")
    void save_ReturnOneAnime_WhenSuccessful(){
        springbootEssentialsUserRepository.save(ADMIN);

        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createdAnimePostRequestBody();
        ResponseEntity<Anime> anime = testRestTemplateAdmin.postForEntity("/animes/admin/",animePostRequestBody,Anime.class);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(anime.getBody()).isNotNull();
        Assertions.assertThat(anime.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("Save return 403 when user role is not admin")
    void save_Return403_WhenUserIsNotAdmin(){
        springbootEssentialsUserRepository.save(USER);

        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createdAnimePostRequestBody();
        ResponseEntity<Anime> anime = testRestTemplateUser.postForEntity("/animes/admin/",animePostRequestBody,Anime.class);

        //Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        Assertions.assertThat(anime.getBody()).isNotNull();
        //Assertions.assertThat(anime.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("Replace update anime when successful")
    void replace_UpdateAnime_WhenSuccessful(){
        Anime animeSaved = testAnimeRepository.save(AnimeCreator.generateAnimeToSave());
        springbootEssentialsUserRepository.save(ADMIN);

        animeSaved.setNome("update");

        ResponseEntity<Void> animeResponseEntity = testRestTemplateAdmin.exchange("/animes/admin/",
                HttpMethod.PUT,
                new HttpEntity<>(animeSaved),
                Void.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Replace return 403 when user role is not admin")
    void replace_Return403_WhenUserIsNotAdmin(){
        Anime animeSaved = testAnimeRepository.save(AnimeCreator.generateAnimeToSave());
        springbootEssentialsUserRepository.save(USER);

        animeSaved.setNome("update");

        ResponseEntity<Void> animeResponseEntity = testRestTemplateUser.exchange("/animes/admin/",
                HttpMethod.PUT,
                new HttpEntity<>(animeSaved),
                Void.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("Delete removes anime when successful")
    void delete_RemoveAnime_WhenSuccessful(){
        Anime animeSaved = testAnimeRepository.save(AnimeCreator.generateAnimeToSave());
        springbootEssentialsUserRepository.save(ADMIN);

        ResponseEntity<Void> animeResponseEntity = testRestTemplateAdmin.exchange("/animes/admin/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                animeSaved.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("Delete return 403 when user role is not admin")
    void delete_Return403_WhenUserIsNotAdmin(){
        Anime animeSaved = testAnimeRepository.save(AnimeCreator.generateAnimeToSave());
        springbootEssentialsUserRepository.save(USER);

        ResponseEntity<Void> animeResponseEntity = testRestTemplateUser.exchange("/animes/admin/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                animeSaved.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }
}
