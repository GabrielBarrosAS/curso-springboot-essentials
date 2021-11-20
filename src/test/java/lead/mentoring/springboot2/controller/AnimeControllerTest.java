package lead.mentoring.springboot2.controller;

import lead.mentoring.springboot2.domain.Anime;
import lead.mentoring.springboot2.requests.AnimePostRequestBody;
import lead.mentoring.springboot2.requests.AnimePutRequestBody;
import lead.mentoring.springboot2.service.AnimeService;
import lead.mentoring.springboot2.util.AnimeCreator;
import lead.mentoring.springboot2.util.AnimePostRequestBodyCreator;
import lead.mentoring.springboot2.util.AnimePutRequestBodyCreator;
import lead.mentoring.springboot2.util.DateUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

//@SpringBootTest Tentará executar nossa aplicação por inteiro para executar os testes
//Com o banco desativado, um teste vazio, irá falhar pq a aplicação n é iniciada sem o banco
@ExtendWith(SpringExtension.class)
//Utilizando o Junit com o spring para executar a aplicação
class AnimeControllerTest {
    @InjectMocks
    //defidindo qual classe iremos realizar os testes de unidade
    private AnimeController animeController;
    @Mock
    //defidindo quais classes são necessárias dentro da classe que será testada
    //Mock irá simular o comportamento das classes esperadas
    private AnimeService animeServiceMock;

    @Mock
    private DateUtil dateUtilMock;

    @BeforeEach
    //Antes de executar cada teste

    void setUp(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.generateAnimeValid()));
        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);

        List<Anime> listAnime = (List.of(AnimeCreator.generateAnimeValid()));
        BDDMockito.when(animeServiceMock.listAllNoPageable())
                .thenReturn(listAnime);

        BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.generateAnimeValid());

        BDDMockito.when(animeServiceMock.findByNome(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.generateAnimeValid()));

        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
                .thenReturn(AnimeCreator.generateAnimeValid());

        //Indicando que nada dever ser feito/retornado quando a função retorna void
        BDDMockito.doNothing().when(animeServiceMock).replace(ArgumentMatchers.any(AnimePutRequestBody.class));

        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());

        String dateUtilString = "dateUtilMock return";
        BDDMockito.when(dateUtilMock.formaLocalDateTimeToDatabaseStyle(ArgumentMatchers.any()))
                .thenReturn(dateUtilString);
    }

    @Test
    @DisplayName("Return list of animes inside page object when successfil")
    void listAll_ReturnListOfAnimesInsidePageObject_WhenSuccessful(){
        String nameExpected = AnimeCreator.generateAnimeValid().getNome();

        Page<Anime> animePage = animeController.listAll(null).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getNome()).isEqualTo(nameExpected);
    }

    @Test
    @DisplayName("Return list of animes when successful")
    void listAll_ReturnListOfAnimes_WhenSuccessful(){
        String nameExpected = AnimeCreator.generateAnimeValid().getNome();

        List<Anime> animeList = animeController.listAllNoPageable().getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeList.get(0).getNome()).isEqualTo(nameExpected);
    }

    @Test
    @DisplayName("Return one anime when successful")
    void findById_ReturnOneAnime_WhenSuccessful(){
        Anime expected = AnimeCreator.generateAnimeValid();

        Anime anime = animeController.findById(expected.getId()).getBody();

        Assertions.assertThat(anime)
                .isNotNull();

        Assertions.assertThat(anime.getId())
                .isNotNull()
                .isEqualTo(expected.getId());

        Assertions.assertThat(anime.getNome())
                .isNotEmpty()
                .isNotNull()
                .isEqualTo(expected.getNome());
    }

    @Test
    @DisplayName("FindByName Return list of anime when successful")
    void findByName_ReturnListOfAnime_WhenSuccessful(){
        Anime expected = AnimeCreator.generateAnimeValid();

        List<Anime> animeList = animeController.findByNameParam(expected.getNome()).getBody();

        Assertions.assertThat(animeList)
                .isNotEmpty()
                .isNotNull()
                .hasSize(1);

        Assertions.assertThat(animeList.get(0).getNome()).isEqualTo(expected.getNome());
    }

    @Test
    @DisplayName("findByName return list empty of anime when anime is not found")
    void findByName_ReturnEmptyListOfAnime_WhenAnimeIsNotFound(){
        //Sobrescrevendo/escrevendo comportamento dentro do método
        BDDMockito.when(animeServiceMock.findByNome(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animeList = animeController.findByNameParam("param").getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save and return one anime when successful")
    void save_ReturnOneAnime_WhenSuccessful(){

        Anime anime = animeController.save(AnimePostRequestBodyCreator.createdAnimePostRequestBody()).getBody();

        Assertions.assertThat(anime)
                .isNotNull()
                .isEqualTo(AnimeCreator.generateAnimeValid());
    }

    @Test
    @DisplayName("replace update anime when successful")
    void replace_UpdateAnime_WhenSuccessful(){

        //Verificando se o método não lançou nenhuma exceção
        Assertions.assertThatCode(() -> animeController.replace(AnimePutRequestBodyCreator.createdAnimePostRequestBody()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController.replace(AnimePutRequestBodyCreator.createdAnimePostRequestBody());

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemoveAnime_WhenSuccessful(){

        //Verificando se o método não lançou nenhuma exceção
        Assertions.assertThatCode(() -> animeController.delete(1))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController.delete(1);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }
}