package lead.mentoring.springboot2.repository;

import lead.mentoring.springboot2.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@DataJpaTest
//Habilitando configurações padrão para o escopo de teste
//Dependencias com <scope>test<scope>, pom.xml estamos definindo o h2
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)//N usar banco de dados em memória
@DisplayName("Testes para Repository")
class AnimeRepositoryTest {
    @Autowired
    private AnimeRepository animeRepository;

    private Anime generateAnime(){
        return Anime.builder().nome("Anime para teste").build();
    }

    @Test
    @DisplayName("Testes para operação save")
    void save_PersistenceData_WhenSuccesssful(){
        Anime animeGenerate = generateAnime();
        Anime animeSaved = this.animeRepository.save(animeGenerate);

        Assertions.assertThat(animeSaved).isNotNull();
        Assertions.assertThat(animeSaved.getId()).isNotNull();
        Assertions.assertThat(animeSaved.getNome()).isEqualTo(animeGenerate.getNome());
    }

    @Test
    @DisplayName("Teste para a operação de Update")
    void save_UpdataData_WhenSuccessful(){
        Anime animeGenerate = generateAnime();
        Anime animeSaved = this.animeRepository.save(animeGenerate);

        animeSaved.setNome("Name update");
        Anime animeUpdate = this.animeRepository.save(animeSaved);

        Assertions.assertThat(animeUpdate).isNotNull();
        Assertions.assertThat(animeUpdate.getId()).isNotNull();
        Assertions.assertThat(animeUpdate.getNome()).isEqualTo(animeSaved.getNome());
    }

    @Test
    @DisplayName("Teste para a operação de Delete")
    void delete_RemoveData_WhenSuccessful(){
        Anime animeGenerate = generateAnime();
        Anime animeSaved = this.animeRepository.save(animeGenerate);

        this.animeRepository.delete(animeSaved);

        Optional<Anime> animeFind = this.animeRepository.findById(animeSaved.getId());
        Assertions.assertThat(animeFind).isEmpty();
    }

    @Test
    @DisplayName("Teste para a operação de findByName")
    void findByName_ReturnData_WhenSuccessful(){
        Anime animeGenerate = generateAnime();
        Anime animeSaved = this.animeRepository.save(animeGenerate);
        Anime animeSaved2 = this.animeRepository.save(animeGenerate);

        List<Anime> animesFind = this.animeRepository.findByNome(animeSaved.getNome());

        Assertions.assertThat(animesFind).isNotEmpty();

        animesFind.forEach(anime -> {
            Assertions.assertThat(anime.getNome()).isEqualTo(generateAnime().getNome());
        });

        Assertions.assertThat(animesFind).contains(animeSaved);
        Assertions.assertThat(animesFind).contains(animeSaved2);
    }

    @Test
    @DisplayName("Teste para a operação de findByName quando o nome não está no banco")
    void findByName_ReturnEmptyList_WhenAnimeIsNotFound(){
        List<Anime> animesFind = this.animeRepository.findByNome(generateAnime().getNome());

        Assertions.assertThat(animesFind).isEmpty();
    }

    @Test
    @DisplayName("Verificando se exceções esperadas são lançadas corretamente")
    void save_ThrowsConstraintViolationException_WhenNameIsEmpty(){
        Anime anime = new Anime();


        Assertions.assertThatThrownBy(() -> this.animeRepository.save(anime))
                .isInstanceOf(ConstraintViolationException.class);
        //Os dois métodos fazem a mesma coisa mas n pode ser usados juntos
        //Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
        //        .isThrownBy(() -> this.animeRepository.save(anime));
        //        .withMessageContaining("The name cannot be empty")
    }




}