package lead.mentoring.springboot2.repository;

import lead.mentoring.springboot2.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

}