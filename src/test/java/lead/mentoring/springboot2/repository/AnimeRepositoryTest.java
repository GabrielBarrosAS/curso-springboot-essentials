package lead.mentoring.springboot2.repository;

import lead.mentoring.springboot2.domain.Anime;
import lead.mentoring.springboot2.util.AnimeCreator;
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

    @Test
    @DisplayName("Testes para operação save")
    void save_PersistenceData_WhenSuccesssful(){
        Anime animeGenerate = AnimeCreator.generateAnimeToSave();
        Anime animeSaved = this.animeRepository.save(animeGenerate);

        Assertions.assertThat(animeSaved).isNotNull();
        Assertions.assertThat(animeSaved.getId()).isNotNull();
        Assertions.assertThat(animeSaved.getNome()).isEqualTo(animeGenerate.getNome());
    }

    @Test
    @DisplayName("Teste para a operação de Update")
    void save_UpdataData_WhenSuccessful(){
        Anime animeGenerate = AnimeCreator.generateAnimeValid();

        Anime animeUpdate = this.animeRepository.save(animeGenerate);

        Assertions.assertThat(animeUpdate).isNotNull();
        Assertions.assertThat(animeUpdate.getId()).isNotNull();
        Assertions.assertThat(animeUpdate.getNome()).isEqualTo(animeGenerate.getNome());
    }

    @Test
    @DisplayName("Teste para a operação de Delete")
    void delete_RemoveData_WhenSuccessful(){
        Anime animeGenerate = AnimeCreator.generateAnimeToSave();
        this.animeRepository.save(animeGenerate);

        this.animeRepository.delete(animeGenerate);

        Optional<Anime> animeFind = this.animeRepository.findById(animeGenerate.getId());
        Assertions.assertThat(animeFind).isEmpty();
    }

    @Test
    @DisplayName("Teste para a operação de findByName")
    void findByName_ReturnData_WhenSuccessful(){
        Anime animeGenerate = AnimeCreator.generateAnimeToSave();
        this.animeRepository.save(animeGenerate);

        List<Anime> animesFind = this.animeRepository.findByNome(animeGenerate.getNome());

        Assertions.assertThat(animesFind).isNotEmpty();

        animesFind.forEach(anime -> {
            Assertions.assertThat(anime.getNome()).isEqualTo(animeGenerate.getNome());
        });

        Assertions.assertThat(animesFind).contains(animeGenerate);
    }

    @Test
    @DisplayName("Teste para a operação de findByName quando o nome não está no banco")
    void findByName_ReturnEmptyList_WhenAnimeIsNotFound(){
        List<Anime> animesFind = this.animeRepository.findByNome(AnimeCreator.generateAnimeValid().getNome());

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