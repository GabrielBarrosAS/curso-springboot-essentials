package lead.mentoring.springboot2.service;

import lead.mentoring.springboot2.domain.Anime;
import lead.mentoring.springboot2.exception.BadRequestException;
import lead.mentoring.springboot2.repository.AnimeRepository;
import lead.mentoring.springboot2.util.AnimeCreator;
import lead.mentoring.springboot2.util.AnimePostRequestBodyCreator;
import lead.mentoring.springboot2.util.AnimePutRequestBodyCreator;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {
    @InjectMocks
    //defidindo qual classe iremos realizar os testes de unidade
    private AnimeService animeService;
    @Mock
    //defidindo quais classes são necessárias dentro da classe que será testada
    //Mock irá simular o comportamento das classes esperadas
    private AnimeRepository animeRepositoryMock;

    @BeforeEach//Antes de executar cada teste

    void setUp(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.generateAnimeValid()));
        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);

        List<Anime> listAnime = (List.of(AnimeCreator.generateAnimeValid()));
        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(listAnime);

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeCreator.generateAnimeValid()));

        BDDMockito.when(animeRepositoryMock.findByNome(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.generateAnimeValid()));

        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeCreator.generateAnimeValid());

        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));
    }

    @Test
    @DisplayName("Return list of animes inside page object when successfil")
    void listAll_ReturnListOfAnimesInsidePageObject_WhenSuccessful(){
        String nameExpected = AnimeCreator.generateAnimeValid().getNome();

        Page<Anime> animePage = animeService.listAll(PageRequest.of(1,1));

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getNome()).isEqualTo(nameExpected);
    }

    @Test
    @DisplayName("Return listAllNoPageable of animes when successful")
    void listAllNoPageable_ReturnListOfAnimes_WhenSuccessful(){
        String nameExpected = AnimeCreator.generateAnimeValid().getNome();

        List<Anime> animeList = animeService.listAllNoPageable();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeList.get(0).getNome()).isEqualTo(nameExpected);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException anime when successful")
    void FindByIdOrThrowBadRequestException_ReturnOneAnime_WhenSuccessful(){
        Anime expected = AnimeCreator.generateAnimeValid();

        Anime anime = animeService.findByIdOrThrowBadRequestException(expected.getId());

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
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when anime is not found")
    void FindByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenAnimeIsNotFound(){
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> animeService.findByIdOrThrowBadRequestException(1));
    }

    @Test
    @DisplayName("FindByName Return list of anime when successful")
    void findByName_ReturnListOfAnime_WhenSuccessful(){
        Anime expected = AnimeCreator.generateAnimeValid();

        List<Anime> animeList = animeService.findByNome("nome");

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
        BDDMockito.when(animeRepositoryMock.findByNome(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animeList = animeService.findByNome("param");

        Assertions.assertThat(animeList)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save and return one anime when successful")
    void save_ReturnOneAnime_WhenSuccessful(){

        Anime anime = animeService.save(AnimePostRequestBodyCreator.createdAnimePostRequestBody());

        Assertions.assertThat(anime)
                .isNotNull()
                .isEqualTo(AnimeCreator.generateAnimeValid());
    }

    @Test
    @DisplayName("replace update anime when successful")
    void replace_UpdateAnime_WhenSuccessful(){

        //Verificando se o método não lançou nenhuma exceção
        Assertions.assertThatCode(() -> animeService.replace(AnimePutRequestBodyCreator.createdAnimePutRequestBody()))
                .doesNotThrowAnyException();

    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemoveAnime_WhenSuccessful(){

        //Verificando se o método não lançou nenhuma exceção
        Assertions.assertThatCode(() -> animeService.delete(1))
                .doesNotThrowAnyException();

    }
}