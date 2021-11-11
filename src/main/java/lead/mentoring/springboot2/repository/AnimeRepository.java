package lead.mentoring.springboot2.repository;

import lead.mentoring.springboot2.domain.Anime;

import java.util.List;

public interface AnimeRepository {
    List<Anime> listAll();
}
