package lead.mentoring.springboot2.repository;

import lead.mentoring.springboot2.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimeRepository extends JpaRepository<Anime,Long> {

    public List<Anime> findByNome(String nome);

}
