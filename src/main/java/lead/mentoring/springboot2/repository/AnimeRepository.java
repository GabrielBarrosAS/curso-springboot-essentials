package lead.mentoring.springboot2.repository;

import lead.mentoring.springboot2.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimeRepository extends JpaRepository<Anime,Long> {

    //Ao criar um novo método findByAtributo name o jpa entende oque deve buscar no banco de dados
    public List<Anime> findByNome(String nome);

}
