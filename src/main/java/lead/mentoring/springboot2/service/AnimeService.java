package lead.mentoring.springboot2.service;

import lead.mentoring.springboot2.domain.Anime;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimeService {

    public List<Anime> listAll(){
        return List.of(new Anime(1L,"DBZZZZZZZZ"),new Anime(2L,"Segundo Anime"));
    }

}
