package lead.mentoring.springboot2.util;

import lead.mentoring.springboot2.domain.Anime;
import lead.mentoring.springboot2.requests.AnimePostRequestBody;

public class AnimePostRequestBodyCreator {

    public static AnimePostRequestBody createdAnimePostRequestBody(){
        return AnimePostRequestBody.builder()
                .nome(AnimeCreator.generateAnimeToSave().getNome())
                .build();
    }
}
