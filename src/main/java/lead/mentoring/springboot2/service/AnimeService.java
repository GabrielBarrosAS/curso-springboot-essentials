package lead.mentoring.springboot2.service;

import lead.mentoring.springboot2.domain.Anime;
import lead.mentoring.springboot2.repository.AnimeRepository;
import lead.mentoring.springboot2.requests.AnimePostRequestBody;
import lead.mentoring.springboot2.requests.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
//Mostrando do spring que alguém pode precisar injetar essa classe
@RequiredArgsConstructor
//Spring realiar a injeção de depência necessária nessa classe

public class AnimeService {

    private final AnimeRepository animeRepository;


    public List<Anime> listAll() {
        return animeRepository.findAll();
    }

    public Anime findByIdOrThrowBadRequestException(long id){
        return animeRepository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Anime not found"));
    }

    public Anime save(AnimePostRequestBody animePostRequestBody) {
        return animeRepository.save(Anime.builder().nome(animePostRequestBody.getNome()).build());
    }

    public void delete(long id) {
        animeRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(AnimePutRequestBody animePutRequestBody) {
        findByIdOrThrowBadRequestException(animePutRequestBody.getId());

        animeRepository.save(Anime.builder()
                            .nome(animePutRequestBody.getNome())
                            .id(animePutRequestBody.getId())
                            .build());
    }
}
