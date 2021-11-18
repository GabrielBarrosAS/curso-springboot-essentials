package lead.mentoring.springboot2.service;

import lead.mentoring.springboot2.domain.Anime;
import lead.mentoring.springboot2.exception.BadRequestException;
import lead.mentoring.springboot2.mapper.AnimeMapper;
import lead.mentoring.springboot2.repository.AnimeRepository;
import lead.mentoring.springboot2.requests.AnimePostRequestBody;
import lead.mentoring.springboot2.requests.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
//Mostrando do spring que alguém pode precisar injetar essa classe
@RequiredArgsConstructor
//Spring realiar a injeção de depência necessária nessa classe

public class AnimeService {

    private final AnimeRepository animeRepository;


    public Page<Anime> listAll(Pageable pageable) {
        return animeRepository.findAll(pageable);
    }

    public List<Anime> listAllNoPageable() {
        return animeRepository.findAll();
    }

    public Anime findByIdOrThrowBadRequestException(long id){
        return animeRepository.findById(id).
                orElseThrow(() -> new BadRequestException("Anime not found"));
    }

    public List<Anime> findByNome(String nome){
        return animeRepository.findByNome(nome);
    }

    //@Transactional(rollbackFor = Exception.class)
    //Faz com que as alterações só sejam salvas/"commitadas" após o fim do método
    //Existem tipos de execessões que podem ser incluídos no roll back
    //Exception inicialmente não é coberta, mas podemos incluir com parâmetro
    @Transactional
    public Anime save(AnimePostRequestBody animePostRequestBody) {
        return animeRepository.save(AnimeMapper.INSTANCE.toAnime(animePostRequestBody));
    }

    public void delete(long id) {
        animeRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(AnimePutRequestBody animePutRequestBody) {
        findByIdOrThrowBadRequestException(animePutRequestBody.getId());

        Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
        anime.setId(animePutRequestBody.getId());

        animeRepository.save(anime);
    }
}
