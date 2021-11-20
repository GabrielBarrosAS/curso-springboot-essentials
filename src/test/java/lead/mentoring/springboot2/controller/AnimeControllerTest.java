package lead.mentoring.springboot2.controller;

import lead.mentoring.springboot2.domain.Anime;
import lead.mentoring.springboot2.service.AnimeService;
import lead.mentoring.springboot2.util.AnimeCreator;
import lead.mentoring.springboot2.util.DateUtil;
import lombok.extern.log4j.Log4j2;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

        String dateUtilString = "dateUtilMock return";
        BDDMockito.when(dateUtilMock.formaLocalDateTimeToDatabaseStyle(ArgumentMatchers.any()))
                .thenReturn(dateUtilString);
    }

    @Test
    @DisplayName("Return list of animes inside page object when successfil")
    void list_ReturnListOfAnimesInsidePageObject_WhenSuccessful(){
        String nameExpected = AnimeCreator.generateAnimeValid().getNome();

        Page<Anime> animePage = animeController.list(null).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getNome()).isEqualTo(nameExpected);
    }
}